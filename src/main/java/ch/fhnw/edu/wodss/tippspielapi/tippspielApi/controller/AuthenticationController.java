package ch.fhnw.edu.wodss.tippspielapi.tippspielApi.controller;

import ch.fhnw.edu.wodss.tippspielapi.tippspielApi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.tippspielApi.service.AuthenticationService;
import ch.fhnw.edu.wodss.tippspielapi.tippspielApi.service.EmailService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

  private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

  @Autowired
  private AuthenticationService authenticationService;

  @Autowired
  private EmailService emailService;

  @Secured({"ROLE_USER"})
  @CrossOrigin
  @PostMapping(path = "/login")
  public LoginResponse login() {
    User user = authenticationService.login();
    return new LoginResponse(user);
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
      User registeredUser = authenticationService.register(newUserDto);
      RegistrationResponse responseBody = new RegistrationResponse(registeredUser);
      if (registeredUser != null) {
        emailService.sendVerificationEmail(registeredUser, locale);
        return ResponseEntity.ok().body(responseBody);
      } else {
        return ResponseEntity.badRequest().build();
      }
    } catch (IllegalStateException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @Data
  private class LoginResponse {

    private String token;
    private String expiration;

    public LoginResponse(User user) {
      token = user.getToken();
      final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
      Date expirationDate = user.getExpiration();
      expiration = simpleDateFormat.format(expirationDate);
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