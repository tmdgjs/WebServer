package kr.hs.dgsw.dbook.dbook.Service;

import kr.hs.dgsw.dbook.dbook.Json.EBookResponse;
import kr.hs.dgsw.dbook.dbook.Json.TokenResponse;
import kr.hs.dgsw.dbook.dbook.VO.EBook;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EBookService {

    EBookResponse fileUpload(List<MultipartFile> file, TokenResponse tokenResponse);

    EBookResponse fileInfoUpload(EBook eBook, TokenResponse objToken);

    EBookResponse updateSharedFlag(Long ebookId, TokenResponse objToken);

    List<EBook> getSharingBookList(TokenResponse objToken);

    EBookResponse getEBookInfo(Long ebookId, TokenResponse objToken);

    Object getSearchBookList(TokenResponse objToken, String search, int type);
}
