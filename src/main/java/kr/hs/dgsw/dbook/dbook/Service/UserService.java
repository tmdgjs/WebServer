package kr.hs.dgsw.dbook.dbook.Service;

import kr.hs.dgsw.dbook.dbook.Json.LoginResponse;
import kr.hs.dgsw.dbook.dbook.Json.SignUpResponse;
import kr.hs.dgsw.dbook.dbook.Json.TokenResponse;
import kr.hs.dgsw.dbook.dbook.VO.Token;
import kr.hs.dgsw.dbook.dbook.VO.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public interface UserService {

    SignUpResponse signup(User user);

    LoginResponse login(User user);

    TokenResponse tokenCheck(String token);

    String getToken(Cookie[] cookies);
}
