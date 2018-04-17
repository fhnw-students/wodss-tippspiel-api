package ch.fhnw.edu.wodss.tippspielapi.tippspielApi.service;

import ch.fhnw.edu.wodss.tippspielapi.tippspielApi.config.authentication.JwtAuthenticationToken;
import ch.fhnw.edu.wodss.tippspielapi.tippspielApi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.tippspielApi.persistence.UserRepository;
import java.util.Date;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@RunWith(JMockit.class)
public class AuthenticationServiceTest {

  @Tested
  AuthenticationService authenticationService;

  @Injectable
  SecurityContext securityContextMock;

  @Injectable
  UserRepository userRepository;

  @Injectable
  User user;

  @Test
  public void testLoginWithExpiredToken(@Mocked SecurityContextHolder anyInstance) {
    UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
        .username("david")
        .password("anyPassword")
        .authorities("ROLE_USER", "ROLE_ADMIN")
        .build();

    new Expectations() {{
      SecurityContextHolder.getContext();
      result = securityContextMock;

      securityContextMock.getAuthentication();
      result = new UsernamePasswordAuthenticationToken(userDetails, "1234");

      userRepository.findByUsername("david");
      result = user;

      user.hasTokenExpired();
      result = true;

      userRepository.save(user);
      result = user;
    }};

    authenticationService.login();

    new Verifications() {{
      securityContextMock.getAuthentication();
      times = 1;

      userRepository.findByUsername("david");
      times = 1;

      userRepository.save((User) any);
      times = 1;
    }};
  }

  @Test
  public void testLoginWithExistingToken(@Mocked SecurityContextHolder anyInstance) {
    UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
        .username("david")
        .password("anyPassword")
        .authorities("ROLE_USER", "ROLE_ADMIN")
        .build();

    new Expectations() {{
      SecurityContextHolder.getContext();
      result = securityContextMock;

      securityContextMock.getAuthentication();
      result = new UsernamePasswordAuthenticationToken(userDetails, "1234");

      userRepository.findByUsername("david");
      result = user;

      user.hasTokenExpired();
      result = false;

      user.getToken();
      result = "dba4d610-8113-4a40-81d4-e5d53d1f55d9";
    }};

    authenticationService.login();

    Assert.assertEquals("dba4d610-8113-4a40-81d4-e5d53d1f55d9", user.getToken());

    new Verifications() {{
      securityContextMock.getAuthentication();
      times = 1;

      userRepository.findByUsername("david");
      times = 1;
    }};
  }

  @Test
  public void testLogoutLoggedInUser(@Mocked SecurityContextHolder anyInstance) {

    new Expectations() {{
      SecurityContextHolder.getContext();
      result = securityContextMock;

      JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(
          "dba4d610-8113-4a40-81d4-e5d53d1f55d9");
      jwtAuthenticationToken.setPrincipal("hirsch");
      securityContextMock.getAuthentication();
      this.result = jwtAuthenticationToken;

      userRepository.findByUsername("hirsch");
      this.result = user;
    }};

    authenticationService.logout();

    new Verifications() {{
      securityContextMock.getAuthentication();
      times = 1;

      userRepository.findByUsername("hirsch");
      times = 1;

      user.clearToken();
      times = 1;

      userRepository.save(user);
      times = 1;
    }};
  }

}