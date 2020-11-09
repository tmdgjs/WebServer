package kr.hs.dgsw.dbook.dbook.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/mains")
public class MainController {

    @GetMapping("/logouts")
    public ModelAndView logout(HttpServletResponse response){
        ModelAndView modelAndView = new ModelAndView();

        Cookie myCookie =  new Cookie("Token", null);
        myCookie.setMaxAge(0);
        myCookie.setPath("/");
        response.addCookie(myCookie);

        return new ModelAndView("redirect:" + "/login");
    }
}
