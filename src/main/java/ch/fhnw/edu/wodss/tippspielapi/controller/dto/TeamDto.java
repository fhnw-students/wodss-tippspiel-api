package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.Team;
import lombok.Data;

import java.util.List;

@Data
public class TeamDto {

    private Long id;
    private String name;

    public TeamDto(Team team){
        id = team.getId();
        name = team.getName();
    }

}
