package ch.fhnw.edu.wodss.tippspielapi.service;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.UserRankingDto;
import ch.fhnw.edu.wodss.tippspielapi.model.Tip;
import ch.fhnw.edu.wodss.tippspielapi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.persistence.TipRepository;
import ch.fhnw.edu.wodss.tippspielapi.persistence.UserRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RankingService {

  @Autowired
  private TipRepository tipRepository;

  @Autowired
  private UserRepository userRepository;

  /**
   * Determines the current ranking using the tips from the database
   *
   * @return a {@link List< UserRankingDto >} containing the ranking of each user by its tips
   */
  public List<UserRankingDto> generateRanking() {
    List<UserRankingDto> ranking = createRankingDtos();

    // in case of same scores, we sort using username
    ranking.sort(
        Comparator.comparing(userRankingDto -> userRankingDto.getUser().getUsername()));

    // then we sort using score
    ranking.sort(Comparator.comparing(UserRankingDto::getPoints).reversed());
    addConcreteRankNumberToRankings(ranking);
    return ranking;
  }

  private void addConcreteRankNumberToRankings(List<UserRankingDto> ranking) {
    for (int i = 0; i < ranking.size(); i++) {
      // we start off with rank 1 instead of 0
      int rank = i + 1;
      ranking.get(i).setRank(rank);
    }
  }

  private List<UserRankingDto> createRankingDtos() {
    List<UserRankingDto> ranking = new ArrayList<>();
    List<User> allUsers = userRepository.findAll();
    allUsers.forEach(user -> {
      UserRankingDto userRanking = createUserRanking(user);
      ranking.add(userRanking);
    });
    return ranking;
  }

  private UserRankingDto createUserRanking(User user) {
    List<Tip> usersTips = tipRepository.findByUserId(user.getId());
    int tipScore = usersTips.stream().mapToInt(Tip::getPoints).sum();

    // every game can only have one tip
    int amountOfGames = usersTips.size();
    return new UserRankingDto(user, tipScore, amountOfGames);
  }
}
