package kr.hs.dgsw.dbook.dbook.Service;

import kr.hs.dgsw.dbook.dbook.Json.CategoryResponse;
import kr.hs.dgsw.dbook.dbook.Repository.CategoryRepository;
import kr.hs.dgsw.dbook.dbook.VO.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public String getCategory(Long categoryid) {

        Category objCategory = categoryRepository.findById(categoryid).orElseGet(() ->new Category("없음"));

        return objCategory.getCategoryName();
    }

    @Override
    public CategoryResponse getCategoryList() {

        CategoryResponse objCategoryResponse = new CategoryResponse();

        List<Category> listCategory = categoryRepository.findAll();

        return new CategoryResponse(listCategory);

    }
}
