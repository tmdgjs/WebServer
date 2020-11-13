package kr.hs.dgsw.dbook.dbook.Service;

import com.sun.xml.bind.v2.TODO;
import kr.hs.dgsw.dbook.dbook.Exception.EBookException;
import kr.hs.dgsw.dbook.dbook.Exception.TokenException;
import kr.hs.dgsw.dbook.dbook.Exception.UserException;
import kr.hs.dgsw.dbook.dbook.Json.LoginResponse;
import kr.hs.dgsw.dbook.dbook.Json.SignUpResponse;
import kr.hs.dgsw.dbook.dbook.Json.TokenResponse;
import kr.hs.dgsw.dbook.dbook.Repository.ProfileImgRepository;
import kr.hs.dgsw.dbook.dbook.Repository.TokenRepository;
import kr.hs.dgsw.dbook.dbook.Repository.UserRepository;
import kr.hs.dgsw.dbook.dbook.VO.ProfileImg;
import kr.hs.dgsw.dbook.dbook.VO.Token;
import kr.hs.dgsw.dbook.dbook.VO.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private ProfileImgRepository profileImgRepository;

    @Override
    public SignUpResponse signup(User user) {
        try{

            boolean isEmailUsed = this.userRepository.findByEmail(user.getEmail()).isPresent();

            if(isEmailUsed)
                throw new UserException("동일한 이메일이 존재합니다.");

            String password     = user.getPassword();
            String name         = user.getName();
            boolean numericFlag = false;

            if(password.length() < 8)
                throw new UserException("비밀번호를 확인해주세요.");
            else if(name.equals("") || name.length() < 2)
                throw new UserException("이름을 확인해주세요.");

            for(int i = 0 ;i < name.length();i++){
                int index = name.charAt(i);

                if(index >= 48 && index <= 57){
                    numericFlag = true;
                    break;
                }
            }

            if(numericFlag)
                throw new UserException("이름에 숫자가 들어갈 수 없습니다.");
            
            password = convertSHA256(password);
            user.setPassword(password);
            
            userRepository.save(user);
            
            return new SignUpResponse(user.getId());
        }catch (UserException e){
            return new SignUpResponse(e.getMessage());
        }
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
    public LoginResponse login(User user) {
        try{
            Optional<User> objUser = Optional.ofNullable(userRepository.findByEmail(user.getEmail())
                    .orElseThrow(() -> new UserException("일치하는 회원이 없습니다.")));

            String resPassword = convertSHA256(user.getPassword());
            String password = objUser.get().getPassword();

            if(!resPassword.equals(password))
                throw new UserException("비밀번호가 일치하지 않습니다.");

            String userEmail = user.getEmail();
            String token = makeToken();
            String connectIP = findIpAddress();

            Optional<Token> objToken = tokenRepository.findByTokenOwnerId(userEmail);

            if(objToken.orElse(null) == null){
                tokenRepository.save(new Token(userEmail, token, connectIP));
            }else{
                objToken.map(found -> {
                    found.setToken(Optional.ofNullable(token).orElse(found.getToken()));
                    found.setConnectIP(Optional.ofNullable(connectIP).orElse(found.getConnectIP()));

                    return tokenRepository.save(found);
                });
            }

            return new LoginResponse(userEmail, token);
        } catch (UserException e){
            return new LoginResponse(e.getMessage());
        }
    }

    @Override
    public TokenResponse tokenCheck(String token) {

        try{
            Token objToken = tokenRepository.findByToken(token).orElseThrow(()-> new TokenException("존재하는 토큰이 없습니다."));
            String myIp = findIpAddress();
            String ownerId = objToken.getTokenOwnerId();

            if(!objToken.getConnectIP().equals(myIp)){
                throw new TokenException("잘못된 접근입니다.");
            }

            return new TokenResponse(ownerId, token);

        }catch (TokenException e){
            return new TokenResponse(e.getMessage());
        }
    }

    @Override
    public String getToken(Cookie[] myCookies) {

        try{
            if (myCookies != null && myCookies.length > 0) {
                for (Cookie myCookie : myCookies) {
                    if (myCookie.getName().equals("Token")) {
                        return myCookie.getValue();
                    }
                }
            }
        }catch (NullPointerException e){
              e.printStackTrace();
        }
        return "";
    }

    @Override
    public LoginResponse fileUpload(MultipartFile files, Long userNo) {
        LoginResponse loginResponse = new LoginResponse();
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(files.getOriginalFilename()));

        try{
            if(fileName.contains("..")) {
                throw new UserException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            ProfileImg profileImg = new ProfileImg(fileName, files.getContentType(), files.getBytes());
            profileImgRepository.save(profileImg);

            System.out.println(userNo);
            User user = userRepository.findById(userNo).map(found -> {


                found.setProfileFileNo(profileImg.getImgNo());

                return this.userRepository.save(found);

            }).orElseThrow(() ->  new UserException("유저를 찾을 수 없습니다."));

            loginResponse.setUserEmail(user.getEmail());

        } catch (IOException e){
            throw new UserException("Could not store file " + fileName + ". Please try again!");
        }

        return loginResponse;
    }

    public String makeToken(){

        StringBuilder token = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 16; i++) {
            token.append((char) ((int) (random.nextInt(26)) + 97));
        }

        return token.toString();
    }

    public String findIpAddress(){

        String ipaddr = null;

        try {
            ipaddr = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return ipaddr;
    }
}
