package ch.fhnw.edu.wodss.tippspielapi.controller;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.NewUserDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.PasswordResetDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.ResetDto;
import ch.fhnw.edu.wodss.tippspielapi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.service.AuthenticationService;
import ch.fhnw.edu.wodss.tippspielapi.service.EmailService;
import ch.fhnw.edu.wodss.tippspielapi.service.exception.IllegalPasswordException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

  private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

  @Autowired
  private AuthenticationService authenticationService;

  @Autowired
  private EmailService emailService;

  @Secured({"ROLE_USER"})
  @CrossOrigin
  @PostMapping(path = "/login")
  public ResponseEntity login() {
    try {
      User user = authenticationService.login();
      return ResponseEntity.ok(new LoginResponse(user));
    } catch (IllegalStateException e) {
      LOGGER.error("An error occurred while loggin in: " + e.getMessage(), e);
      return ResponseEntity.badRequest().build();
    }
  }

  @Secured({"ROLE_USER"})
  @CrossOrigin
  @PostMapping(path = "/logout")
  public ResponseEntity logout() {
    authenticationService.logout();
    return ResponseEntity.ok().build();
  }

  @CrossOrigin
  @PostMapping(path = "/register")
  public ResponseEntity<RegistrationResponse> register(Locale locale,
      @RequestBody NewUserDto newUserDto) {
    try {
      User registeredUser = authenticationService.register(newUserDto, locale);
      if (registeredUser != null) {
        RegistrationResponse responseBody = new RegistrationResponse(registeredUser);
        return ResponseEntity.ok().body(responseBody);
      } else {
        return ResponseEntity.badRequest().build();
      }
    } catch (Exception e) {
      LOGGER.error("An error occurred while registering: " + e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @CrossOrigin
  @PutMapping(path = "/verify/{verifyToken}")
  public ResponseEntity verify(@PathVariable("verifyToken") String token) {
    try {
      authenticationService.verify(token);
      return ResponseEntity.ok().build();
    } catch (IllegalArgumentException e) {
      LOGGER.error("An error occurred while verifying a new user: " + e.getMessage(), e);
      return ResponseEntity.badRequest().build();
    }
  }

  @CrossOrigin
  @PutMapping(path = "/reset")
  public ResponseEntity reset(Locale locale, @RequestBody ResetDto resetDto) {
    try {
      authenticationService.reset(resetDto.getEmail(), locale);
    } catch (Exception e) {
      // Security: never return anything different than a 200 for a reset request
      LOGGER.error("An error occurred while resetting a user's password: " + e.getMessage(), e);
    }
    return ResponseEntity.ok().build();
  }

  @CrossOrigin
  @PutMapping(path = "/reset/{resetToken}")
  public ResponseEntity newPassword(@PathVariable String resetToken,
      @RequestBody PasswordResetDto passwordResetDto) {
    try {
      authenticationService.saveNewPassword(resetToken, passwordResetDto.getPassword());
    } catch (IllegalPasswordException | IllegalArgumentException e) {
      LOGGER.error("An error occurred while saving the new password: " + e.getMessage(), e);
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok().build();
  }

  @Data
  private class LoginResponse {

    private String token;

    public LoginResponse(User user) {
      token = user.getToken();
    }
  }

  @Data
  private class RegistrationResponse {

    private Long id;
    private String username;
    private String email;

    public RegistrationResponse(User user) {
      id = user.getId();
      username = user.getUsername();
      email = user.getEmail();
    }
  }

}