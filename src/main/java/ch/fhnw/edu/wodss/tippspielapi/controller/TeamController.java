package ch.fhnw.edu.wodss.tippspielapi.controller;


import ch.fhnw.edu.wodss.tippspielapi.controller.dto.*;
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
import java.util.Locale;

@RestController
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamInvitationService teamInvitationService;

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/{teamId}")
    @CrossOrigin
    @Secured({"ROLE_USER"})
    public ResponseEntity findById(@PathVariable String teamId) {
        TeamDto teamDto = teamService.findById(Long.parseLong(teamId));
        return ResponseEntity.ok().body(teamDto);
    }

    @GetMapping("/{teamId}/users")
    @CrossOrigin
    @Secured({"ROLE_USER"})
    public ResponseEntity findAllTeamMates(@PathVariable String teamId) {
        List<TeamMateDto> teamDtos = teamService.findAllTeamMates(Long.parseLong(teamId));
        return ResponseEntity.ok().body(teamDtos);
    }

    @PostMapping
    @CrossOrigin
    @Secured({"ROLE_USER"})
    public ResponseEntity createTeam(@Valid @RequestBody NewTeamDto newTeamDto) {
        TeamDto teamDto = teamService.create(newTeamDto);
        return ResponseEntity.ok().body(teamDto);
    }

    @PutMapping("/{teamId}")
    @CrossOrigin
    @Secured({"ROLE_USER"})
    public ResponseEntity updateTeam(@PathVariable String teamId,@RequestBody NewTeamDto newTeamDto) {
        User user = authenticationService.getCurrentUser();
        TeamDto teamDto = teamService.update(Long.parseLong(teamId), newTeamDto, user);
        return ResponseEntity.ok().body(teamDto);

    }

    @DeleteMapping("/{teamId}/user/{userId}")
    @CrossOrigin
    @Secured({"ROLE_USER"})
    public ResponseEntity deleteUserFromTeam(@PathVariable String teamId, @PathVariable String userId) {
        User user = authenticationService.getCurrentUser();
        teamService.deleteFromTeam(Long.parseLong(userId), Long.parseLong(teamId), user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{teamId}/user")
    @CrossOrigin
    @Secured({"ROLE_USER"})
    public ResponseEntity inviteUserToTeam(@PathVariable String teamId, @RequestParam("email") String email, Locale locale) {
        User user = authenticationService.getCurrentUser();
        TeamInvitationDto teamInvitationDto = teamInvitationService.create(Long.parseLong(teamId), email, user, locale);
        return ResponseEntity.ok().body(teamInvitationDto);
    }

}
