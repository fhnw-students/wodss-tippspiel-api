package ch.fhnw.edu.wodss.tippspielapi.service;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.UserRankingDto;
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
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Page;
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
  public void testRankingWithoutUsers(@Mocked Page<UserRankingInformation> pageMock) {
    new Expectations() {{
      rankingRepository.getUserRankingInformation(PageRequest.of(5, 5));
      result = pageMock;

      pageMock.getContent();
      result = new ArrayList<UserRankingInformation>();

      pageMock.getTotalPages();
      result = 0;
    }};

    UserRankingDto userRankingDto = rankingService.generateRanking(5, 5);
    List<UserRanking> userRanking = userRankingDto.getRankings();

    Assert.assertTrue(userRanking.isEmpty());

    new Verifications() {{
      rankingRepository.getUserRankingInformation(PageRequest.of(5, 5));
      times = 1;

      pageMock.getTotalPages();
      times = 1;

      pageMock.getContent();
      times = 1;
    }};
  }

  @Test
  public void testRankingHappyPath(@Mocked Page<UserRankingInformation> pageMock) {
    List<UserRankingInformation> userRankingInformation = new ArrayList<>();
    userRankingInformation.add(createUserRankingInformation(2L, "hirsch", true, 50, 5));
    userRankingInformation.add(createUserRankingInformation(1L, "davu", true, 44, 5));
    userRankingInformation.add(createUserRankingInformation(3L, "ken", false, 35, 4));

    List<Integer> expectedScores = Arrays.asList(50, 44, 35);
    List<String> expectedNames = Arrays.asList("hirsch", "davu", "ken");
    List<Integer> expectedGames = Arrays.asList(5, 5, 4);

    new Expectations() {{
      rankingRepository.getUserRankingInformation(PageRequest.of(3, 9));
      result = pageMock;

      pageMock.getContent();
      result = userRankingInformation;

      pageMock.getTotalPages();
      result = userRankingInformation.size();
    }};

    UserRankingDto userRankingDto = rankingService.generateRanking(3, 9);
    List<UserRanking> userRankings = userRankingDto.getRankings();

    for (int i = 0; i < userRankings.size(); i++) {
      UserRanking userRanking = userRankings.get(i);
      Assert.assertEquals(i + 28, userRanking.getRank());
      Assert.assertEquals(expectedScores.get(i).intValue(), userRanking.getPoints());
      Assert.assertEquals(expectedNames.get(i), userRanking.getUsername());
      Assert.assertEquals(expectedGames.get(i).intValue(), userRanking.getGames());
    }
  }

  @Test
  public void testRankingWithSameScores(@Mocked Page<UserRankingInformation> pageMock) {
    List<UserRankingInformation> userRankingInformation = new ArrayList<>();
    userRankingInformation.add(createUserRankingInformation(2L, "hirsch", true, 99, 9));
    userRankingInformation.add(createUserRankingInformation(1L, "davu", true, 99, 10));
    userRankingInformation.add(createUserRankingInformation(3L, "ken", false, 87, 10));

    List<Integer> expectedScores = Arrays.asList(99, 99, 87);
    List<String> expectedNames = Arrays.asList("hirsch", "davu", "ken");
    List<Integer> expectedGames = Arrays.asList(9, 10, 10);

    new Expectations() {{
      rankingRepository.getUserRankingInformation(PageRequest.of(0, 3));
      result = pageMock;

      pageMock.getContent();
      result = userRankingInformation;

      pageMock.getTotalPages();
      result = 3;
    }};

    UserRankingDto result = rankingService.generateRanking(0, 3);
    List<UserRanking> userRankings = result.getRankings();

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