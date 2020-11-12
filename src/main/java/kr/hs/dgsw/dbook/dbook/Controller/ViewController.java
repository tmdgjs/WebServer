package kr.hs.dgsw.dbook.dbook.Controller;

import kr.hs.dgsw.dbook.dbook.Json.TokenResponse;
import kr.hs.dgsw.dbook.dbook.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class ViewController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ModelAndView mainPage(HttpServletResponse response, HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();

        // 토큰 가져오기
        String token = userService.getToken(request.getCookies());

        // 토큰 검증
        TokenResponse objToken = userService.tokenCheck(token);


        /*if(!objToken.getMessage().equals("")){
            modelAndView.setViewName("redirect:" + "/login");
            return modelAndView;
        }
*/

        modelAndView.setViewName("/main.html");
        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView loginPage(HttpServletResponse response,  HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();

        // 토큰 가져오기
        String token = userService.getToken(request.getCookies());

        // 토큰 검증
        TokenResponse objToken = userService.tokenCheck(token);


        /*if(!objToken.getMessage().equals("")){
            modelAndView.setViewName("redirect:" + "/login");
            return modelAndView;
        }
*/

        modelAndView.setViewName("/login.html");
        return modelAndView;
    }

    @GetMapping("/signup")
    public ModelAndView signupPage(HttpServletResponse response){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/signup.html");
        return modelAndView;
    }

    @GetMapping("/upload")
    public ModelAndView uploadPage(HttpServletResponse response,  HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();

        // 토큰 가져오기
        String token = userService.getToken(request.getCookies());

        // 토큰 검증
        TokenResponse objToken = userService.tokenCheck(token);


        /*if(!objToken.getMessage().equals("")){
            modelAndView.setViewName("redirect:" + "/login");
            return modelAndView;
        }
*/

        modelAndView.setViewName("/upload.html");
        return modelAndView;
    }

    @GetMapping("/mypage")
    public ModelAndView settingPage(HttpServletResponse response,  HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();

        // 토큰 가져오기
        String token = userService.getToken(request.getCookies());

        // 토큰 검증
        TokenResponse objToken = userService.tokenCheck(token);

        /*if(!objToken.getMessage().equals("")){
            modelAndView.setViewName("redirect:" + "/login");
            return modelAndView;
        }
*/
        modelAndView.setViewName("/mypage.html");
        return modelAndView;
    }

}
