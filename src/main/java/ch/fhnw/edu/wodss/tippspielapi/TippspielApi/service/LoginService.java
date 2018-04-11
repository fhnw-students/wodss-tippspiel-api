package ch.fhnw.edu.wodss.tippspielapi.TippspielApi.service;

import ch.fhnw.edu.wodss.tippspielapi.TippspielApi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.TippspielApi.persistence.JwtAuthenticationRepository;
import ch.fhnw.edu.wodss.tippspielapi.TippspielApi.persistence.UserRepository;
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

  public User login() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();

    User user = userRepository.findByUsername(username);
    if (user.hasTokenExpired()) {
      user.generateNewToken();
      user = userRepository.save(user);
    }
    return user;
  }

}
