package ch.fhnw.edu.wodss.tippspielapi.controller;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.TipDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.TippedGameDto;
import ch.fhnw.edu.wodss.tippspielapi.model.Tip;
import ch.fhnw.edu.wodss.tippspielapi.model.TippedGame;
import ch.fhnw.edu.wodss.tippspielapi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.service.AuthenticationService;
import ch.fhnw.edu.wodss.tippspielapi.service.GameService;
import ch.fhnw.edu.wodss.tippspielapi.service.I18NService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

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
    public ResponseEntity enterTip(@PathVariable String gameId, @RequestBody TipDto tipDto) {
        User user = authenticationService.getCurrentUser();
        Tip tip = gameService.enterTip(Long.parseLong(gameId), user, tipDto);
        return ResponseEntity.ok().body(new TipDto(tip));
    }

}
