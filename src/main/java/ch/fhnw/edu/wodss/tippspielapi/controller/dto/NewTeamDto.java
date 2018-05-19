package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.Team;
import lombok.Data;

@Data
public class NewTeamDto {

    private String name;

    public NewTeamDto(){ }

}
