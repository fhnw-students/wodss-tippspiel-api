package ch.fhnw.edu.wodss.tippspielapi.service;

import ch.fhnw.edu.wodss.tippspielapi.model.UserRanking;
import ch.fhnw.edu.wodss.tippspielapi.persistence.RankingRepository;
import ch.fhnw.edu.wodss.tippspielapi.persistence.RankingRepository.UserRankingInformation;
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
import org.springframework.data.domain.PageRequest;

@RunWith(JMockit.class)
public class RankingServiceTest {

  @Tested
  private RankingService rankingService;

  @Injectable
  private UserRepository userRepository;

  @Injectable
  private TipRepository tipRepository;

  @Injectable
  private RankingRepository rankingRepository;

  @Test
  public void testRankingWithoutUsers() {
    new Expectations() {{
      rankingRepository.getUserRankingInformation(PageRequest.of(5, 5));
      result = new ArrayList<UserRanking>();
    }};

    List<UserRanking> userRanking = rankingService.generateRanking(5, 5);

    Assert.assertTrue(userRanking.isEmpty());
  }

  @Test
  public void testRankingHappyPath() {
    List<UserRankingInformation> userRankingInformation = new ArrayList<>();
    userRankingInformation.add(createUserRankingInformation(2L, "hirsch", true, 50, 5));
    userRankingInformation.add(createUserRankingInformation(1L, "davu", true, 44, 5));
    userRankingInformation.add(createUserRankingInformation(3L, "ken", false, 35, 4));

    List<Integer> expectedScores = Arrays.asList(50, 44, 35);
    List<String> expectedNames = Arrays.asList("davu", "hirsch", "ken");
    List<Integer> expectedGames = Arrays.asList(5, 5, 4);

    new Expectations() {{
      rankingRepository.getUserRankingInformation(PageRequest.of(5, 5));
      this.result = userRankingInformation;
    }};

    List<UserRanking> userRankings = rankingService.generateRanking(5, 5);

    for (int i = 0; i < userRankings.size(); i++) {
      UserRanking userRanking = userRankings.get(i);
      Assert.assertEquals(i + 1, userRanking.getRank());
      Assert.assertEquals(expectedScores.get(i).intValue(), userRanking.getPoints());
      Assert.assertEquals(expectedNames.get(i), userRanking.getUsername());
      Assert.assertEquals(expectedGames.get(i).intValue(), userRanking.getGames());
    }
  }

  @Test
  public void testRankingWithSameScores() {
    List<UserRankingInformation> userRankingInformation = new ArrayList<>();
    userRankingInformation.add(createUserRankingInformation(2L, "hirsch", true, 99, 9));
    userRankingInformation.add(createUserRankingInformation(1L, "davu", true, 99, 10));
    userRankingInformation.add(createUserRankingInformation(3L, "ken", false, 87, 10));

    List<Integer> expectedScores = Arrays.asList(99, 99, 87);
    List<String> expectedNames = Arrays.asList("hirsch", "davu", "ken");
    List<Integer> expectedGames = Arrays.asList(9, 10, 10);

    new Expectations() {{
      rankingRepository.getUserRankingInformation(PageRequest.of(5, 5));
      result = userRankingInformation;
    }};

    List<UserRanking> userRankings = rankingService.generateRanking(0, 1);

    for (int i = 0; i < userRankings.size(); i++) {
      UserRanking userRanking = userRankings.get(i);
      Assert.assertEquals(i + 1, userRanking.getRank());
      Assert.assertEquals(expectedScores.get(i).intValue(), userRanking.getPoints());
      Assert.assertEquals(expectedNames.get(i), userRanking.getUsername());
      Assert.assertEquals(expectedGames.get(i).intValue(), userRanking.getGames());
    }
  }

  private UserRankingInformation createUserRankingInformation(Long id, String username,
      boolean admin,
      int points, int games) {
    return new UserRankingInformation() {
      @Override
      public String getUsername() {
        return username;
      }

      @Override
      public Long getUserId() {
        return id;
      }

      @Override
      public int getPoints() {
        return points;
      }

      @Override
      public int getGames() {
        return games;
      }
    };
  }


}