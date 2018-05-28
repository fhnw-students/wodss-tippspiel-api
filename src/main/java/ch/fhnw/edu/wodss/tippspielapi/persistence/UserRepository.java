package ch.fhnw.edu.wodss.tippspielapi.persistence;

import ch.fhnw.edu.wodss.tippspielapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User, Long> {

  User findByUsername(String username);

  User findByEmail(String email);

  User findByVerificationToken(String verificationToken);

  User findByResetToken(String resetToken);

  void deleteByEmail(String email);
}
