package ch.fhnw.edu.wodss.tippspielapi.service;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.UserRankingDto;
import ch.fhnw.edu.wodss.tippspielapi.model.UserRanking;
import ch.fhnw.edu.wodss.tippspielapi.persistence.RankingRepository;
import ch.fhnw.edu.wodss.tippspielapi.persistence.RankingRepository.UserRankingInformation;
import ch.fhnw.edu.wodss.tippspielapi.persistence.TipRepository;
import ch.fhnw.edu.wodss.tippspielapi.persistence.UserRepository;

import java.lang.reflect.Array;
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
    public void testRankingHappyPath(@Mocked Page<UserRankingInformation> pageMock) {
        List<UserRankingInformation> userRankingInformation = new ArrayList<>();
        userRankingInformation.add(createUserRankingInformation(2L, "hirsch", "", true, 50, 5));
        userRankingInformation.add(createUserRankingInformation(1L, "davu", "", true, 44, 5));
        userRankingInformation.add(createUserRankingInformation(3L, "ken", "", false, 35, 4));

        List<Integer> expectedScores = Arrays.asList(50, 44, 35);
        List<String> expectedNames = Arrays.asList("hirsch", "davu", "ken");
        List<Integer> expectedGames = Arrays.asList(5, 5, 4);

        UserRankingDto userRankingDto = rankingService.generateRanking(userRankingInformation, 0, 3, 9);
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
    public void testRankingWithoutUsers(@Mocked Page<UserRankingInformation> pageMock) {
        UserRankingDto userRankingDto = rankingService.generateRanking(Arrays.asList(), 0,5, 5);
        List<UserRanking> userRanking = userRankingDto.getRankings();

        Assert.assertTrue(userRanking.isEmpty());
    }

    @Test
    public void testRankingWithSameScores(@Mocked Page<UserRankingInformation> pageMock) {
        List<UserRankingInformation> userRankingInformation = new ArrayList<>();
        userRankingInformation.add(createUserRankingInformation(2L, "hirsch", "", true, 99, 9));
        userRankingInformation.add(createUserRankingInformation(1L, "davu", "",true, 99, 10));
        userRankingInformation.add(createUserRankingInformation(3L, "ken", "",false, 87, 10));

        List<Integer> expectedScores = Arrays.asList(99, 99, 87);
        List<String> expectedNames = Arrays.asList("hirsch", "davu", "ken");
        List<Integer> expectedGames = Arrays.asList(9, 10, 10);

        UserRankingDto result = rankingService.generateRanking(userRankingInformation, 3, 0, 3);
        List<UserRanking> userRankings = result.getRankings();

        for (int i = 0; i < userRankings.size(); i++) {
            UserRanking userRanking = userRankings.get(i);
            Assert.assertEquals(i + 1, userRanking.getRank());
            Assert.assertEquals(expectedScores.get(i).intValue(), userRanking.getPoints());
            Assert.assertEquals(expectedNames.get(i), userRanking.getUsername());
            Assert.assertEquals(expectedGames.get(i).intValue(), userRanking.getGames());
        }
    }

    @Test
    public void testGetTotalPages_HappyPaths() {
        Assert.assertEquals(0, rankingService.getTotalPages(0, 5));
        Assert.assertEquals(0, rankingService.getTotalPages(0, 0));
        Assert.assertEquals(0, rankingService.getTotalPages(5, 0));
        Assert.assertEquals(1, rankingService.getTotalPages(5, 5));
        Assert.assertEquals(2, rankingService.getTotalPages(6, 5));
        Assert.assertEquals(2, rankingService.getTotalPages(2, 1));
        Assert.assertEquals(1, rankingService.getTotalPages(2, 3));
    }

    @Test
    public void testGetListOfPage_HappyPaths() {
        List<UserRanking> emptyUserRankings = Arrays.asList();
        Assert.assertEquals(0, rankingService.getListOfPage(emptyUserRankings, 0, 0).size());
        Assert.assertEquals(0, rankingService.getListOfPage(emptyUserRankings, 1, 5).size());
        Assert.assertEquals(0, rankingService.getListOfPage(emptyUserRankings, 2, 1).size());
    }

    @Test
    public void testGetListOfPage_HappyPathsWithFilledArray() {
        List<UserRanking> userRankings = Arrays.asList(
                new UserRanking(createUserRankingInformation(1L, "", "", false, 0, 0), 1),
                new UserRanking(createUserRankingInformation(2L, "", "", false, 0, 0), 1)
        );
        Assert.assertEquals(0, rankingService.getListOfPage(userRankings, 0, 0).size());
        Assert.assertEquals(0, rankingService.getListOfPage(userRankings, 1, 5).size());
        Assert.assertEquals(1, rankingService.getListOfPage(userRankings, 0, 1).size());
        Assert.assertEquals(1, rankingService.getListOfPage(userRankings, 1, 1).size());
        Assert.assertEquals(0, rankingService.getListOfPage(userRankings, 2, 1).size());
        Assert.assertEquals(2, rankingService.getListOfPage(userRankings, 0, 2).size());
        Assert.assertEquals(2, rankingService.getListOfPage(userRankings, 0, 3).size());
    }

    private UserRankingInformation createUserRankingInformation(Long id, String username, String email,
                                                                boolean admin,
                                                                int points, int games) {
        return new UserRankingInformation() {
            @Override
            public String getUsername() {
                return username;
            }

            @Override
            public String getEmail() {
                return email;
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