package ch.fhnw.edu.wodss.tippspielapi.service;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ArgonPasswordEncoder implements PasswordEncoder {

  private static final ArgonPasswordEncoder instance = new ArgonPasswordEncoder();

  @Value("${security.argon2.defaultSaltLength}")
  private int defaultSaltLength;

  @Value("${security.argon2.defaultHashLength}")
  private int defaultHashLength;

  @Value("${security.argon2.iterations}")
  private int iterations;

  @Value("${security.argon2.memory}")
  private int memory;

  @Value("${security.argon2.parallelism}")
  private int parallelism;

  private ArgonPasswordEncoder() {
  }

  public static ArgonPasswordEncoder getInstance() {
    return instance;
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    return Argon2Factory.create(defaultSaltLength, defaultHashLength)
        .verify(encodedPassword, rawPassword.toString());
  }

  @Override
  public String encode(CharSequence rawPassword) {
    Argon2 argon2 = Argon2Factory.create(defaultSaltLength, defaultHashLength);
    String hash = argon2.hash(iterations, memory, parallelism, rawPassword.toString());
    return hash;
  }
}
