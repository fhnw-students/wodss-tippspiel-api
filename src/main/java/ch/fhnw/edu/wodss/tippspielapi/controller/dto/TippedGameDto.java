package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.TippedGame;
import ch.fhnw.edu.wodss.tippspielapi.service.I18NService;
import java.util.Date;
import java.util.Locale;
import lombok.Data;

@Data
public class TippedGameDto {

    private Long id;
    private Date date;

    private GamePhaseDto phase;
    private LocationDto location;

    private GameNationDto host;
    private GameNationDto guest;

    private TipDto tip;

    private Integer hostWinsPercentage;
    private Integer guestWinsPercentage;
    private Integer drawPercentage;

    public TippedGameDto() {}

    public TippedGameDto(TippedGame tippedGame, Locale locale, I18NService i18NService) {
        id = tippedGame.getId();
        date = tippedGame.getDate();

        hostWinsPercentage = tippedGame.getHostWinsPercentage();
        guestWinsPercentage = tippedGame.getGuestWinsPercentage();
        drawPercentage = tippedGame.getDrawPercentage();

        phase = new GamePhaseDto(tippedGame.getPhase(), locale, i18NService);
        location = new LocationDto(tippedGame.getLocation(), locale, i18NService);

        host = new GameNationDto(tippedGame.getHost(), tippedGame.getHostScore(), locale, i18NService);
        guest = new GameNationDto(tippedGame.getGuest(), tippedGame.getGuestScore(), locale, i18NService);

        if (tippedGame.getTip() != null) {
            this.tip = new TipDto(tippedGame.getTip());
        }
    }

}
