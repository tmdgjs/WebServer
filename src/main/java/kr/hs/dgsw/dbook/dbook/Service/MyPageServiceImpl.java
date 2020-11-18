package kr.hs.dgsw.dbook.dbook.Service;

import kr.hs.dgsw.dbook.dbook.Json.MypageResponse;
import kr.hs.dgsw.dbook.dbook.Json.TokenResponse;
import kr.hs.dgsw.dbook.dbook.Repository.EBookRepository;
import kr.hs.dgsw.dbook.dbook.Repository.LibraryRepository;
import kr.hs.dgsw.dbook.dbook.Repository.ProfileImgRepository;
import kr.hs.dgsw.dbook.dbook.Repository.UserRepository;
import kr.hs.dgsw.dbook.dbook.VO.ProfileImg;
import kr.hs.dgsw.dbook.dbook.VO.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.Optional;

@Service
public class MyPageServiceImpl implements MyPageService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private EBookRepository eBookRepository;

    @Autowired
    private ProfileImgRepository profileImgRepository;


    @Override
    public MypageResponse getMyPageInfo(TokenResponse objToken) {

        MypageResponse mypageResponse = new MypageResponse();

        String myEmail = objToken.getTokenOwnerId(); // my

        Optional<User> optionalUser = this.userRepository.findByEmail(myEmail); // find User
        String name = optionalUser.get().getName(); // name;
        Integer profileImgNo = optionalUser.get().getProfileFileNo(); // profileImgNo

        int myUploadCnt   = this.eBookRepository.findByEbookUploader(myEmail).size();  // my upload books
        Integer myLibraryCnt  = this.libraryRepository.findByUserId(myEmail).size() + myUploadCnt; // my library Cnt

        return new MypageResponse(myEmail, name, profileImgNo, myUploadCnt, myLibraryCnt) ;
    }

    @Override
    public ProfileImg getProfileImageInfo(Integer imageNo) {
        return profileImgRepository.findById(imageNo).orElse(null);
    }

    private String convertSHA256(String password) {

        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean updatePassword(TokenResponse objToken, String password) {

        String inputPassword = password.substring(9);

        if(inputPassword.length() < 8)
            return false;

        String convertPW = convertSHA256(inputPassword);

        String email = objToken.getTokenOwnerId();

        Optional<User> optionalUser = Optional.ofNullable(this.userRepository.findByEmail(email).map(found -> {
            found.setPassword(convertPW);
            return this.userRepository.save(found);

        }).orElseGet(null));

        if(optionalUser == null)
            return false;

        return true;
    }
}
