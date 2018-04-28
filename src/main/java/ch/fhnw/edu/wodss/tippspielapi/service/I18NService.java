package ch.fhnw.edu.wodss.tippspielapi.service;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import org.springframework.stereotype.Service;

@Service
public class I18NService {

  public static final String MESSAGE_BUNDLE_NAME = "ch.fhnw.edu.wodss.tipspielapi.MessageBundle";

  public String getLocalizedString(String key, Locale locale) {
    ResourceBundle resourceBundle = ResourceBundle.getBundle(MESSAGE_BUNDLE_NAME, locale);
    return resourceBundle.getString(key);
  }

  public String getParameterizedLocalizedString(String key, Locale locale, String... parameters) {
    ResourceBundle resourceBundle = ResourceBundle.getBundle(MESSAGE_BUNDLE_NAME, locale);
    String rawContent = resourceBundle.getString(key);
    return MessageFormat.format(rawContent, parameters);
  }

}
