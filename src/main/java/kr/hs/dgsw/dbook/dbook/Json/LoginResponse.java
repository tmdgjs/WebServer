package kr.hs.dgsw.dbook.dbook.Json;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginResponse {

    String token;

    String userEmail;

    String message;

    public LoginResponse(String message) {
        this.message = message;
    }

    public LoginResponse(String userEmail, String token) {
        this.userEmail = userEmail;
        this.token = token;
    }
}
