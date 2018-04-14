package ch.fhnw.edu.wodss.tippspielapi.tippspielApi.service;

import ch.fhnw.edu.wodss.tippspielapi.tippspielApi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.tippspielApi.persistence.UserRepository;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@RunWith(JMockit.class)
public class LoginServiceTest {

  @Tested
  LoginService loginService;

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

    loginService.login();

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

    loginService.login();

    Assert.assertEquals("dba4d610-8113-4a40-81d4-e5d53d1f55d9", user.getToken());

    new Verifications() {{
      securityContextMock.getAuthentication();
      times = 1;

      userRepository.findByUsername("david");
      times = 1;
    }};
  }

}