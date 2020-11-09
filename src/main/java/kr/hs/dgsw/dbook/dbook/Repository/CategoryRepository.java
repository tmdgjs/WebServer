package kr.hs.dgsw.dbook.dbook.Repository;

import kr.hs.dgsw.dbook.dbook.VO.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
