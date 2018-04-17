package ch.fhnw.edu.wodss.tippspielapi.tippspielApi.service;

import ch.fhnw.edu.wodss.tippspielapi.tippspielApi.model.User;
import com.sun.mail.smtp.SMTPTransport;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
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
  String host;

  @Value("${email.smtp.sender.email}")
  String sender;

  @Value("${email.smtp.sender.password}")
  String senderPassword;

  @Value("${email.smtp.server.port}")
  int port;

  @Value("${email.smtp.auth}")
  boolean auth;

  @Value("${frontend.host}")
  String frontendHost;

  public void sendVerificationEmail(User user, Locale locale) {
    Properties properties = System.getProperties();
    properties.setProperty("mail.smtps.host", sender);
    properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    properties.setProperty("mail.smtp.socketFactory.fallback", "false");
    properties.setProperty("mail.smtp.port", String.valueOf(port));
    properties.setProperty("mail.smtp.socketFactory.port", String.valueOf(port));
    properties.setProperty("mail.smtps.auth", String.valueOf(auth));
    Session session = Session.getDefaultInstance(properties);

    try {
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

      SMTPTransport transport = (SMTPTransport) session.getTransport("smtps");

      transport.connect(host, sender, senderPassword);
      transport.sendMessage(message, message.getAllRecipients());
      LOGGER.info("Sent verification email with verification token [" + user.getVerificationToken()
          + "] to user [" + user.getUsername() + " " + user.getEmail() + "].");
      transport.close();

    } catch (MessagingException e) {
      LOGGER.error(
          "An error occurred when sending the registration verification email to user [" + user
              .getUsername() + "" + user.getEmail() + "].");
      e.printStackTrace();
      throw new IllegalStateException("Email could not be sent.");
    }

  }
}
