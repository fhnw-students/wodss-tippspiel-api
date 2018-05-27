package ch.fhnw.edu.wodss.tippspielapi.model;

import ch.fhnw.edu.wodss.tippspielapi.persistence.RankingRepository.UserRankingInformation;
import lombok.Data;

@Data
public class UserRanking {
  private String username;
  private String email;
  private Long userId;
  private int points;
  private int games;
  private int rank;

  public UserRanking(UserRankingInformation userRankingInformation, int rank) {
    username = userRankingInformation.getUsername();
    email = userRankingInformation.getEmail();
    userId = userRankingInformation.getUserId();
    points = userRankingInformation.getPoints();
    games = userRankingInformation.getGames();
    this.rank = rank;
  }
}
