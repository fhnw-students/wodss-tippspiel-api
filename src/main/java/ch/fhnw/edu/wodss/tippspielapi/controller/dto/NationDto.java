package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.Nation;
import lombok.Data;

@Data
public class NationDto {

    private String id;
    private String code;
    private String name;
    private Nation.TournamentGroup tournamentGroup;

    public NationDto(Nation nation){
        id = nation.getId();
        code = nation.getCode();
        tournamentGroup = nation.getTournamentGroup();

        // TODO: Add translation
        this.name = nation.getCode();

    }
}
