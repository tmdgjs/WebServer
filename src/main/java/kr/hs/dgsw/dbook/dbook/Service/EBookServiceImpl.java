package kr.hs.dgsw.dbook.dbook.Service;

import kr.hs.dgsw.dbook.dbook.Exception.EBookException;
import kr.hs.dgsw.dbook.dbook.Json.EBookResponse;
import kr.hs.dgsw.dbook.dbook.Json.LibraryResponse;
import kr.hs.dgsw.dbook.dbook.Json.TokenResponse;
import kr.hs.dgsw.dbook.dbook.Repository.EBookRepository;
import kr.hs.dgsw.dbook.dbook.Repository.LibraryRepository;
import kr.hs.dgsw.dbook.dbook.VO.EBook;
import kr.hs.dgsw.dbook.dbook.VO.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EBookServiceImpl implements EBookService{

    @Autowired
    private EBookRepository eBookRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private LibraryRepository libraryRepository;

    @Override
    public EBookResponse fileUpload(List<MultipartFile> file, TokenResponse tokenResponse) {

        String tokenOwnerID = tokenResponse.getTokenOwnerId();
        EBook objEBook = fileSave(file, tokenOwnerID);

        /*if(objEBook.getEbookFilePath().equals("") || objEBook.getEbookPosterPath().equals("")){
            return new EBookResponse("업로드 에러");
        }*/

        eBookRepository.save(objEBook);

        return new EBookResponse(objEBook);
    }

    @Override
    public EBookResponse fileInfoUpload(EBook eBook, TokenResponse objToken) {

        Long eBookID = eBook.getEbookId();
        Long eBookCategoryId = eBook.getCategoryId();

        List<String> ebookList = listAdd(eBook);

        try{

            if(eBookCategoryId == null){
                throw new EBookException("카테고리 정보가 없습니다.");
            }

            for(String str : ebookList){
                if(str.equals("")){
                    int index = ebookList.indexOf(str);
                    switch (index){
                        case 0 :
                            throw new EBookException("제목 정보가 없습니다.");
                        case 1 :
                            throw new EBookException("작가 정보가 없습니다.");
                    }
                }
            }

            eBookRepository.findById(eBookID).map(found -> {

                found.setEbookTitle(eBook.getEbookTitle());
                found.setEbookSubTitle(eBook.getEbookSubTitle());
                found.setEbookAuthor(eBook.getEbookAuthor());
                found.setCategoryId(eBookCategoryId);
                found.setIsShared(Optional.ofNullable(eBook.getIsShared()).orElseThrow(()-> new EBookException("공유 정보가 없습니다.")));

                return this.eBookRepository.save(found);

            }).orElseThrow(() ->  new FileNotFoundException("책을 찾을 수 없습니다."));

            return new EBookResponse(eBook);

        } catch (EBookException u){

            this.eBookRepository.deleteById(eBookID);
            return new EBookResponse(u.getMessage());

        } catch (FileNotFoundException f){

            return new EBookResponse(f.getMessage());
        }
    }

    @Override
    public EBookResponse updateSharedFlag(Long ebookId, TokenResponse objToken) {
        EBookResponse eBookResponse = new EBookResponse();
        try{
            String userId = objToken.getTokenOwnerId();
            Optional<EBook> objEbook = Optional.ofNullable(this.eBookRepository.findById(ebookId).map(found -> {
                if (!found.getEbookUploader().equals(userId)) {
                    throw new EBookException("책 업로더가 다릅니다.");
                }
                Boolean isShared = found.getIsShared();
                found.setIsShared(!isShared);
                return this.eBookRepository.save(found);
            }).orElseThrow(() -> new EBookException("책 아이디가 없습니다.")));
            eBookResponse.setEBook(objEbook.get());
            return eBookResponse;
        } catch (EBookException e){
            eBookResponse.setMessage(e.getMessage());
            return eBookResponse;
        }
    }

    @Override
    public List<EBook> getSharingBookList(TokenResponse objToken) {
        String userId = objToken.getTokenOwnerId();

        return this.eBookRepository.findByIsSharedAndEbookUploaderNot(true, userId);
    }

    @Override
    public EBookResponse getEBookInfo(Long ebookId, TokenResponse objToken) {
        try{
            EBookResponse eBookResponse = new EBookResponse();
            EBook eBook = this.eBookRepository.findById(ebookId).orElseThrow(() ->  new EBookException("책 정보가 없습니다."));

            eBookResponse.setEBook(eBook);
            String categoryName = categoryService.getCategory(eBook.getCategoryId());
            eBookResponse.setCategory(categoryName);

            return eBookResponse;

        } catch (EBookException e){
            return new EBookResponse(e.getMessage());
        }
    }

    @Override
    public Object getSearchBookList(TokenResponse objToken, String search, int type) {
            String userId = objToken.getTokenOwnerId();
            // type 1 : 내 서재, type 2 : 공유 책 리스트
            if(type == 1){
                List<EBook> eBookList = this.eBookRepository.findByEbookUploaderAndEbookTitleLike(userId, search + "%");
                List<Library> libraries = this.libraryRepository.findByUserId(userId);
                for(Library library : libraries){
                    EBook eBook = this.eBookRepository.findById(library.getEbookId()).orElseThrow(null);
                    if(eBook.getEbookTitle().contains(search)){
                        eBookList.add(eBook);
                    }
                }
                return new LibraryResponse(eBookList, userId);
            }
            else{
                return this.eBookRepository.findByIsSharedAndEbookUploaderNotAndEbookTitleLike(true, userId, search  + "%");
            }
    }

    public EBook fileSave(List<MultipartFile> file, String tokenOwnerID) {

        try {
            EBook objEBook = new EBook();
            objEBook.setEbookUploader(tokenOwnerID);

            //0 : epub, 1 : poster

            objEBook.setEbookFile(file.get(0).getBytes());
            objEBook.setEbookCover(file.get(1).getBytes());
            objEBook.setEbookFileName(file.get(0).getOriginalFilename());
            objEBook.setEbookFileType(file.get(0).getContentType());
            objEBook.setEbookCoverType(file.get(1).getContentType());

            /*String posterOriginalfileName;
            File saveFile;

            if(file.size() == 2) {
                UUID uuid = UUID.randomUUID();

                posterOriginalfileName = file.get(1).getOriginalFilename();
                //saveFile = new File(System.getProperty("user.dir") + "/upload" + "/poster/" + uuid.toString() + "_" + posterOriginalfileName);
                //file.get(1).transferTo(saveFile);
                //objEBook.setEbookPosterPath(saveFile.toString().substring(2));

            } else{
                posterOriginalfileName = System.getProperty("user.dir") + "/upload" + "/unknown/unknownposter.jpg";
                objEBook.setEbookPosterPath(posterOriginalfileName.substring(2));
            }*/

            return objEBook;
        } catch (IOException e) {
            e.printStackTrace();
            return new EBook();
        }
    }


    public String ebookFilePath(MultipartFile file) {
        try {
            String ebookOriginalfileName = file.getOriginalFilename();
            UUID uuid = UUID.randomUUID();

           File saveFile  = new File(System.getProperty("user.dir") + "/upload/" + uuid.toString() + "_" + ebookOriginalfileName);
            file.transferTo(saveFile);

            return saveFile.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public List<String> listAdd(EBook eBook){

        System.out.println(eBook);

        List<String> ebookList = new ArrayList<>();
        ebookList.add(eBook.getEbookTitle());
        ebookList.add(eBook.getEbookAuthor());
        //ebookList.add(eBook.getEbookCategory());

        return ebookList;
    }
}
