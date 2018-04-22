package ch.fhnw.edu.wodss.tippspielapi.tippspielApi.service.exception;

/**
 * This {@link RuntimeException} represents an issue while sending an email (using the {@link
 * ch.fhnw.edu.wodss.tippspielapi.tippspielApi.service.EmailService}).
 */
public class EmailSendingException extends RuntimeException {

  public EmailSendingException(String message) {
    super();
  }
}
