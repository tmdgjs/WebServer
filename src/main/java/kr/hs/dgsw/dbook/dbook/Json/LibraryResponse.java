package kr.hs.dgsw.dbook.dbook.Json;

import kr.hs.dgsw.dbook.dbook.VO.EBook;
import kr.hs.dgsw.dbook.dbook.VO.Library;
import lombok.Data;

import java.util.List;

@Data
public class LibraryResponse {

    Library library;

    String message;

    int code;

    String userId;

    List<EBook> libraryList;

    public LibraryResponse(){
        message = "";
        int code = 0;
    }

    public LibraryResponse(String message) {
        this.message = message;
    }

    public LibraryResponse(Library library) {
        this.library = library;
    }

    public LibraryResponse(List<EBook> libraryList, String userId){ this.libraryList = libraryList; this.userId = userId;}
}
