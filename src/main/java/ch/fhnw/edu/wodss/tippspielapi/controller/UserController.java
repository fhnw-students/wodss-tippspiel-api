package ch.fhnw.edu.wodss.tippspielapi.controller;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.UserDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.UserGameDto;
import ch.fhnw.edu.wodss.tippspielapi.model.Game;
import ch.fhnw.edu.wodss.tippspielapi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private GameService gameService;

    @Autowired
    private AuthenticationService authenticationService;

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
    public ResponseEntity getGamesOfCurrentUser() {
        User user = authenticationService.getCurrentUser();
        List<UserGameDto> games = gameService.getGamesByUserId(user.getId());
        return ResponseEntity.ok().body(games);
    }
    
}
