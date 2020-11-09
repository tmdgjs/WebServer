package kr.hs.dgsw.dbook.dbook.Repository;

import kr.hs.dgsw.dbook.dbook.VO.EBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EBookRepository extends JpaRepository<EBook,Long> {

    //Optional<EBook> findByEB
    List<EBook> findByEbookUploader(String ebookUploader);

    List<EBook> findByIsSharedAndEbookUploaderNot(Boolean isShared, String ebookUploader);

    List<EBook> findByEbookUploaderAndEbookTitleLike(String eboookUploader, String search);

    List<EBook> findByIsSharedAndEbookUploaderNotAndEbookTitleLike(Boolean isShared, String ebookUploader, String search);

}
