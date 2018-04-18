package ch.fhnw.edu.wodss.tippspielapi.tippspielApi.service;

import ch.fhnw.edu.wodss.tippspielapi.tippspielApi.config.authentication.JwtAuthenticationToken;
import ch.fhnw.edu.wodss.tippspielapi.tippspielApi.controller.dto.NewUserDto;
import ch.fhnw.edu.wodss.tippspielapi.tippspielApi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.tippspielApi.persistence.UserRepository;
import java.util.Locale;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import net.bytebuddy.pool.TypePool.Resolution.Illegal;
import org.junit.Assert;
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
  EmailService emailService;

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

      user.hasAuthenticationTokenExpired();
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

      user.hasAuthenticationTokenExpired();
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

  @Test(expected = IllegalStateException.class)
  public void testLoginWithNotVerifiedUser(@Mocked SecurityContextHolder anyInstance) {
    UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
        .username("david")
        .password("anyPassword")
        .authorities("ROLE_USER", "ROLE_ADMIN")
        .build();

    User user = new User();
    user.setUsername("david");
    user.setPassword("1234");
    user.setEmail("david@students.ch");
    user.setAdmin(true);
    user.setId(27L);
    user.generateVerificationToken();

    new Expectations() {{
      SecurityContextHolder.getContext();
      result = securityContextMock;

      securityContextMock.getAuthentication();
      result = new UsernamePasswordAuthenticationToken(userDetails, "1234");

      userRepository.findByUsername("david");
      result = user;
    }};

    authenticationService.login();

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

  @Test
  public void testRegisterNewUser(@Mocked SecurityContextHolder anyInstance) {
    User user = new User();
    user.setId(27L);
    user.setEmail("davu@students.ch");
    user.setUsername("davu");
    user.setPassword("1234");
    user.setAdmin(false);

    new Expectations() {{
      userRepository.findByEmail("davu@students.ch");
      result = null;

      userRepository.save((User) any);
      result = user;

    }};

    NewUserDto newUserDto = new NewUserDto();
    newUserDto.setEmail("davu@students.ch");
    newUserDto.setUsername("davu");
    newUserDto.setPassword("1234");
    User registeredUser = authenticationService.register(newUserDto);

    Assert.assertEquals(user, registeredUser);

    new Verifications() {{
      userRepository.findByEmail("davu@students.ch");
      times = 1;

      userRepository.save((User) any);
      times = 1;
    }};
  }

  @Test
  public void testRegisterUserWithExistingEmail() {
    User user = new User();
    user.setId(27L);
    user.setEmail("davu@students.ch");
    user.setUsername("davu");
    user.setPassword("1234");
    user.setAdmin(false);

    new Expectations() {{
      userRepository.findByEmail("davu@students.ch");
      result = user;
    }};

    NewUserDto newUserDto = new NewUserDto();
    newUserDto.setEmail("davu@students.ch");
    newUserDto.setUsername("davu");
    newUserDto.setPassword("1234");
    User registeredUser = authenticationService.register(newUserDto);

    Assert.assertNull(registeredUser);

    new Verifications() {{
      userRepository.findByEmail("davu@students.ch");
      times = 1;
    }};
  }

  @Test
  public void testVerifyNewUser() {
    User user = new User();
    user.setId(27L);
    user.setEmail("davu@students.ch");
    user.setUsername("davu");
    user.setPassword("1234");
    user.setAdmin(false);
    user.generateVerificationToken();

    new Expectations() {{
      userRepository.findByVerificationToken("9938d833-c3be-4b79-aea6-3adc180075d0");
      result = user;
    }};

    authenticationService.verify("9938d833-c3be-4b79-aea6-3adc180075d0");

    new Verifications() {{
      userRepository.findByVerificationToken("9938d833-c3be-4b79-aea6-3adc180075d0");
      times = 1;

      user.clearVerificationToken();
      times = 1;

      userRepository.save(user);
      times = 1;
    }};
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVerifyVerifiedUser() {
    new Expectations() {{
      userRepository.findByVerificationToken("9938d833-c3be-4b79-aea6-3adc180075d0");
      result = null;
    }};
    authenticationService.verify("9938d833-c3be-4b79-aea6-3adc180075d0");

    new Verifications() {{
      userRepository.findByVerificationToken("9938d833-c3be-4b79-aea6-3adc180075d0");
      times = 1;
    }};
  }

  @Test
  public void resetVerifiedUser() {
    User user = new User();
    user.setId(2947L);
    user.setAdmin(false);
    user.setEmail("any@email.com");
    user.setUsername("jimmyhendrix");
    Assert.assertNull(user.getVerificationToken());
    Assert.assertNull(user.getResetToken());

    new Expectations() {{
      userRepository.findByEmail("any@email.com");
      result = user;

      userRepository.save(user);
      result = user;
    }};
    authenticationService.reset("any@email.com", Locale.ENGLISH);

    new Verifications() {{
      userRepository.findByEmail("any@email.com");
      times = 1;

      emailService.sendResetEmail(user, Locale.ENGLISH);
      times = 1;

      userRepository.save(user);
      times = 1;
    }};
  }

  @Test
  public void resetNotVerifiedUser() {
    User user = new User();
    user.setId(7045L);
    user.setAdmin(false);
    user.setEmail("tom@hanks.com");
    user.setUsername("tomhanks");
    user.generateVerificationToken();
    Assert.assertNotNull(user.getVerificationToken());
    Assert.assertNull(user.getResetToken());

    new Expectations() {{
      userRepository.findByEmail("any@email.com");
      result = user;
    }};
    authenticationService.reset("any@email.com", Locale.ENGLISH);

    new Verifications() {{
      userRepository.findByEmail("any@email.com");
      times = 1;

      emailService.sendVerificationEmail(user, Locale.ENGLISH);
      times = 1;

      emailService.sendResetEmail((User) any, (Locale) any);
      times = 0;

      userRepository.save(user);
      times = 0;
    }};
  }

  @Test
  public void resetNotExistingUser() {
    new Expectations() {{
      userRepository.findByEmail("any@email.com");
      result = null;
    }};
    try {
      authenticationService.reset("any@email.com", Locale.ENGLISH);
      Assert.fail();
    } catch (IllegalArgumentException e) {
      // expected
    }

    new Verifications() {{
      userRepository.findByEmail("any@email.com");
      times = 1;

      emailService.sendVerificationEmail(user, Locale.ENGLISH);
      times = 0;

      emailService.sendResetEmail((User) any, (Locale) any);
      times = 0;

      userRepository.save(user);
      times = 0;
    }};
  }

  @Test
  public void resetResettingUser() {
    User user = new User();
    user.setId(2947L);
    user.setAdmin(false);
    user.setEmail("any@email.com");
    user.setUsername("alicecooper");
    user.generateResetToken();
    Assert.assertNull(user.getVerificationToken());
    Assert.assertNotNull(user.getResetToken());

    new Expectations() {{
      userRepository.findByEmail("alice@cooper.com");
      result = user;
    }};
    authenticationService.reset("alice@cooper.com", Locale.ENGLISH);

    new Verifications() {{
      userRepository.findByEmail("alice@cooper.com");
      times = 1;

      emailService.sendResetEmail(user, Locale.ENGLISH);
      times = 1;

      emailService.sendVerificationEmail((User) any, (Locale) any);
      times = 0;

      userRepository.save((User) any);
      times = 0;
    }};
  }

}