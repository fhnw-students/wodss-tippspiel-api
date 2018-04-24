package ch.fhnw.edu.wodss.tippspielapi.config.authentication;

import ch.fhnw.edu.wodss.tippspielapi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

  public static final String ADMIN_ROLE_NAME = "ROLE_ADMIN";
  public static final String USER_ROLE_NAME = "ROLE_USER";

  @Autowired
  private UserRepository userRepository;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String token = authentication.getCredentials().toString();
    User user = userRepository.findByToken(token);
    if (user != null && !user.hasAuthenticationTokenExpired()) {
      JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
      jwtAuthenticationToken.addAuthority(new SimpleGrantedAuthority(USER_ROLE_NAME));
      if (user.getIsAdmin()) {
        jwtAuthenticationToken.addAuthority(new SimpleGrantedAuthority(ADMIN_ROLE_NAME));
      }
      jwtAuthenticationToken.setPrincipal(user.getUsername());
      jwtAuthenticationToken.setAuthenticated(true);
      return jwtAuthenticationToken;
    } else {
      throw new AuthenticationException("Authentication not successful.") {
        // no overrides necessary!
      };
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return JwtAuthenticationToken.class.equals(authentication);
  }
}