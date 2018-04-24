package ch.fhnw.edu.wodss.tippspielapi.persistence;

import ch.fhnw.edu.wodss.tippspielapi.model.JwtAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtAuthenticationRepository extends JpaRepository<JwtAuthentication, Long> {

  JwtAuthentication findByToken(String token);

}
