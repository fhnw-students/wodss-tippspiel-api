package ch.fhnw.edu.wodss.tippspielapi.TippspielApi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .inMemoryAuthentication()
                .withUser("admin")
                .password("1234")
                .authorities("ROLE_USER");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors().and()
                .authorizeRequests()
                .antMatchers("/api").permitAll()
                .anyRequest().authenticated().and()
                .httpBasic();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // TODO: extract as new class
        return new PasswordEncoder() {

            // TODO: this has to encode the given raw password with argon2
            @Override
            public String encode(CharSequence rawPassword) {
                return "";
            }

            // TODO: this has to check the raw password against the encoded one (check where this comes from)
            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return true;
            }
        };
    }

}
