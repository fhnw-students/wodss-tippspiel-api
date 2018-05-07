package ch.fhnw.edu.wodss.tippspielapi.controller;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.TippedGameDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.UserDto;
import ch.fhnw.edu.wodss.tippspielapi.model.TippedGame;
import ch.fhnw.edu.wodss.tippspielapi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping(path = "/me/games")
    public ResponseEntity getGamesOfCurrentUser(Locale locale) {
        User user = authenticationService.getCurrentUser();

        List<TippedGame> games = gameService.getGamesByUserId(user.getId());
        List<TippedGameDto> tippedGameDtos = games.stream()
                .map(game -> new TippedGameDto(game, locale, i18NService))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(tippedGameDtos);
    }

    @Secured({"ROLE_USER"})
    @CrossOrigin
    @GetMapping(path = "/{username}/games")
    public ResponseEntity getGamesOfUser(@PathVariable String username) {
        List<TippedGame> games = gameService.getGamesByUsername(username);
        return ResponseEntity.ok().body(games);
    }
    
}
