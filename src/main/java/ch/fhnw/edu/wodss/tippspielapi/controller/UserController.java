package ch.fhnw.edu.wodss.tippspielapi.controller;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.*;
import ch.fhnw.edu.wodss.tippspielapi.model.TippedGame;
import ch.fhnw.edu.wodss.tippspielapi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private GameService gameService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamInvitationService teamInvitationService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private I18NService i18NService;

    @Secured({"ROLE_USER"})
    @CrossOrigin
    @GetMapping("/me")
    public UserDto getCurrentUser() {
        User user = authenticationService.getCurrentUser();
        UserDto userDto = new UserDto(user);
        return userDto;
    }

    @Secured({"ROLE_USER"})
    @CrossOrigin
    @GetMapping(path = "/{username}/games")
    public ResponseEntity getGamesOfUser(@PathVariable String username, Locale locale) {
        List<TippedGame> games = gameService.getGamesByUsername(username);
        List<TippedGameDto> tippedGameDtos = games.stream()
                .map(game -> new TippedGameDto(game, locale, i18NService))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(tippedGameDtos);
    }

    @Secured({"ROLE_USER"})
    @CrossOrigin
    @GetMapping("/me/teams")
    public ResponseEntity getTeamsOfCurrentUser() {
        User user = authenticationService.getCurrentUser();
        List<UserTeamDto> teams = teamService.getTeamsByUserId(user.getId());
        return ResponseEntity.ok().body(teams);
    }

    @Secured({"ROLE_USER"})
    @CrossOrigin
    @GetMapping(path = "/me/team-invitations", params = {"page", "size"})
    public ResponseEntity getInvitationsOfTeamsOfCurrentUser(@RequestParam("page") int page, @RequestParam("size") int size) {
        User user = authenticationService.getCurrentUser();
        PageDto pageDto = teamInvitationService.getMyInvitations(user, page, size);
        return ResponseEntity.ok().body(pageDto);
    }

}
