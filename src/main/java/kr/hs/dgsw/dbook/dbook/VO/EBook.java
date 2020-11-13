package kr.hs.dgsw.dbook.dbook.VO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class EBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long ebookId;

    String ebookTitle;

    String ebookSubTitle;

    Long categoryId;

    String ebookAuthor;

    String ebookUploader;

    @CreationTimestamp
    LocalDateTime ebookCreated;

    Boolean isShared;

    String ebookFilePath;

    String ebookPosterPath;

    String ebookPublisher;

    LocalDateTime ebookPublisherDate;

    //String ebookPreview;

}
