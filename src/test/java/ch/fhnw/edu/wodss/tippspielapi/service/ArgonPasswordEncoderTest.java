package ch.fhnw.edu.wodss.tippspielapi.service;

import mockit.Tested;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(value={"spring.profiles.active=local"})
public class ArgonPasswordEncoderTest {

  @Tested
  ArgonPasswordEncoder argonPasswordEncoder;

  @Test
  public void testSuccessfulAuthentication() {
    String randomPasswordHash = argonPasswordEncoder.encode("aR4nd0mPa$$w0rd");
    Assert.assertTrue(argonPasswordEncoder.matches("aR4nd0mPa$$w0rd", randomPasswordHash));
  }

  @Test
  public void testUnsuccessfulAuthentication() {
    String randomPasswordHash = argonPasswordEncoder.encode("aR4nd0mPa$$w0rd");
    Assert.assertFalse(argonPasswordEncoder.matches("aR4nd0mBa$$w0rt", randomPasswordHash));
  }

}