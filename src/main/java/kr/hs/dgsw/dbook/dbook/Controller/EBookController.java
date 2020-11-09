package kr.hs.dgsw.dbook.dbook.Controller;

import com.sun.mail.iap.Response;
import kr.hs.dgsw.dbook.dbook.Json.EBookResponse;
import kr.hs.dgsw.dbook.dbook.Json.TokenResponse;
import kr.hs.dgsw.dbook.dbook.Service.EBookService;
import kr.hs.dgsw.dbook.dbook.Service.UserService;
import kr.hs.dgsw.dbook.dbook.VO.EBook;
import kr.hs.dgsw.dbook.dbook.VO.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/ebooks")
public class EBookController {

    @Autowired
    private EBookService eBookService;

    @Autowired
    private UserService userService;

    @PostMapping("/uploads/files")
    public ResponseEntity<Object> fileUpload(@RequestPart List<MultipartFile> files, HttpServletRequest request) {

        // 토큰 가져오기
        String token = userService.getToken(request.getCookies());

        // 토큰 검증
        TokenResponse objToken = userService.tokenCheck(token);

        if(!objToken.getMessage().equals("")){
            return new ResponseEntity<>(objToken, HttpStatus.SEE_OTHER);
        }

        EBookResponse eBookResponse = eBookService.fileUpload(files, objToken);

        return new ResponseEntity<>(eBookResponse, HttpStatus.OK);
    }

    @PostMapping("/uploads/infos")
    public ResponseEntity<Object> fileInfoUpload(@RequestBody EBook eBook, HttpServletRequest request){

        // 토큰 가져오기
        String token = userService.getToken(request.getCookies());

        // 토큰 검증
        TokenResponse objToken = userService.tokenCheck(token);

        if(!objToken.getMessage().equals("")){
            return new ResponseEntity<>(objToken, HttpStatus.SEE_OTHER);
        }

        EBookResponse eBookResponse = eBookService.fileInfoUpload(eBook, objToken);
        if(eBookResponse.getEBook() == null){
            return new ResponseEntity<>(eBookResponse, HttpStatus.SEE_OTHER);
        }
            return new ResponseEntity<>(eBookResponse, HttpStatus.OK);
    }

    @PutMapping("/shares/{ebookId}")
    public ResponseEntity<Object> updateSharedFlag(@PathVariable Long ebookId, HttpServletRequest request){
        // 토큰 가져오기
        String token = userService.getToken(request.getCookies());

        // 토큰 검증
        TokenResponse objToken = userService.tokenCheck(token);

        if(!objToken.getMessage().equals("")){
            return new ResponseEntity<>(objToken, HttpStatus.SEE_OTHER);
        }

        EBookResponse eBookResponse = eBookService.updateSharedFlag(ebookId, objToken);

        if(!eBookResponse.getMessage().equals("")){
            return new ResponseEntity<>(eBookResponse, HttpStatus.NO_CONTENT);
        }

        eBookResponse.setMessage("공유 정보가 변경되었습니다.");
        return new ResponseEntity<>(eBookResponse, HttpStatus.OK);
    }

    @GetMapping("/shares")
    public ResponseEntity<Object> getSharingBookList(HttpServletRequest request){
        // 토큰 가져오기
        String token = userService.getToken(request.getCookies());

        // 토큰 검증
        TokenResponse objToken = userService.tokenCheck(token);

        if(!objToken.getMessage().equals("")){
            return new ResponseEntity<>(objToken, HttpStatus.SEE_OTHER);
        }

        List<EBook> eBookList = eBookService.getSharingBookList(objToken);

        return new ResponseEntity<>(eBookList,HttpStatus.OK);

    }

    @GetMapping("/{ebookId}")
    public ResponseEntity<Object> getEbookInfo(@PathVariable Long ebookId, HttpServletRequest request){
        // 토큰 가져오기
        String token = userService.getToken(request.getCookies());

        // 토큰 검증
        TokenResponse objToken = userService.tokenCheck(token);

        if(!objToken.getMessage().equals("")){
            return new ResponseEntity<>(objToken, HttpStatus.SEE_OTHER);
        }

        EBookResponse eBookRes = eBookService.getEBookInfo(ebookId, objToken);
        if(!eBookRes.getMessage().equals(""))
            return new ResponseEntity<>(eBookRes, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(eBookRes, HttpStatus.OK);
    }

    @GetMapping("/searchs")
    public ResponseEntity<Object> searchBookList(@RequestParam("name") String search,@RequestParam("type") int type ,HttpServletRequest request){
        // 토큰 가져오기
        String token = userService.getToken(request.getCookies());

        // 토큰 검증
        TokenResponse objToken = userService.tokenCheck(token);

        if(!objToken.getMessage().equals("")){
            return new ResponseEntity<>(objToken, HttpStatus.SEE_OTHER);
        }

        Object objSearchList = eBookService.getSearchBookList(objToken, search, type);
        return new ResponseEntity<>(objSearchList,HttpStatus.OK);
    }
}
