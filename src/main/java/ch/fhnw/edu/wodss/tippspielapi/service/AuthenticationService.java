package ch.fhnw.edu.wodss.tippspielapi.service;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.NewUserDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.UserDto;
import ch.fhnw.edu.wodss.tippspielapi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.persistence.UserRepository;
import ch.fhnw.edu.wodss.tippspielapi.service.exception.IllegalPasswordException;
import java.util.List;
import java.util.Locale;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Transactional(rollbackOn = Exception.class)
public class AuthenticationService {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EmailService emailService;

  @Autowired
  private TokenHelper tokenHelper;

  public User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Object principal = authentication.getPrincipal();
    String username = principal.toString();
    User user = userRepository.findByUsername(username);
    return user;
  }

  public User login() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    User user = userRepository.findByUsername(username);
    String token = tokenHelper.generateToken(username);
    if (user.isNotVerified()) {
      throw new IllegalStateException("User [" + user.getUsername() + "] has not been verified.");
    } else {
      user.setToken(token);
    }
    return user;
  }

  public User register(NewUserDto newUserDto, Locale locale) {
    List<User> existingUsers = userRepository
        .findByEmailOrUsername(newUserDto.getEmail(), newUserDto.getUsername());
    if (existingUsers == null || existingUsers.isEmpty()) {
      User newUser = mapDtoToUser(newUserDto);
      newUser = userRepository.save(newUser);
      emailService.sendVerificationEmail(newUser, locale);
      return newUser;
    } else {
      return null;
    }
  }

  private User mapDtoToUser(NewUserDto newUserDto) {
    User newUser;
    newUser = new User();
    newUser.setUsername(newUserDto.getUsername());
    newUser.setPassword(ArgonPasswordEncoder.getInstance().encode(newUserDto.getPassword()));
    newUser.setEmail(newUserDto.getEmail());
    newUser.setAdmin(false);
    newUser.generateVerificationToken();
    return newUser;
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
      if (user.isNotResetting()) {
        user.generateResetToken();
        userRepository.save(user);
      }
      emailService.sendResetEmail(user, locale);
    } else {
      emailService.sendVerificationEmail(user, locale);
    }

  }

  /**
   * Set the given password as new password for the user representing the given reset token. The
   * to-be-set password will also be validated.
   */
  public void saveNewPassword(String resetToken, String password) {
    User user = userRepository.findByResetToken(resetToken);
    if (user != null) {
      validatePassword(password);
      user.setPassword(password);
      user.clearResetToken();
      userRepository.save(user);
    } else {
      String message = "unknown reset token provided: [" + resetToken + "]";
      LOGGER.error(message);
      throw new IllegalArgumentException(message);
    }
  }

  // Extend this method for more validation if desired.
  private void validatePassword(String password) throws IllegalPasswordException {
    if (password == null || password.length() < 4) {
      LOGGER.error("Length of new password was too small.");
      throw new IllegalPasswordException("The password must have a length of 4 characters!");
    }
  }
}
