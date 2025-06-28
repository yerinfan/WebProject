// 5. Repositories
package kr.ac.kopo.repository;

import kr.ac.kopo.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}