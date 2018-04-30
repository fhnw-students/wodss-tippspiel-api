package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.Game;
import ch.fhnw.edu.wodss.tippspielapi.model.Tip;
import ch.fhnw.edu.wodss.tippspielapi.model.TippedGame;
import ch.fhnw.edu.wodss.tippspielapi.service.I18NService;
import lombok.Data;

import java.util.Date;
import java.util.Locale;

@Data
public class TippedGameDto {

    private Long id;
    private Date date;

    private GamePhaseDto phase;
    private LocationDto location;

    private GameNationDto host;
    private GameNationDto guest;

    private TipDto tip;

    public TippedGameDto(TippedGame tippedGame, Locale locale, I18NService i18NService) {
        id = tippedGame.getId();
        date = tippedGame.getDate();

        phase = new GamePhaseDto(tippedGame.getPhase(), locale, i18NService);
        location = new LocationDto(tippedGame.getLocation(), locale, i18NService);

        host = new GameNationDto(tippedGame.getHost(), tippedGame.getHostScore(), locale, i18NService);
        guest = new GameNationDto(tippedGame.getGuest(), tippedGame.getGuestScore(), locale, i18NService);

        if (tippedGame.getTip() != null) {
            this.tip = new TipDto(tippedGame.getTip());
        }
    }

}
