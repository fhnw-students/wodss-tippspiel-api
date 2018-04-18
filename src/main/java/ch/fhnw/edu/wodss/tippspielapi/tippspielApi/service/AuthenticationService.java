package ch.fhnw.edu.wodss.tippspielapi.tippspielApi.service;

import ch.fhnw.edu.wodss.tippspielapi.tippspielApi.controller.dto.NewUserDto;
import ch.fhnw.edu.wodss.tippspielapi.tippspielApi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.tippspielApi.persistence.UserRepository;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EmailService emailService;

  public User login() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    User user = userRepository.findByUsername(username);
    if (user.isNotVerified()) {
      throw new IllegalStateException("User [" + user.getUsername() + "] has not been verified.");
    } else if (user.hasAuthenticationTokenExpired()) {
      user.generateNewAuthenticationToken();
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

  public void verify(String token) {
    User user = userRepository.findByVerificationToken(token);
    if (user != null) {
      user.clearVerificationToken();
      userRepository.save(user);
    } else {
      throw new IllegalArgumentException(
          "An unknown verification token [" + token + "] was provided.");
    }
  }

  /**
   * Resets the user of the given email address and sends an email in the given language. In case
   * the user was not verified as user yet, send a verification instead.
   */
  public void reset(String email, Locale locale) {
    User user = userRepository.findByEmail(email);
    if (user == null) {
      throw new IllegalArgumentException("unknown email provided: [" + email + "].");
    } else if (user.isVerified()) {
      if (user.isResetting()) {
        user.generateResetToken();
        userRepository.save(user);
      }
      emailService.sendResetEmail(user, locale);
    } else {
      emailService.sendVerificationEmail(user, locale);
    }

  }
}
