package ch.fhnw.edu.wodss.tippspielapi.tippspielApi.service;

import org.springframework.security.crypto.password.PasswordEncoder;

public class ArgonPasswordEncoder implements PasswordEncoder {

  private static final ArgonPasswordEncoder instance = new ArgonPasswordEncoder();

  private ArgonPasswordEncoder() {

  }

  public static ArgonPasswordEncoder getInstance() {
    return instance;
  }

  // TODO: implement this.
  @Override
  public String encode(CharSequence rawPassword) {
    return String.valueOf(rawPassword);
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    return encode(rawPassword).equals(encodedPassword);
  }
}
