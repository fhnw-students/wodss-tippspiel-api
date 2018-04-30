package ch.fhnw.edu.wodss.tippspielapi.controller;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.GamePhaseDto;
import ch.fhnw.edu.wodss.tippspielapi.model.GamePhase;
import ch.fhnw.edu.wodss.tippspielapi.persistence.GamePhaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity getAllGamePhases() {
        List<GamePhase> gamePhases = gamePhaseRepository.findAll();
        List<GamePhaseDto> body = gamePhases.stream()
                .map(GamePhaseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(body);
    }

}
