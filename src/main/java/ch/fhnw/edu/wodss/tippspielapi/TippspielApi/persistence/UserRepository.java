package ch.fhnw.edu.wodss.tippspielapi.TippspielApi.persistence;

import ch.fhnw.edu.wodss.tippspielapi.TippspielApi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User, Long> {

  User findByUsername(String username);

  User findByToken(String token);
}
