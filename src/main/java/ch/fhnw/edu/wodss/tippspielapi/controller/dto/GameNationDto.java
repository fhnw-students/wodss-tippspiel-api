package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.Nation;
import lombok.Data;

@Data
public class GameNationDto {

    private NationDto nation;
    private Integer score;

    public GameNationDto(Nation nation, Integer score){
        this.nation = new NationDto(nation);
        this.score = score;
    }

}
