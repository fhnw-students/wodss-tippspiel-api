package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.TeamInvitation;
import lombok.Data;

@Data
public class TeamInvitationDto {

    private long id;
    private String email;
    private TeamDto team;

    public TeamInvitationDto(TeamInvitation teamInvitation) {
        id = teamInvitation.getId();
        email = teamInvitation.getEmail();
        team = new TeamDto(teamInvitation.getTeam());
    }
}
