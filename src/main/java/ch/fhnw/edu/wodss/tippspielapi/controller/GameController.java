package ch.fhnw.edu.wodss.tippspielapi.controller;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.TipDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.UserGameDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.UserGameTipDto;
import ch.fhnw.edu.wodss.tippspielapi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.service.AuthenticationService;
import ch.fhnw.edu.wodss.tippspielapi.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private AuthenticationService authenticationService;

    @PutMapping("/{gameId}/tip")
    @CrossOrigin
    @Secured({"ROLE_USER"})
    public ResponseEntity enterTip(@PathVariable String gameId, @RequestBody TipDto tip) {
        User user = authenticationService.getCurrentUser();
        UserGameDto userGameDto = gameService.enterTip(Long.parseLong(gameId), user, tip);
        return ResponseEntity.ok().body(userGameDto);
    }

}
