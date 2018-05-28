package ch.fhnw.edu.wodss.tippspielapi.controller;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.*;
import ch.fhnw.edu.wodss.tippspielapi.model.Game;
import ch.fhnw.edu.wodss.tippspielapi.model.Tip;
import ch.fhnw.edu.wodss.tippspielapi.model.User;
import ch.fhnw.edu.wodss.tippspielapi.service.AuthenticationService;
import ch.fhnw.edu.wodss.tippspielapi.service.GameService;
import ch.fhnw.edu.wodss.tippspielapi.service.I18NService;
import ch.fhnw.edu.wodss.tippspielapi.service.TipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private TipService tipService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private I18NService i18NService;

    @GetMapping()
    @CrossOrigin
    @Secured({"ROLE_USER"})
    public ResponseEntity getAll(Locale locale) {
        List<Game> games = gameService.getAll();

        List<GameDto> gameDtos = games.stream()
                .map(game -> new GameDto(game, locale, i18NService))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(gameDtos);
    }

    @PostMapping("")
    @CrossOrigin
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity create(@RequestBody NewGameDto newGameDto, Locale locale) {
        Game game = gameService.create(newGameDto);
        return ResponseEntity.ok().body(new GameDto(game, locale, i18NService));
    }

    @PutMapping("/{gameId}")
    @CrossOrigin
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity enterScore(@PathVariable String gameId, @RequestBody ScoreDto scoreDto, Locale locale) {
        Game game = gameService.enterScore(Long.parseLong(gameId), scoreDto);
        return ResponseEntity.ok().body(new GameDto(game, locale, i18NService));
    }

    @PutMapping("/{gameId}/tip")
    @CrossOrigin
    @Secured({"ROLE_USER"})
    public ResponseEntity enterTip(@PathVariable String gameId, @RequestBody ScoreDto scoreDto) {
        User user = authenticationService.getCurrentUser();
        Tip tip = tipService.enterTip(Long.parseLong(gameId), user, scoreDto);
        return ResponseEntity.ok().body(new TipDto(tip));
    }

    @DeleteMapping("/{gameId}")
    @CrossOrigin
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity delete(@PathVariable String gameId) {
        gameService.delete(Long.parseLong(gameId));
        return ResponseEntity.noContent().build();
    }

}
