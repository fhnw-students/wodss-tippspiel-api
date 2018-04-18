package ch.fhnw.edu.wodss.tippspielapi.tippspielApi.service;

import ch.fhnw.edu.wodss.tippspielapi.tippspielApi.model.User;
import com.sun.mail.smtp.SMTPTransport;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

  public static final String VERIFICATION_EMAIL_SUBJECT = "email.verification.subject";
  public static final String VERIFICATION_EMAIL_CONTENT = "email.verification.content";

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

  public void sendVerificationEmail(User user, Locale locale) {
    Session session = prepareSession();
    try {
      MimeMessage message = createMessage(user, locale, session);
      sendMessage(session, message);
      LOGGER.info("Sent verification email with verification token [" + user.getVerificationToken()
          + "] to user [" + message.getAllRecipients() + "].");
    } catch (MessagingException e) {
      LOGGER.error(
          "An error occurred when sending the registration verification email to user [" + user
              .getUsername() + " " + user.getEmail() + "].");
      e.printStackTrace();
      throw new IllegalStateException("Email could not be sent.");
    }
  }

  private void sendMessage(Session session, MimeMessage message)
      throws MessagingException {
    SMTPTransport transport = (SMTPTransport) session.getTransport("smtps");
    transport.connect(host, sender, senderPassword);
    transport.sendMessage(message, message.getAllRecipients());
    transport.close();
  }

  private MimeMessage createMessage(User user, Locale locale, Session session)
      throws MessagingException {
    MimeMessage message = new MimeMessage(session);
    message.setFrom(new InternetAddress(sender));

    String to = user.getEmail();
    message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

    ResourceBundle resourceBundle = ResourceBundle
        .getBundle("ch.fhnw.edu.wodss.tipspielapi.MessageBundle", locale);

    String subject = resourceBundle.getString(VERIFICATION_EMAIL_SUBJECT);
    message.setSubject(subject);

    String rawContent = resourceBundle.getString(VERIFICATION_EMAIL_CONTENT);
    String content = MessageFormat.format(rawContent, user.getUsername(),
        frontendHost, user.getVerificationToken());
    message.setContent(content, "text/html; charset=ISO-8859-1");

    return message;
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
}
