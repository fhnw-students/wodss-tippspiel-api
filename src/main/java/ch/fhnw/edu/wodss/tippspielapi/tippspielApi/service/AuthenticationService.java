package ch.fhnw.edu.wodss.tippspielapi.tippspielApi.service;

import ch.fhnw.edu.wodss.tippspielapi.tippspielApi.controller.NewUserDto;
import ch.fhnw.edu.wodss.tippspielapi.tippspielApi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.tippspielApi.persistence.JwtAuthenticationRepository;
import ch.fhnw.edu.wodss.tippspielapi.tippspielApi.persistence.UserRepository;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

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

  public void logout() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Object principal = authentication.getPrincipal();
    String username = principal.toString();
    User user = userRepository.findByUsername(username);
    user.clearToken();
    userRepository.save(user);
  }

  public User register(NewUserDto newUserDto) {
    User user = userRepository.findByEmail(newUserDto.getEmail());
    if (user == null) {
      User newUser = new User();
      newUser.setUsername(newUserDto.getUsername());
      newUser.setPassword(ArgonPasswordEncoder.getInstance().encode(newUserDto.getPassword()));
      newUser.setEmail(newUserDto.getEmail());
      newUser.setAdmin(false);
      newUser.generateVerificationToken();
      newUser = userRepository.save(newUser);
      return newUser;
    } else {
      return null;
    }
  }

}
