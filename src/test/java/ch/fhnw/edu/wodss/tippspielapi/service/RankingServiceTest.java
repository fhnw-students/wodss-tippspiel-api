package ch.fhnw.edu.wodss.tippspielapi.service;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.UserRankingDto;
import ch.fhnw.edu.wodss.tippspielapi.model.Tip;
import ch.fhnw.edu.wodss.tippspielapi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.model.builder.TipBuilder;
import ch.fhnw.edu.wodss.tippspielapi.model.builder.UserBuilder;
import ch.fhnw.edu.wodss.tippspielapi.persistence.TipRepository;
import ch.fhnw.edu.wodss.tippspielapi.persistence.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class RankingServiceTest {

  @Tested
  private RankingService rankingService;

  @Injectable
  private UserRepository userRepository;

  @Injectable
  private TipRepository tipRepository;

  @Test
  public void testRankingWithoutUsers() {
    new Expectations() {{
      userRepository.findAll();
      result = new ArrayList<User>();
    }};

    List<UserRankingDto> userRankingDtos = rankingService.generateRanking();

    Assert.assertTrue(userRankingDtos.isEmpty());
  }

  @Test
  public void testRankingHappyPath() {
    List<User> users = new ArrayList<>();
    users.add(createUser("gery@students.fhnw.ch", "hirsch", true, 2L));
    users.add(createUser("david@students.fhnw.ch", "davu", true, 1L));
    users.add(createUser("ken@students.fhnw.ch", "ken", false, 3L));

    List<Tip> gerysTips = new ArrayList<>();
    gerysTips.add(new TipBuilder().withUser(users.get(0)).withPoints(4).build());
    gerysTips.add(new TipBuilder().withUser(users.get(0)).withPoints(6).build());
    gerysTips.add(new TipBuilder().withUser(users.get(0)).withPoints(6).build());

    List<Tip> davusTips = new ArrayList<>();
    davusTips.add(new TipBuilder().withUser(users.get(1)).withPoints(6).build());
    davusTips.add(new TipBuilder().withUser(users.get(1)).withPoints(10).build());
    davusTips.add(new TipBuilder().withUser(users.get(1)).withPoints(0).build());
    davusTips.add(new TipBuilder().withUser(users.get(1)).withPoints(4).build());

    List<Tip> kensTips = new ArrayList<>();
    kensTips.add(new TipBuilder().withUser(users.get(2)).withPoints(10).build());
    kensTips.add(new TipBuilder().withUser(users.get(2)).withPoints(0).build());

    List<Integer> expectedScores = Arrays.asList(20, 16, 10);
    List<String> expectedNames = Arrays.asList("davu", "hirsch", "ken");
    List<Integer> expectedGames = Arrays.asList(4, 3, 2);

    new Expectations() {{
      userRepository.findAll();
      this.result = users;

      tipRepository.findByUserId(anyLong);
      returns(gerysTips, davusTips, kensTips);
    }};

    List<UserRankingDto> userRankingDtos = rankingService.generateRanking();

    for (int i = 0; i < userRankingDtos.size(); i++) {
      UserRankingDto userRankingDto = userRankingDtos.get(i);
      Assert.assertEquals(i + 1, userRankingDto.getRank());
      Assert.assertEquals(expectedScores.get(i).intValue(), userRankingDto.getPoints());
      Assert.assertEquals(expectedNames.get(i), userRankingDto.getUser().getUsername());
      Assert.assertEquals(expectedGames.get(i).intValue(), userRankingDto.getGames());
    }
  }

  @Test
  public void testRankingWithSameScores() {
    List<User> users = new ArrayList<>();
    users.add(createUser("gery@students.fhnw.ch", "hirsch", true, 2L));
    users.add(createUser("david@students.fhnw.ch", "davu", true, 1L));
    users.add(createUser("ken@students.fhnw.ch", "ken", false, 3L));

    List<Tip> gerysTips = new ArrayList<>();
    gerysTips.add(new TipBuilder().withUser(users.get(0)).withPoints(2).build());
    gerysTips.add(new TipBuilder().withUser(users.get(0)).withPoints(8).build());
    gerysTips.add(new TipBuilder().withUser(users.get(0)).withPoints(10).build());
    gerysTips.add(new TipBuilder().withUser(users.get(0)).withPoints(0).build());
    gerysTips.add(new TipBuilder().withUser(users.get(0)).withPoints(0).build());

    List<Tip> davusTips = new ArrayList<>();
    davusTips.add(new TipBuilder().withUser(users.get(1)).withPoints(6).build());
    davusTips.add(new TipBuilder().withUser(users.get(1)).withPoints(10).build());
    davusTips.add(new TipBuilder().withUser(users.get(1)).withPoints(0).build());
    davusTips.add(new TipBuilder().withUser(users.get(1)).withPoints(4).build());

    List<Tip> kensTips = new ArrayList<>();
    kensTips.add(new TipBuilder().withUser(users.get(2)).withPoints(10).build());
    kensTips.add(new TipBuilder().withUser(users.get(2)).withPoints(0).build());

    List<Integer> expectedScores = Arrays.asList(20, 20, 10);
    List<String> expectedNames = Arrays.asList("davu", "hirsch", "ken");
    List<Integer> expectedGames = Arrays.asList(4, 5, 2);

    new Expectations() {{
      userRepository.findAll();
      this.result = users;

      tipRepository.findByUserId(anyLong);
      returns(gerysTips, davusTips, kensTips);
    }};

    List<UserRankingDto> userRankingDtos = rankingService.generateRanking();

    for (int i = 0; i < userRankingDtos.size(); i++) {
      UserRankingDto userRankingDto = userRankingDtos.get(i);
      Assert.assertEquals(i + 1, userRankingDto.getRank());
      Assert.assertEquals(expectedScores.get(i).intValue(), userRankingDto.getPoints());
      Assert.assertEquals(expectedNames.get(i), userRankingDto.getUser().getUsername());
      Assert.assertEquals(expectedGames.get(i).intValue(), userRankingDto.getGames());
    }
  }

  private User createUser(String email, String username, boolean admin, Long id) {
    return new UserBuilder()
        .withEmail(email)
        .withUsername(username)
        .withAdmin(admin)
        .withId(id)
        .build();
  }


}