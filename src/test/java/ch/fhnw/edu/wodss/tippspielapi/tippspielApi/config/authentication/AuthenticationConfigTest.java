package ch.fhnw.edu.wodss.tippspielapi.tippspielApi.config.authentication;

import ch.fhnw.edu.wodss.tippspielapi.tippspielApi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.tippspielApi.persistence.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RunWith(JMockit.class)
public class AuthenticationConfigTest {

  private static final Collection<GrantedAuthority> userRoleAuthorities = new ArrayList<>(
      Arrays.asList(new SimpleGrantedAuthority[]{
          new SimpleGrantedAuthority("ROLE_USER")}));
  private static final Collection<GrantedAuthority> adminRoleAuthorities = new ArrayList<>(
      Arrays.asList(new SimpleGrantedAuthority[]{
          new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("ROLE_ADMIN")}));


  @Tested
  private AuthenticationConfig authenticationConfig;

  @Injectable
  private UserRepository userRepositoryMock;

  @Injectable
  private JwtAuthenticationProvider jwtAuthenticationProvider;

  @Injectable
  private ObjectPostProcessor<Object> objectPostProcessorMock;

  @Test
  public void testUserDetailsServiceWithStandardUser() throws Exception {
    User user = new User();
    final String username = "hirsch";
    user.setUsername(username);
    user.setIsAdmin(false);
    user.setPassword("aNotSoSecurePassword");

    new Expectations() {{
      userRepositoryMock.findByUsername(username);
      result = user;
    }};

    AuthenticationManagerBuilder authenticationManagerBuilder = new AuthenticationManagerBuilder(
        objectPostProcessorMock);
    authenticationConfig.init(authenticationManagerBuilder);
    UserDetailsService userDetailsService = authenticationManagerBuilder
        .getDefaultUserDetailsService();
    UserDetails hirsch = userDetailsService.loadUserByUsername(username);
    Assert.assertEquals(username, hirsch.getUsername());
    Assert.assertEquals("aNotSoSecurePassword", hirsch.getPassword());
    hirsch.getAuthorities()
        .forEach(authority -> Assert.assertTrue(userRoleAuthorities.contains(authority)));
    userRoleAuthorities
        .forEach(authority -> Assert.assertTrue(hirsch.getAuthorities().contains(authority)));

    new Verifications() {{
      userRepositoryMock.findByUsername(username);
      times = 1;
    }};
  }

  @Test
  public void testUserDetailsServiceWithAdminUser() throws Exception {
    User user = new User();
    final String username = "david";
    user.setUsername(username);
    user.setIsAdmin(true);
    user.setPassword("4$3cur3P4$$w0rd");

    new Expectations() {{
      userRepositoryMock.findByUsername(username);
      result = user;
    }};

    AuthenticationManagerBuilder authenticationManagerBuilder = new AuthenticationManagerBuilder(
        objectPostProcessorMock);
    authenticationConfig.init(authenticationManagerBuilder);
    UserDetailsService userDetailsService = authenticationManagerBuilder
        .getDefaultUserDetailsService();
    UserDetails admin = userDetailsService.loadUserByUsername(username);
    Assert.assertEquals(username, admin.getUsername());
    Assert.assertEquals("4$3cur3P4$$w0rd", admin.getPassword());
    admin.getAuthorities()
        .forEach(authority -> Assert.assertTrue(adminRoleAuthorities.contains(authority)));
    adminRoleAuthorities
        .forEach(authority -> Assert.assertTrue(admin.getAuthorities().contains(authority)));

    new Verifications() {{
      userRepositoryMock.findByUsername(username);
      times = 1;
    }};
  }

  @Test(expected = UsernameNotFoundException.class)
  public void testUserDetailsServiceWithUnknownUser() throws Exception {
    final String username = "david";

    new Expectations() {{
      userRepositoryMock.findByUsername(username);
      result = null;
    }};

    AuthenticationManagerBuilder authenticationManagerBuilder = new AuthenticationManagerBuilder(
        objectPostProcessorMock);
    authenticationConfig.init(authenticationManagerBuilder);
    UserDetailsService userDetailsService = authenticationManagerBuilder
        .getDefaultUserDetailsService();
    userDetailsService.loadUserByUsername(username);
  }

}
