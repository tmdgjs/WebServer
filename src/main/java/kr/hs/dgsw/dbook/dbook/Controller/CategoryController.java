package kr.hs.dgsw.dbook.dbook.Controller;

import kr.hs.dgsw.dbook.dbook.Json.CategoryResponse;
import kr.hs.dgsw.dbook.dbook.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categorys")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/{categoryid}")
    public ResponseEntity<String> getCategory(@PathVariable Long categoryid){
        String categoryName = categoryService.getCategory(categoryid);
        return new ResponseEntity<>(categoryName, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<CategoryResponse> getCategoryList(){
        CategoryResponse objCategoryResponse = categoryService.getCategoryList();
        return new ResponseEntity<>(objCategoryResponse, HttpStatus.OK);
    }
}
