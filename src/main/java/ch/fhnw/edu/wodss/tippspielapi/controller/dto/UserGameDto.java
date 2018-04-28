package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.*;
import lombok.Data;

import java.util.Date;

@Data
public class UserGameDto {

    private Long id;
    private Date date;
    private Location location;
    private GamePhase phase;
    private Nation home;
    private Nation guest;
    private Integer hostScore;
    private Integer guestScore;
    private UserGameTipDto tip;

    public UserGameDto() {

    }

    public UserGameDto(Game game, Tip tip) {
        id = game.getId();
        date = game.getDate();
        location = game.getLocation();
        phase = game.getPhase();
        home = game.getHome();
        guest = game.getGuest();
        hostScore = game.getHostScore();
        guestScore = game.getGuestScore();

        if(tip != null) {
            this.tip = new UserGameTipDto(tip);
        }
    }

}
