package ch.fhnw.edu.wodss.tippspielapi.service.exception;

import ch.fhnw.edu.wodss.tippspielapi.service.EmailService;

/**
 * This {@link RuntimeException} represents an issue while sending an email (using the {@link
 * EmailService}).
 */
public class EmailSendingException extends RuntimeException {

  public EmailSendingException(String message) {
    super();
  }
}
