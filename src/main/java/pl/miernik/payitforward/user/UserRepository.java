package pl.miernik.payitforward.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findFirstByEmailIgnoringCase(String email);

    User findByEmailIgnoreCase(String email);
}