package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.Nation;
import ch.fhnw.edu.wodss.tippspielapi.service.I18NService;
import lombok.Data;

import java.util.Locale;

@Data
public class GameNationDto {

    private NationDto nation;
    private Integer score;

    public GameNationDto(Nation nation, Integer score, Locale locale, I18NService i18NService){
        this.nation = new NationDto(nation, locale, i18NService);
        this.score = score;
    }

}
