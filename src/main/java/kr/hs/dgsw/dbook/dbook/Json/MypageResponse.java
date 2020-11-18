package kr.hs.dgsw.dbook.dbook.Json;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MypageResponse {

    public MypageResponse(String email, String name, Integer profileImgNo, Integer libraryBooks, Integer uploadBooks) {
        this.email = email;
        this.name = name;
        this.profileImgNo = profileImgNo;
        this.libraryBooks = libraryBooks;
        this.uploadBooks = uploadBooks;
    }

    String email;

    String name;

    Integer profileImgNo;

    Integer libraryBooks;

    Integer uploadBooks;


}
