package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.Team;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class NewTeamDto {

    @NotNull
    private String name;

    public NewTeamDto(){ }

}
