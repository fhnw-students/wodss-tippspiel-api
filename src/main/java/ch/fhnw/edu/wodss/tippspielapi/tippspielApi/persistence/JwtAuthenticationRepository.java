package ch.fhnw.edu.wodss.tippspielapi.tippspielApi.persistence;

import ch.fhnw.edu.wodss.tippspielapi.tippspielApi.model.JwtAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtAuthenticationRepository extends JpaRepository<JwtAuthentication, Long> {

  JwtAuthentication findByToken(String token);

}
