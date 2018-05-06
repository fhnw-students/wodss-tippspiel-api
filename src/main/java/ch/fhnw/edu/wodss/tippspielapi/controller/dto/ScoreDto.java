package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import lombok.Data;

@Data
public class ScoreDto {

    private Integer hostScore;
    private Integer guestScore;

    public ScoreDto() {}

}
