package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.TeamMate;
import ch.fhnw.edu.wodss.tippspielapi.model.User;
import lombok.Data;

@Data
public class TeamMateDto {

    private Long id;
    private String username;
    private boolean owner;

    public TeamMateDto(User user, TeamMate teamMate) {
        id = user.getId();
        username = user.getUsername();
        owner = teamMate.isOwner();
    }
}
