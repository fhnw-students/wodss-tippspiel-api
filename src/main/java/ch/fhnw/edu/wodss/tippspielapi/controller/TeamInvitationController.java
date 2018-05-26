package ch.fhnw.edu.wodss.tippspielapi.controller;


import ch.fhnw.edu.wodss.tippspielapi.controller.dto.NewTeamDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.TeamDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.TeamInvitationDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.TeamMateDto;
import ch.fhnw.edu.wodss.tippspielapi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.service.AuthenticationService;
import ch.fhnw.edu.wodss.tippspielapi.service.TeamInvitationService;
import ch.fhnw.edu.wodss.tippspielapi.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/team-invitations")
public class TeamInvitationController {

    @Autowired
    private TeamInvitationService teamInvitationService;

    @Autowired
    private AuthenticationService authenticationService;

    @DeleteMapping("/{teamInvitationId}")
    @CrossOrigin
    @Secured({"ROLE_USER"})
    public ResponseEntity delete(@PathVariable String teamInvitationId) {
        User currentUser = authenticationService.getCurrentUser();
        teamInvitationService.delete(Long.parseLong(teamInvitationId), currentUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{teamInvitationId}")
    @CrossOrigin
    @Secured({"ROLE_USER"})
    public ResponseEntity accept(@PathVariable String teamInvitationId) {
        User currentUser = authenticationService.getCurrentUser();
        teamInvitationService.accept(Long.parseLong(teamInvitationId), currentUser);
        return ResponseEntity.ok().build();
    }

}
