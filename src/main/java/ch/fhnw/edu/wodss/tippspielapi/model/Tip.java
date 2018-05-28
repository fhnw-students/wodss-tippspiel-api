package ch.fhnw.edu.wodss.tippspielapi.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Tip {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "game_id", nullable = false)
  private Game game;

  private Integer hostScore;
  private Integer guestScore;

  private boolean tippedHostScoreCorrectly;
  private boolean tippedGuestScoreCorrectly;
  private boolean tippedWinnerCorrectly;
  private boolean tippedBalanceAndWinnerCorrectly;

  private Integer points;

  public boolean guestWins() {
    if (guestScore == null || hostScore == null) {
      return false;
    } else {
      return guestScore > hostScore;
    }
  }

  public boolean hostWins() {
    if (guestScore == null || hostScore == null) {
      return false;
    } else {
      return hostScore > guestScore;
    }
  }

  public boolean isFilledTip() {
    return guestScore != null && hostScore != null;
  }
}
