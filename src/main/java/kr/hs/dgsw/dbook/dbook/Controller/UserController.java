package kr.hs.dgsw.dbook.dbook.Controller;


import kr.hs.dgsw.dbook.dbook.Json.LoginResponse;
import kr.hs.dgsw.dbook.dbook.Json.SignUpResponse;
import kr.hs.dgsw.dbook.dbook.Service.UserService;
import kr.hs.dgsw.dbook.dbook.VO.User;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signups")
    public ResponseEntity<SignUpResponse> Signup(@RequestBody User user){

        SignUpResponse signUpResponse = userService.signup(user);

        return new ResponseEntity<>(signUpResponse, HttpStatus.OK);

    }

    @PostMapping("/logins")
    public ResponseEntity<LoginResponse> Login(@RequestBody User user, HttpServletResponse response){
        LoginResponse loginResponse = userService.login(user);

        Cookie myCookie =  new Cookie("Token", loginResponse.getToken());
        myCookie.setPath("/");
        response.addCookie(myCookie);

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

}
