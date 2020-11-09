package kr.hs.dgsw.dbook.dbook.Repository;

import kr.hs.dgsw.dbook.dbook.VO.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
