package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.User;
import lombok.Data;

@Data
public class UserRankingDto {

  private int points;
  private int rank;
  private int games;
  private UserDto user;

  public UserRankingDto() {
  }

  public UserRankingDto(User user, int points, int games) {
    this.user = new UserDto(user);
    this.points = points;
    this.games = games;
  }

}
