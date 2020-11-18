package kr.hs.dgsw.dbook.dbook.Controller;

import com.sun.mail.iap.Response;
import kr.hs.dgsw.dbook.dbook.Json.LibraryResponse;
import kr.hs.dgsw.dbook.dbook.Json.TokenResponse;
import kr.hs.dgsw.dbook.dbook.Repository.LibraryRepository;
import kr.hs.dgsw.dbook.dbook.Service.LibraryService;
import kr.hs.dgsw.dbook.dbook.Service.UserService;
import kr.hs.dgsw.dbook.dbook.VO.EBook;
import kr.hs.dgsw.dbook.dbook.VO.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/librarys")
public class LibraryController {

    @Autowired
    private UserService userService;

    @Autowired
    private LibraryService libraryService;

    @GetMapping
    public ResponseEntity<Object> getMyLibrary(HttpServletRequest request){

        // 토큰 가져오기
        String token = userService.getToken(request.getCookies());

        // 토큰 검증
        TokenResponse objToken = userService.tokenCheck(token);

        if(!objToken.getMessage().equals("")){
            return new ResponseEntity<>(objToken, HttpStatus.SEE_OTHER);
        }

        LibraryResponse libraryResponse = libraryService.getMyLibrary(objToken);

        return new ResponseEntity<>(libraryResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addLibraryBook(@RequestBody EBook eBook, HttpServletRequest request){
        // 토큰 가져오기
        String token = userService.getToken(request.getCookies());

        // 토큰 검증
        TokenResponse objToken = userService.tokenCheck(token);

        if(!objToken.getMessage().equals("")){
            return new ResponseEntity<>(objToken, HttpStatus.SEE_OTHER);
        }


        LibraryResponse objLibraryResponse = libraryService.addLibraryBook(objToken, eBook.getEbookId());

        if(objLibraryResponse.getCode() != 0){
            return new ResponseEntity<>(objLibraryResponse, HttpStatus.FOUND);
        }
            return new ResponseEntity<>(objLibraryResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{ebookId}")
    public ResponseEntity<Object> deleteLibraryBook(@PathVariable Long ebookId, HttpServletRequest request){

        // 토큰 가져오기
        String token = userService.getToken(request.getCookies());

        // 토큰 검증
        TokenResponse objToken = userService.tokenCheck(token);

        if(!objToken.getMessage().equals("")){
            return new ResponseEntity<>(objToken, HttpStatus.SEE_OTHER);
        }

        libraryService.deleteLibraryBook(objToken, ebookId);
        objToken.setMessage("삭제되었습니다.");

        return new ResponseEntity<>(objToken, HttpStatus.OK);

    }



}
