package kr.hs.dgsw.dbook.dbook.Json;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class SignUpResponse {

    String message;

    Integer code;

    Long userNo;

    public SignUpResponse(){
        this.code = 0;
    }

    public SignUpResponse(String message) {
        this.message = message;
        this.code = 0;
    }

    public SignUpResponse(Long userNo) {
        this.userNo = userNo;
        this.code = 0;
    }


}
