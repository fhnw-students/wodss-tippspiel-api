package ch.fhnw.edu.wodss.tippspielapi.config;

import ch.fhnw.edu.wodss.tippspielapi.config.authentication.JwtAuthFilter;
import ch.fhnw.edu.wodss.tippspielapi.service.ArgonPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    securedEnabled = true,
    jsr250Enabled = true,
    prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private JwtAuthFilter jwtAuthFilter;

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.cors()
        .and().authorizeRequests().antMatchers("/auth/login").fullyAuthenticated()
        .and().httpBasic()
        .and().csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurerAdapter() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");
      }
    };
  }


  @Bean
  public static PasswordEncoder passwordEncoder() {
    return ArgonPasswordEncoder.getInstance();
  }

}
