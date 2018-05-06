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

        if(tip != null) {
            this.tip = tip;
        }
    }

}
