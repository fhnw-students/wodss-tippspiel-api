package ch.fhnw.edu.wodss.tippspielapi.TippspielApi.config;

import ch.fhnw.edu.wodss.tippspielapi.TippspielApi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.TippspielApi.persistence.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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

  @Autowired
  private UserRepository userRepository;

  @Override
  public void init(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    UserDetailsService userDetailsService = createUserDetailsService();
    authenticationManagerBuilder.userDetailsService(userDetailsService);
  }

  private UserDetailsService createUserDetailsService() {
    return username -> {
      User user = userRepository.findByUsername(username);
      if (user != null) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (user.getAdmin()) {
          grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
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
