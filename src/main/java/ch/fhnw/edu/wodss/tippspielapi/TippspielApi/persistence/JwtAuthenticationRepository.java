package ch.fhnw.edu.wodss.tippspielapi.TippspielApi.persistence;

import ch.fhnw.edu.wodss.tippspielapi.TippspielApi.model.JwtAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtAuthenticationRepository extends JpaRepository<JwtAuthentication, Long> {

  JwtAuthentication findByToken(String token);

}
