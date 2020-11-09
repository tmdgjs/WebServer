package kr.hs.dgsw.dbook.dbook.Repository;

import kr.hs.dgsw.dbook.dbook.VO.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByTokenOwnerId(String userid);

    Optional<Token> findByToken(String token);
}
