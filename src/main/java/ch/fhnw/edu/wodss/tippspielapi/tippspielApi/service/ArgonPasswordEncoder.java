package ch.fhnw.edu.wodss.tippspielapi.tippspielApi.service;

import org.springframework.security.crypto.password.PasswordEncoder;

public class ArgonPasswordEncoder implements PasswordEncoder {

  private static final ArgonPasswordEncoder instance = new ArgonPasswordEncoder();

  private ArgonPasswordEncoder() {

  }

  public static ArgonPasswordEncoder getInstance() {
    return instance;
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    return encode(rawPassword).equals(encodedPassword);
  }

  @Override
  public String encode(CharSequence rawPassword) {
    // TODO: implement this.
    return String.valueOf(rawPassword);
  }
}
