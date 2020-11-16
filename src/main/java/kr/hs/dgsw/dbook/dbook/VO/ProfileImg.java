package kr.hs.dgsw.dbook.dbook.VO;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "profileImg")
public class ProfileImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer imgNo;

    String fileName;

    String fileType;

    @Lob
    private byte[] data;

    public ProfileImg(String fileName, String fileType, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }

}