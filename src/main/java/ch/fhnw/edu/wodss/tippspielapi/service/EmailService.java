package ch.fhnw.edu.wodss.tippspielapi.service;

import ch.fhnw.edu.wodss.tippspielapi.model.Team;
import ch.fhnw.edu.wodss.tippspielapi.service.exception.EmailSendingException;
import ch.fhnw.edu.wodss.tippspielapi.model.User;
import com.sun.mail.smtp.SMTPTransport;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

  public static final String VERIFICATION_EMAIL_SUBJECT = "email.verification.subject";
  public static final String VERIFICATION_EMAIL_CONTENT = "email.verification.content";
  private static final String RESET_EMAIL_SUBJECT = "email.reset.subject";
  private static final String RESET_EMAIL_CONTENT = "email.reset.content";
  private static final String TEAM_INVITATION_EMAIL_SUBJECT = "email.team-invitation.subject";
  private static final String TEAM_INVITATION_EMAIL_CONTENT = "email.team-invitation.content";
  public static final String CONTENT_TYPE = "text/html; charset=ISO-8859-1";

  @Autowired
  private I18NService i18nService;

  @Value("${email.smtp.server}")
  private String host;

  @Value("${email.smtp.sender.email}")
  private String sender;

  @Value("${email.smtp.sender.password}")
  private String senderPassword;

  @Value("${email.smtp.server.port}")
  private int port;

  @Value("${email.smtp.auth}")
  private boolean auth;

  @Value("${frontend.host}")
  private String frontendHost;

  /**
   * s Sends a registration verification email with link to the given user in the given language.
   */
  public void sendVerificationEmail(User user, Locale locale) {
    Session session = prepareSession();
    MimeMessage message = createMessage(user.getEmail(), locale, session,
        VERIFICATION_EMAIL_SUBJECT, VERIFICATION_EMAIL_CONTENT, user.getUsername(), frontendHost,
        user.getVerificationToken());
    LOGGER.info("Sending verification email with verification token [" + user.getVerificationToken()
        + "] to user [" + user.getEmail() + "].");
    sendMessage(session, message);
    LOGGER.info("Sent verification email with verification token [" + user.getVerificationToken()
        + "] to user [" + user.getEmail() + "].");
  }

  /**
   * Sends a password reset link to the given user in the given language.
   */
  public void sendResetEmail(User user, Locale locale) {
    Session session = prepareSession();
    MimeMessage message = createMessage(user.getEmail(), locale, session,
        RESET_EMAIL_SUBJECT, RESET_EMAIL_CONTENT, user.getUsername(), frontendHost,
        user.getResetToken());
    LOGGER.info("Sending password reset email with reset token [" + user.getResetToken()
        + "] to user [" + user.getEmail() + "].");
    sendMessage(session, message);
    LOGGER.info("Sent password reset email with reset token [" + user.getResetToken()
        + "] to user [" + user.getEmail() + "].");
  }

  /**
   * Sends a team invitation message to the given email in the given language.
   */
  public void sendInvitationEmailTo(String email, Team team, Locale locale) {
    Session session = prepareSession();
    MimeMessage message = createMessage(email, locale, session,
            TEAM_INVITATION_EMAIL_SUBJECT, TEAM_INVITATION_EMAIL_CONTENT, team.getName(), frontendHost);
    LOGGER.info("Sending team-invitation email [" + team.getName()
            + "] to user [" + email + "].");
    sendMessage(session, message);
    LOGGER.info("Sent team-invitation email [" + team.getName()
            + "] to user [" + email + "].");
  }

  private Session prepareSession() {
    Properties properties = System.getProperties();
    properties.setProperty("mail.smtps.host", sender);
    properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    properties.setProperty("mail.smtp.socketFactory.fallback", "false");
    properties.setProperty("mail.smtp.port", String.valueOf(port));
    properties.setProperty("mail.smtp.socketFactory.port", String.valueOf(port));
    properties.setProperty("mail.smtps.auth", String.valueOf(auth));
    return Session.getDefaultInstance(properties);
  }

  private MimeMessage createMessage(String recipientAddress, Locale locale, Session session,
      String subjectKey, String messageKey, String... contentParameters) {

    MimeMessage message = new MimeMessage(session);

    addSender(message);
    addRecipient(recipientAddress, message);

    addSubject(subjectKey, message, locale);
    addContent(messageKey, message, locale, contentParameters);

    return message;
  }

  private void addSender(MimeMessage message) {
    try {
      message.setFrom(new InternetAddress(sender));
    } catch (MessagingException e) {
      throw new EmailSendingException("Email cannot be sent using sender: [" + sender + "].");
    }
  }

  private void addRecipient(String recipientAddress, MimeMessage message) {
    try {
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientAddress));
    } catch (MessagingException e) {
      throw new EmailSendingException(
          "Email cannot be sent using recipient: [" + recipientAddress + "].");
    }
  }

  private void addSubject(String subjectKey, MimeMessage message, Locale locale) {
    String subject = "";
    try {
      subject = i18nService.getLocalizedString(subjectKey, locale);
      message.setSubject(subject);
    } catch (MessagingException e) {
      throw new EmailSendingException("Email cannot be sent with subject [" + subject + "].");
    }
  }

  private void addContent(String messageKey, MimeMessage message, Locale locale,
      String[] contentParameters) {
    String content = "";
    try {
      String rawContent = i18nService
          .getParameterizedLocalizedString(messageKey, locale, contentParameters);
      content = MessageFormat.format(rawContent, contentParameters);
      message.setContent(content, CONTENT_TYPE);
    } catch (MessagingException e) {
      throw new EmailSendingException("Email cannot be sent with content [" + content + "].");
    }
  }

  private void sendMessage(Session session, MimeMessage message) {
    SMTPTransport transport = null;
    try {
      transport = (SMTPTransport) session.getTransport("smtps");
      transport.connect(host, sender, senderPassword);
      transport.sendMessage(message, message.getAllRecipients());
    } catch (MessagingException e) {
      LOGGER.error("Failed to send email.");
      e.printStackTrace();
      throw new EmailSendingException("Could not send email");
    } finally {
      try {
        transport.close();
      } catch (MessagingException e) {
        e.printStackTrace();
        LOGGER
            .error("Could not close the transport object. This is likely to create a memory leak!");
        throw new EmailSendingException("Could not close the transport object.");
      }

    }
  }
}
