package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.Game;
import ch.fhnw.edu.wodss.tippspielapi.model.Tip;
import lombok.Data;

@Data
public class UserGameTipDto {

    private Integer hostScore;
    private Integer guestScore;

    public UserGameTipDto() {

    }

    public UserGameTipDto(Tip tip) {
        hostScore = tip.getHostScore();
        guestScore = tip.getGuestScore();
    }
}
