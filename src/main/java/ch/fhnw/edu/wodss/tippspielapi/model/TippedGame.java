package ch.fhnw.edu.wodss.tippspielapi.model;

import lombok.Data;

import java.util.Date;

@Data
public class TippedGame {

  private Long id;
  private Date date;
  private Location location;
  private GamePhase phase;
  private Nation host;
  private Nation guest;
  private Integer hostScore;
  private Integer guestScore;
  private Integer hostWinsPercentage;
  private Integer guestWinsPercentage;
  private Integer drawPercentage;

  private Tip tip;

  public TippedGame(Game game, Tip tip) {
    id = game.getId();
    date = game.getDate();
    phase = game.getPhase();
    location = game.getLocation();
    host = game.getHost();
    guest = game.getGuest();
    hostScore = game.getHostScore();
    guestScore = game.getGuestScore();

    if (tip != null) {
      this.tip = tip;
    }

    hostWinsPercentage = 0;
    guestWinsPercentage = 0;
    drawPercentage = 0;
  }

  public void increaseGuestWinsPercentage() {
    if (guestWinsPercentage != null) {
      this.guestWinsPercentage++;
    } else {
      guestWinsPercentage = 1;
    }
  }

  public void increaseHostWinsPercentage() {
    if (hostWinsPercentage != null) {
      this.hostWinsPercentage++;
    } else {
      hostWinsPercentage = 1;
    }
  }

  public void increaseDrawPercentage() {
    if (drawPercentage != null) {
      this.drawPercentage++;
    } else {
      drawPercentage = 1;
    }
  }
}
