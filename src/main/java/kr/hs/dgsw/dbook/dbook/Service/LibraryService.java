package kr.hs.dgsw.dbook.dbook.Service;

import kr.hs.dgsw.dbook.dbook.Json.LibraryResponse;
import kr.hs.dgsw.dbook.dbook.Json.TokenResponse;
import kr.hs.dgsw.dbook.dbook.VO.EBook;
import kr.hs.dgsw.dbook.dbook.VO.Library;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LibraryService {

    LibraryResponse getMyLibrary(TokenResponse objToken);

    ResponseEntity<Object> deleteLibraryBook(TokenResponse objToken, Long ebookid);

    LibraryResponse addLibraryBook(TokenResponse objToken, Long ebookid);
}
