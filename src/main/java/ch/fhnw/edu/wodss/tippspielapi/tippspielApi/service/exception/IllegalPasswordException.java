package ch.fhnw.edu.wodss.tippspielapi.tippspielApi.service.exception;

/**
 * This class represents a {@link RuntimeException} that is thrown whenever somebody tries to set a
 * new password that doesn't match the constraints of the tippspiel app.
 */
public class IllegalPasswordException extends RuntimeException {

  public IllegalPasswordException(String message) {
    super(message);
  }
}
