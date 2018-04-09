package ch.fhnw.edu.wodss.tippspielapi.TippspielApi.service;

import ch.fhnw.edu.wodss.tippspielapi.TippspielApi.model.JwtAuthentication;
import ch.fhnw.edu.wodss.tippspielapi.TippspielApi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.TippspielApi.persistence.JwtAuthenticationRepository;
import ch.fhnw.edu.wodss.tippspielapi.TippspielApi.persistence.UserRepository;
import java.util.Calendar;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

  @Autowired
  private JwtAuthenticationRepository jwtAuthenticationRepository;

  @Autowired
  private UserRepository userRepository;

  public JwtAuthentication login() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    JwtAuthentication jwtAuthentication = jwtAuthenticationRepository.findByToken(username);

    if (jwtAuthentication != null) {
      if (isTokenExpired(jwtAuthentication)) {
        jwtAuthenticationRepository.delete(jwtAuthentication);
        jwtAuthentication = createJwtAuthentication(username);
      }
    } else {
      jwtAuthentication = createJwtAuthentication(username);
    }
    return jwtAuthentication;
  }

  private JwtAuthentication createJwtAuthentication(String username) {
    JwtAuthentication jwtAuthentication;
    User user = userRepository.findByUsername(username);
    jwtAuthentication = new JwtAuthentication(user);
    jwtAuthentication = jwtAuthenticationRepository.save(jwtAuthentication);
    return jwtAuthentication;
  }

  private boolean isTokenExpired(JwtAuthentication jwtAuthentication) {
    Date expiration = jwtAuthentication.getExpiration();
    Calendar now = Calendar.getInstance();
    return expiration.before(now.getTime());
  }

}
