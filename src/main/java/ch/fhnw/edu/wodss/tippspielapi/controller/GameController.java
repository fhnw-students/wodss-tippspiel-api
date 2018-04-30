package ch.fhnw.edu.wodss.tippspielapi.controller;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.TipDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.TippedGameDto;
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

    @Autowired
    private I18NService i18NService;

    @PutMapping("/{gameId}/tip")
    @CrossOrigin
    @Secured({"ROLE_USER"})
    public ResponseEntity enterTip(@PathVariable String gameId, @RequestBody TipDto tip, Locale locale) {
        User user = authenticationService.getCurrentUser();
        TippedGame tippedGame = gameService.enterTip(Long.parseLong(gameId), user, tip);
        return ResponseEntity.ok().body(new TippedGameDto(tippedGame, locale, i18NService));
    }

}
