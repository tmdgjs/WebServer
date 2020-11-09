package kr.hs.dgsw.dbook.dbook.Json;

import kr.hs.dgsw.dbook.dbook.VO.Category;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class CategoryResponse {

    String message;

    int code;

    List<Category> categoryList;

    public CategoryResponse(String message) {
        this.message = message;
    }

    public CategoryResponse() {
        this.message = "";
        this.code = 0;
    }

    public CategoryResponse(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
}
