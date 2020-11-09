package kr.hs.dgsw.dbook.dbook.Service;

import kr.hs.dgsw.dbook.dbook.Json.CategoryResponse;

public interface CategoryService {

    String getCategory(Long categoryid);

    CategoryResponse getCategoryList();
}
