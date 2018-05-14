package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.Tip;
import lombok.Data;

@Data
public class TipDto {

    private Integer hostScore;
    private Integer guestScore;

    private boolean tippedHostScoreCorrectly;
    private boolean tippedGuestScoreCorrectly;
    private boolean tippedWinnerCorrectly;
    private boolean tippedBalanceAndWinnerCorrectly;

    private Integer points;

    public TipDto() {}

    public TipDto(Tip tip) {
        hostScore = tip.getHostScore();
        guestScore = tip.getGuestScore();

        tippedHostScoreCorrectly = tip.isTippedHostScoreCorrectly();
        tippedGuestScoreCorrectly = tip.isTippedGuestScoreCorrectly();
        tippedWinnerCorrectly = tip.isTippedWinnerCorrectly();
        tippedBalanceAndWinnerCorrectly = tip.isTippedBalanceAndWinnerCorrectly();

        points = tip.getPoints();
    }

}
