package kr.hs.dgsw.dbook.dbook.Json;

import kr.hs.dgsw.dbook.dbook.VO.EBook;
import lombok.Data;

@Data
public class EBookResponse {

    Integer code;

    String message;

    EBook eBook;

    String category;

    public EBookResponse(){
        this.code = 0;
        this.message = "";
    }

    public EBookResponse(EBook eBook) {
        this.eBook = eBook;
    }

    public EBookResponse(String message) {
        this.message = message;
        this.code = 0;
    }
}
