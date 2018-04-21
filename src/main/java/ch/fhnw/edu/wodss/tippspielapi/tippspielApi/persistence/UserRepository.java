package ch.fhnw.edu.wodss.tippspielapi.tippspielApi.persistence;

import ch.fhnw.edu.wodss.tippspielapi.tippspielApi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User, Long> {

  User findByUsername(String username);

  User findByToken(String token);

  User findByEmail(String email);

  User findByVerificationToken(String verificationToken);

  User findByResetToken(String resetToken);
}
