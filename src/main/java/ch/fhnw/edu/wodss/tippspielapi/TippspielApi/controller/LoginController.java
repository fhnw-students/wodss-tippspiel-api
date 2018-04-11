package ch.fhnw.edu.wodss.tippspielapi.TippspielApi.controller;

import ch.fhnw.edu.wodss.tippspielapi.TippspielApi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.TippspielApi.service.LoginService;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/login")
public class LoginController {
  private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

  @Autowired
  private LoginService loginService;

  @Secured({"ROLE_USER"})
  @CrossOrigin
  @PostMapping
  public LoginResponse login() {
    User user = loginService.login();
    return new LoginResponse(user);
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
}