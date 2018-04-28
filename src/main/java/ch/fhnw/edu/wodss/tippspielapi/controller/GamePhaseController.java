package ch.fhnw.edu.wodss.tippspielapi.controller;

import java.util.Collection;
import ch.fhnw.edu.wodss.tippspielapi.model.GamePhase;
import ch.fhnw.edu.wodss.tippspielapi.persistence.GamePhaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game-phases")
public class GamePhaseController {

    @Autowired
    private GamePhaseRepository gamePhaseRepository;

    @GetMapping
    @CrossOrigin
    @Secured({"ROLE_USER"})
    public Collection<GamePhase> getAllGamePhases() {
        return gamePhaseRepository.findAll();
    }

}
