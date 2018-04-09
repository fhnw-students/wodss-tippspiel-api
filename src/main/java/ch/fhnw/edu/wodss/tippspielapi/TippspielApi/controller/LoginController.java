package ch.fhnw.edu.wodss.tippspielapi.TippspielApi.controller;

import ch.fhnw.edu.wodss.tippspielapi.TippspielApi.model.JwtAuthentication;
import ch.fhnw.edu.wodss.tippspielapi.TippspielApi.service.LoginService;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
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

  @Autowired
  private LoginService loginService;

  @Secured({"ROLE_USER"})
  @CrossOrigin
  @PostMapping
  public LoginResponse login() {
    JwtAuthentication jwtAuthentication = loginService.login();
    return new LoginResponse(jwtAuthentication);
  }

  @Data
  private class LoginResponse {
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private String token;
    private String formattedExpiration;

    public LoginResponse(JwtAuthentication authentication) {
      token = authentication.getToken();
      final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
      formattedExpiration = simpleDateFormat.format(authentication.getExpiration());
    }
  }
}