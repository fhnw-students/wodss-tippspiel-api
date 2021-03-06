package ch.fhnw.edu.wodss.tippspielapi.service;

import ch.fhnw.edu.wodss.tippspielapi.config.authentication.JwtAuthenticationToken;
import ch.fhnw.edu.wodss.tippspielapi.service.AuthenticationService;
import ch.fhnw.edu.wodss.tippspielapi.service.EmailService;
import ch.fhnw.edu.wodss.tippspielapi.service.exception.IllegalPasswordException;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.NewUserDto;
import ch.fhnw.edu.wodss.tippspielapi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.persistence.UserRepository;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Helper;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
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

  @Injectable
  TokenHelper tokenHelper;

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
  public void testRegisterNewUser(@Mocked SecurityContextHolder anyInstance,
      @Mocked ArgonPasswordEncoder instance) {
    User user = new User();
    user.setId(27L);
    user.setEmail("davu@students.ch");
    user.setUsername("davu");
    user.setPassword("1234");
    user.setAdmin(false);

    new Expectations() {{
      userRepository.findByEmailOrUsername("davu@students.ch", "davu");
      result = null;

      userRepository.save((User) any);
      result = user;

      ArgonPasswordEncoder.getInstance();
      result = instance;

      instance.encode("1234");
      result = "1234";
    }};

    NewUserDto newUserDto = new NewUserDto();
    newUserDto.setEmail("davu@students.ch");
    newUserDto.setUsername("davu");
    newUserDto.setPassword("1234");
    User registeredUser = authenticationService.register(newUserDto, Locale.ENGLISH);

    Assert.assertEquals(user, registeredUser);

    new Verifications() {{
      userRepository.findByEmailOrUsername("davu@students.ch", "davu");
      times = 1;

      userRepository.save((User) any);
      times = 1;
    }};
  }

  @Test
  public void testRegisterUserWithExistingEmail(@Mocked ArgonPasswordEncoder instance) {
    User user = new User();
    user.setId(27L);
    user.setEmail("davu@students.ch");
    user.setUsername("nonExistingUsername");
    user.setPassword("1234abcd");
    user.setAdmin(false);
    List<User> foundUsers = Arrays.asList(user);

    new Expectations() {{
      userRepository.findByEmailOrUsername("davu@students.ch", "nonExistingUsername");
      result = foundUsers;
    }};

    NewUserDto newUserDto = new NewUserDto();
    newUserDto.setEmail("davu@students.ch");
    newUserDto.setUsername("nonExistingUsername");
    newUserDto.setPassword("1234abcd");
    User registeredUser = authenticationService.register(newUserDto, Locale.ENGLISH);

    Assert.assertNull(registeredUser);

    new Verifications() {{
      userRepository.findByEmailOrUsername("davu@students.ch", "nonExistingUsername");
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

  public void testResetVerifiedUser() {
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
  public void testResetNotVerifiedUser() {
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
  public void testResetNotExistingUser() {
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
  public void testResetResettingUser() {
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

  @Test
  public void testNewPasswordOnExistingUser() {
    User user = new User();
    user.setId(2947L);
    user.setAdmin(false);
    user.setEmail("any@email.com");
    user.setUsername("jimmyhendrix");
    user.setPassword("thisIsAPassword");
    user.generateResetToken();
    Assert.assertNull(user.getToken());
    Assert.assertNull(user.getVerificationToken());
    final String resetToken = user.getResetToken();
    Assert.assertNotNull(resetToken);

    new Expectations() {{
      userRepository.findByResetToken(resetToken);
      result = user;

      userRepository.save(user);
      result = user;
    }};

    authenticationService.saveNewPassword(resetToken, "thisIsANewPassword");
    Assert.assertNull(user.getResetToken());

    new Verifications() {{
      userRepository.findByResetToken(resetToken);
      times = 1;

      userRepository.save(user);
      times = 1;
    }};
  }


  @Test
  public void testNewInvalidPasswordOnExistingUser() {
    User user = new User();
    user.setId(2947L);
    user.setAdmin(false);
    user.setEmail("any@email.com");
    user.setUsername("jimmyhendrix");
    user.setPassword("thisIsAPassword");
    user.generateResetToken();
    Assert.assertNull(user.getToken());
    Assert.assertNull(user.getVerificationToken());
    final String resetToken = user.getResetToken();
    Assert.assertNotNull(resetToken);

    new Expectations() {{
      userRepository.findByResetToken(resetToken);
      result = user;
    }};

    try {
      authenticationService.saveNewPassword(resetToken, "tsp");
      Assert.fail();
    } catch (IllegalPasswordException e) {
      // Everything is good, this is expected as tsp is a too short password
    }
    Assert.assertEquals(resetToken, user.getResetToken());
    Assert.assertEquals("thisIsAPassword", user.getPassword());

    new Verifications() {{
      userRepository.findByResetToken(resetToken);
      times = 1;

      userRepository.save(user);
      times = 0;
    }};
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNewPasswordOnNotExistingUser() {
    String resetToken = UUID.randomUUID().toString();

    new Expectations() {{
      userRepository.findByResetToken(resetToken);
      result = null;

    }};
    authenticationService.saveNewPassword(resetToken, "thisIsANewPassword");

    new Verifications() {{
      userRepository.findByResetToken(resetToken);
      times = 1;

      userRepository.save((User) any);
      times = 0;
    }};
  }

}