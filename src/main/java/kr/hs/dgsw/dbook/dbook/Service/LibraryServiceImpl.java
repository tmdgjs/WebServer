package kr.hs.dgsw.dbook.dbook.Service;

import kr.hs.dgsw.dbook.dbook.Exception.LibraryException;
import kr.hs.dgsw.dbook.dbook.Json.LibraryResponse;
import kr.hs.dgsw.dbook.dbook.Json.TokenResponse;
import kr.hs.dgsw.dbook.dbook.Repository.EBookRepository;
import kr.hs.dgsw.dbook.dbook.Repository.LibraryRepository;
import kr.hs.dgsw.dbook.dbook.VO.EBook;
import kr.hs.dgsw.dbook.dbook.VO.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LibraryServiceImpl implements LibraryService {

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private EBookRepository eBookRepository;

    @Override
    public LibraryResponse getMyLibrary(TokenResponse objToken) {

        LibraryResponse libraryResponse = new LibraryResponse();
        String userId = objToken.getTokenOwnerId();
        libraryResponse.setUserId(userId);

        List<EBook> myLibrary = this.eBookRepository.findByEbookUploader(userId);
        List<Library> mySharedList = this.libraryRepository.findByUserId(userId);

        for (Library library : mySharedList) {
            Long ebookId = library.getEbookId();
            EBook eBook = this.eBookRepository.findById(ebookId).orElseThrow(null);
            myLibrary.add(eBook);
        }

        libraryResponse.setLibraryList(myLibrary);
        return libraryResponse;
    }

    @Override
    public ResponseEntity<Object> deleteLibraryBook(TokenResponse objToken, Long ebookId) {

        String userId = objToken.getTokenOwnerId();
        Long libraryId = Objects.requireNonNull(this.libraryRepository.findByUserIdAndEbookId(userId, ebookId)
                .orElseThrow(() -> new LibraryException("해당하는 책이 없습니다."))).getLibraryId();

        try{
            this.libraryRepository.deleteById(libraryId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (LibraryException e){
            String message = e.getMessage();
            return new ResponseEntity<>(message, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @Override
    public LibraryResponse addLibraryBook(TokenResponse objToken, Long ebookId) {

        LibraryResponse libraryResponse = new LibraryResponse();


        try{
            String userId = objToken.getTokenOwnerId();
            Optional<Library> ebook = this.libraryRepository.findByUserIdAndEbookId(userId, ebookId);
            if(ebook.isPresent()){
                throw new LibraryException("이미 존재하는 책입니다.");
            }

            Library saveLibrary = this.libraryRepository.save(new Library(ebookId, userId));
            libraryResponse.setLibrary(saveLibrary);
            libraryResponse.setMessage("추가되었습니다.");
            return libraryResponse;
        } catch (LibraryException e){
            libraryResponse.setMessage(e.getMessage());
            libraryResponse.setCode(417);
            return libraryResponse;
        }
    }
}
