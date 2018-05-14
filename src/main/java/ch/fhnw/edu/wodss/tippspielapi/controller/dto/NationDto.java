package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.Nation;
import ch.fhnw.edu.wodss.tippspielapi.service.I18NService;
import lombok.Data;

import java.util.Locale;

@Data
public class NationDto {

    private String id;
    private String code;
    private String name;
    private Nation.TournamentGroup tournamentGroup;

    public NationDto() {}

    public NationDto(Nation nation, Locale locale, I18NService i18NService){
        id = nation.getId();
        code = nation.getCode();
        tournamentGroup = nation.getTournamentGroup();

        this.name = i18NService.getLocalizedString("country.name." + nation.getCode(), locale);
    }
}
