package kr.hs.dgsw.dbook.dbook.VO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long tokenNo;

    @Column(nullable = false, unique = true)
    String tokenOwnerId;

    @Column(nullable = false)
    String token;

    @Column(nullable = false)
    String connectIP;

    @CreationTimestamp
    LocalDateTime tokenCreated;

    @UpdateTimestamp
    LocalDateTime tokenUpdated;

    public Token(String tokenOwnerId, String token, String connectIP) {
        this.tokenOwnerId = tokenOwnerId;
        this.token = token;
        this.connectIP = connectIP;
    }


}
