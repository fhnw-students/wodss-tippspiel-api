package ch.fhnw.edu.wodss.tippspielapi.config.authentication;

import ch.fhnw.edu.wodss.tippspielapi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.persistence.UserRepository;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class AuthenticationConfig extends GlobalAuthenticationConfigurerAdapter {

  public static final String USER_ROLE_NAME = "ROLE_USER";
  public static final String ADMIN_ROLE_NAME = "ROLE_ADMIN";

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtAuthenticationProvider jwtAuthenticationProvider;

  @Override
  public void init(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    UserDetailsService userDetailsService = createUserDetailsService();
    authenticationManagerBuilder.userDetailsService(userDetailsService);
    authenticationManagerBuilder.authenticationProvider(jwtAuthenticationProvider);
  }

  private UserDetailsService createUserDetailsService() {
    return username -> {
      User user = userRepository.findByUsername(username);
      if (user != null) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority(USER_ROLE_NAME));
        if (user.isAdmin()) {
          grantedAuthorities.add(new SimpleGrantedAuthority(ADMIN_ROLE_NAME));
        }
        UserDetails builtUser = org.springframework.security.core.userdetails.User.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .authorities(grantedAuthorities)
            .build();
        return builtUser;
      } else {
        throw new UsernameNotFoundException("Could not find user [" + username + "].");
      }
    };
  }

}
