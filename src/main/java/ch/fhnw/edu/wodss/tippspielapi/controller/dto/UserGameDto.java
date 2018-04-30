package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.*;
import lombok.Data;

import java.util.Date;

@Data
public class UserGameDto {

    private Long id;
    private Date date;
    private LocationDto location;
    private GamePhaseDto phase;
    private GameNationDto host;
    private GameNationDto guest;
    private UserGameTipDto tip;


    public UserGameDto(Game game, Tip tip) {
        id = game.getId();
        date = game.getDate();

        phase = new GamePhaseDto(game.getPhase());

        location = new LocationDto(game.getLocation());

        host = new GameNationDto(game.getHost(), game.getHostScore());
        guest = new GameNationDto(game.getGuest(), game.getGuestScore());

        if(tip != null) {
            this.tip = new UserGameTipDto(tip);
        }
    }

}
