package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.Game;
import ch.fhnw.edu.wodss.tippspielapi.service.I18NService;
import lombok.Data;

import java.util.Date;
import java.util.Locale;

@Data
public class GameDto {

    private Long id;
    private Date date;

    private GamePhaseDto phase;
    private LocationDto location;

    private GameNationDto host;
    private GameNationDto guest;

    public GameDto() {}

    public GameDto(Game game, Locale locale, I18NService i18NService) {
        id = game.getId();
        date = game.getDate();

        phase = new GamePhaseDto(game.getPhase(), locale, i18NService);
        location = new LocationDto(game.getLocation(), locale, i18NService);

        host = new GameNationDto(game.getHost(), game.getHostScore(), locale, i18NService);
        guest = new GameNationDto(game.getGuest(), game.getGuestScore(), locale, i18NService);
    }

}
