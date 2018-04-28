package ch.fhnw.edu.wodss.tippspielapi.tippspielApi.config.authentication;

import ch.fhnw.edu.wodss.tippspielapi.config.authentication.JwtAuthenticationProvider;
import ch.fhnw.edu.wodss.tippspielapi.config.authentication.JwtAuthenticationToken;
import ch.fhnw.edu.wodss.tippspielapi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.persistence.UserRepository;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@RunWith(JMockit.class)
public class JwtAuthenticationProviderTest {

  @Tested
  private JwtAuthenticationProvider jwtAuthenticationProvider;

  @Injectable
  private UserRepository userRepositoryMock;

  @Test
  public void testAuthenticationWithStandardUser() {
    User user = new User();
    user.setUsername("hirsch");
    user.generateNewAuthenticationToken();
    user.setAdmin(false);

    new Expectations() {{
      userRepositoryMock.findByToken("db25cf61-4597-4021-b4ee-0d2d9c45f");
      result = user;
    }};

    Authentication authentication = new JwtAuthenticationToken(
        "db25cf61-4597-4021-b4ee-0d2d9c45f");
    jwtAuthenticationProvider.authenticate(authentication);

    Assert.assertTrue(authentication.isAuthenticated());
    Assert.assertEquals("hirsch", authentication.getPrincipal());
    Assert.assertTrue(
        authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
    Assert.assertFalse(
        authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
  }

  @Test(expected = AuthenticationException.class)
  public void testAuthenticationWithStandardUserAndExpiredToken() {
    User user = new User();
    user.setUsername("hirsch");
    user.setAdmin(false);

    new Expectations() {{
      userRepositoryMock.findByToken("db25cf61-4597-4021-b4ee-0d2d9c45f");
      result = user;
    }};

    Authentication authentication = new JwtAuthenticationToken(
        "db25cf61-4597-4021-b4ee-0d2d9c45f");
    jwtAuthenticationProvider.authenticate(authentication);
  }

  @Test
  public void testAuthenticationWithAdminUser() {
    User user = new User();
    user.setUsername("david");
    user.generateNewAuthenticationToken();
    user.setAdmin(true);

    new Expectations() {{
      userRepositoryMock.findByToken("db25cf61-4597-4021-b4ee-0d2d9c45f");
      result = user;
    }};

    Authentication authentication = new JwtAuthenticationToken(
        "db25cf61-4597-4021-b4ee-0d2d9c45f");
    jwtAuthenticationProvider.authenticate(authentication);

    Assert.assertTrue(authentication.isAuthenticated());
    Assert.assertEquals("david", authentication.getPrincipal());
    Assert.assertTrue(
        authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
    Assert.assertTrue(
        authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
  }

  @Test(expected = AuthenticationException.class)
  public void testAuthenticationWithUnknownUser() {
    new Expectations() {{
      userRepositoryMock.findByToken("db25cf61-4597-4021-b4ee-0d2d9c45f");
      result = null;
    }};

    Authentication authentication = new JwtAuthenticationToken(
        "db25cf61-4597-4021-b4ee-0d2d9c45f");
    jwtAuthenticationProvider.authenticate(authentication);
  }
}
