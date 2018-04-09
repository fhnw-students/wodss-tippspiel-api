package ch.fhnw.edu.wodss.tippspielapi.TippspielApi.config;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class JwtAuthenticationToken implements Authentication {
  private final String token;
  private Collection<GrantedAuthority> authorities;
  private String username;
  private boolean authenticated;

  public JwtAuthenticationToken(String token) {
    this.token = token;
    authorities = new ArrayList<>();
    authenticated = false;
  }

  @Override
  public Collection<GrantedAuthority> getAuthorities() {
    return authorities;
  }

  public void addAuthority(SimpleGrantedAuthority authority) {
    authorities.add(authority);
  }

  @Override
  public Object getCredentials() {
    return token;
  }

  @Override
  public Object getDetails() {
    return new Object();
  }

  @Override
  public Object getPrincipal() {
    return username;
  }

  @Override
  public boolean isAuthenticated() {
    return authenticated;
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    this.authenticated = isAuthenticated;
  }

  @Override
  public String getName() {
    return username;
  }

  public void setPrincipal(String username) {
    this.username = username;
  }
}
