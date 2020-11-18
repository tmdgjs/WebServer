package kr.hs.dgsw.dbook.dbook.Controller;

import kr.hs.dgsw.dbook.dbook.Json.MypageResponse;
import kr.hs.dgsw.dbook.dbook.Json.TokenResponse;
import kr.hs.dgsw.dbook.dbook.Service.MyPageService;
import kr.hs.dgsw.dbook.dbook.Service.UserService;
import kr.hs.dgsw.dbook.dbook.VO.ProfileImg;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping("/mypages")
public class MypageController {

    @Autowired
    private MyPageService myPageService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Object> getMyPageInfo(HttpServletRequest request){

        // 토큰 가져오기
        String token = userService.getToken(request.getCookies());

        // 토큰 검증
        TokenResponse objToken = userService.tokenCheck(token);

        if(!objToken.getMessage().equals("")){
            return new ResponseEntity<>(objToken, HttpStatus.SEE_OTHER);
        }

        MypageResponse mypageResponse = new MypageResponse();
        mypageResponse = myPageService.getMyPageInfo(objToken);

        return new ResponseEntity<>(mypageResponse, HttpStatus.OK);
    }

    @GetMapping("/images") // 이미지 가져오기
    public void fileView(HttpServletResponse res, @RequestParam("no") Integer imageNo) {

        ProfileImg profileImg = myPageService.getProfileImageInfo(imageNo);
        try{
            res.setContentType("image/jpeg");
            byte[] imageFile = profileImg.getData();
            InputStream is = new ByteArrayInputStream(imageFile);
            IOUtils.copy(is, res.getOutputStream());
        } catch (Exception e){
            e.getMessage();
        }
    }

    @PutMapping("/passwords")
    public ResponseEntity<Object> updatePassword(HttpServletRequest request, @RequestBody String password){

        // 토큰 가져오기
        String token = userService.getToken(request.getCookies());

        // 토큰 검증
        TokenResponse objToken = userService.tokenCheck(token);

        if(!objToken.getMessage().equals("")){
            return new ResponseEntity<>(objToken, HttpStatus.SEE_OTHER);
        }

        boolean isUpdate = false;
        isUpdate = this.myPageService.updatePassword(objToken, password);

        if(!isUpdate){
            return new ResponseEntity<>("비밀번호 변경에 실패했습니다.",HttpStatus.OK);
        }

        return new ResponseEntity<>(isUpdate, HttpStatus.OK);

    }
}
