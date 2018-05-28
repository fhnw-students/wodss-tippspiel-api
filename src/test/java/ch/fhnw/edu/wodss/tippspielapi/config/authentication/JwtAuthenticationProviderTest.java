package ch.fhnw.edu.wodss.tippspielapi.config.authentication;

import ch.fhnw.edu.wodss.tippspielapi.config.authentication.JwtAuthenticationProvider;
import ch.fhnw.edu.wodss.tippspielapi.config.authentication.JwtAuthenticationToken;
import ch.fhnw.edu.wodss.tippspielapi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.persistence.UserRepository;
import ch.fhnw.edu.wodss.tippspielapi.service.TokenHelper;
import io.jsonwebtoken.Claims;
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
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

  @Injectable
  private TokenHelper tokenHelper;

  @Test
  public void testAuthenticationWithStandardUser(@Mocked Claims claims) {
    User user = new User();
    user.setUsername("hirsch");
    user.setAdmin(false);

    Calendar instance = Calendar.getInstance();
    instance.add(Calendar.HOUR, 1);
    Date inAnHour = instance.getTime();

    new Expectations() {{
      userRepositoryMock.findByUsername("hirsch");
      result = user;

      tokenHelper.getUsernameFromToken("validToken");
      result = "hirsch";

      tokenHelper.getClaimsFromToken("validToken");
      result = claims;

      claims.getExpiration();
      this.result = inAnHour;
    }};

    Authentication authentication = new JwtAuthenticationToken("validToken");
    jwtAuthenticationProvider.authenticate(authentication);

    Assert.assertTrue(authentication.isAuthenticated());
    Assert.assertEquals("hirsch", authentication.getPrincipal());
    Assert.assertTrue(
        authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
    Assert.assertFalse(
        authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
  }

  @Test(expected = AuthenticationException.class)
  public void testAuthenticationWithStandardUserAndExpiredToken(@Mocked Claims claims) {
    User user = new User();
    user.setUsername("hirsch");
    user.setAdmin(false);

    Calendar instance = Calendar.getInstance();
    instance.add(Calendar.HOUR, -1);
    Date anHourAgo = instance.getTime();

    new Expectations() {{
      userRepositoryMock.findByUsername("hirsch");
      result = user;

      tokenHelper.getUsernameFromToken("expiredToken");
      result = "hirsch";

      tokenHelper.getClaimsFromToken("expiredToken");
      result = claims;

      claims.getExpiration();
      result = anHourAgo;
    }};

    Authentication authentication = new JwtAuthenticationToken("expiredToken");
    jwtAuthenticationProvider.authenticate(authentication);
  }

  @Test
  public void testAuthenticationWithAdminUser(@Mocked Claims claims) {
    User user = new User();
    user.setUsername("david");
    user.setAdmin(true);

    Calendar instance = Calendar.getInstance();
    instance.add(Calendar.HOUR, 1);
    Date inAnHour = instance.getTime();

    new Expectations() {{
      userRepositoryMock.findByUsername("david");
      result = user;

      tokenHelper.getUsernameFromToken("validToken");
      result = "david";

      tokenHelper.getClaimsFromToken("validToken");
      result = claims;

      claims.getExpiration();
      this.result = inAnHour;
    }};

    Authentication authentication = new JwtAuthenticationToken("validToken");
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
      tokenHelper.getUsernameFromToken("unknownUserToken");
      result = null;

      userRepositoryMock.findByUsername(null);
      result = null;

      tokenHelper.getClaimsFromToken("unknownUserToken");
      result = null;
    }};

    Authentication authentication = new JwtAuthenticationToken("unknownUserToken");
    jwtAuthenticationProvider.authenticate(authentication);
  }
}
