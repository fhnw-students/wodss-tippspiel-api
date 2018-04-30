package ch.fhnw.edu.wodss.tippspielapi.service;

import java.util.Locale;

import ch.fhnw.edu.wodss.tippspielapi.service.I18NService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class I18NServiceTest {

  private I18NService i18nService;

  @Before
  public void setup() {
    i18nService = new I18NService();
  }

  @Test
  public void testGetLocalizedString() {
    String germanSpain = i18nService.getLocalizedString("country.name.es", Locale.GERMAN);
    Assert.assertEquals("Spanien", germanSpain);
  }

  @Test
  public void testGetParameterizedLocalizedString() {
    String englishEmailResetText = i18nService
        .getParameterizedLocalizedString("email.reset.content", Locale.ENGLISH, "testuser",
            "http://localhost:3000", "token");
    Assert.assertEquals(
        "<html><body><h2>Hello testuser!</h2><p>Someone requested to reset your account for the bet game. If that was you, use <a href=\"http://localhost:3000/auth/reset/token\">this link</a> to set a new password. Otherwise please ignore this email.</p><p>Cheers,<br />Tippspiel App</body></html>",
        englishEmailResetText);
  }

}
