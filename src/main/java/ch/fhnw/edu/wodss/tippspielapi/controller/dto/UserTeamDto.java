package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.Team;
import ch.fhnw.edu.wodss.tippspielapi.model.TeamMate;
import lombok.Data;

@Data
public class UserTeamDto {
    private Long id;
    private String name;
    private Integer averagePoints;
    private boolean owner;

    public UserTeamDto(Team team, TeamMate teamMate) {
        this.id = team.getId();
        this.name = team.getName();
        this.owner = teamMate.isOwner();
        this.averagePoints = 0;
    }
}

