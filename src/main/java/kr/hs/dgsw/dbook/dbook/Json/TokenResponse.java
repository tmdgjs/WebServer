package kr.hs.dgsw.dbook.dbook.Json;

import lombok.Data;

@Data
public class TokenResponse {

    String tokenOwnerId;

    String token;

    String connectIP;

    String message;

    public TokenResponse(String tokenOwnerId, String token) {
        this.tokenOwnerId = tokenOwnerId;
        this.token = token;
        this.message = "";
    }

    public TokenResponse(String message) {
        this.message = message;
    }

}
