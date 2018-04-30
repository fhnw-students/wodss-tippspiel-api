package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.Tip;
import lombok.Data;

@Data
public class UserGameTipDto {

    private Integer hostScore;
    private Integer guestScore;
    private Boolean ruleHostScore;
    private Boolean ruleGuestScore;
    private Boolean ruleWinner;
    private Boolean ruleBalanceAndWinner;
    private Integer points;

    public UserGameTipDto(Tip tip) {
        hostScore = tip.getHostScore();
        guestScore = tip.getGuestScore();
        ruleHostScore = tip.getRuleBalanceAndWinner();
        ruleGuestScore = tip.getRuleGuestScore();
        ruleWinner = tip.getRuleWinner();
        ruleBalanceAndWinner = tip.getRuleBalanceAndWinner();
        points = tip.getPoints();
    }

}
