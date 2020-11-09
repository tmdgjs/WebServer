package kr.hs.dgsw.dbook.dbook.Repository;


import kr.hs.dgsw.dbook.dbook.VO.Library;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LibraryRepository extends JpaRepository<Library, Long> {

    List<Library> findByUserId(String userid);

    Optional<Library> findByUserIdAndEbookId(String userid, Long ebookid);


}
