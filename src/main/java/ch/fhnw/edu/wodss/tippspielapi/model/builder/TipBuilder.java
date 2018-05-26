package ch.fhnw.edu.wodss.tippspielapi.model.builder;

import ch.fhnw.edu.wodss.tippspielapi.model.Game;
import ch.fhnw.edu.wodss.tippspielapi.model.Tip;
import ch.fhnw.edu.wodss.tippspielapi.model.User;

public class TipBuilder {

  private User user;
  private Game game;
  private Integer points;

  public TipBuilder withUser(User user) {
    this.user = user;
    return this;
  }

  public TipBuilder withGame(Game game) {
    this.game = game;
    return this;
  }

  public TipBuilder withPoints(Integer points) {
    this.points = points;
    return this;
  }

  public Tip build() {
    Tip tip = new Tip();
    tip.setGame(game);
    tip.setUser(user);
    tip.setPoints(points);
    return tip;
  }
}
