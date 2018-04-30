package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.Game;
import ch.fhnw.edu.wodss.tippspielapi.model.Tip;
import ch.fhnw.edu.wodss.tippspielapi.model.UserTipRule;
import lombok.Data;

@Data
public class UserGameTipDto {

    private Integer hostScore;
    private Integer guestScore;
    private Integer points;

    public UserGameTipDto(Tip tip) {
        hostScore = tip.getHostScore();
        guestScore = tip.getGuestScore();
        points = null;
    }

    public UserGameTipDto(Tip tip, UserTipRule userTipRule) {
        hostScore = tip.getHostScore();
        guestScore = tip.getGuestScore();
        points = userTipRule.getPoints();
    }
}
