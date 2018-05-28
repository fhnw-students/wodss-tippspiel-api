package ch.fhnw.edu.wodss.tippspielapi.model;

import ch.fhnw.edu.wodss.tippspielapi.persistence.RankingRepository;
import ch.fhnw.edu.wodss.tippspielapi.persistence.RankingRepository.UserRankingInformation;
import lombok.Data;

@Data
public class TeamRanking {
  private String name;
  private Long teamId;
  private int points;
  private int games;
  private int rank;

  public TeamRanking(RankingRepository.TeamRankingInformation teamRankingInformation, int rank) {
    name = teamRankingInformation.getName();
    teamId = teamRankingInformation.getTeamId();
    points = teamRankingInformation.getPoints();
    games = teamRankingInformation.getGames();
    this.rank = rank;
  }

  public boolean contains(String name) {
    return this.name.toUpperCase().contains(name.toUpperCase());
  }

}
