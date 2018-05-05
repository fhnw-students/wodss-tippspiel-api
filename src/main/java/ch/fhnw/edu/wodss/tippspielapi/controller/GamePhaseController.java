package ch.fhnw.edu.wodss.tippspielapi.controller;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.GamePhaseDto;
import ch.fhnw.edu.wodss.tippspielapi.model.GamePhase;
import ch.fhnw.edu.wodss.tippspielapi.persistence.GamePhaseRepository;
import ch.fhnw.edu.wodss.tippspielapi.service.I18NService;
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

    @Autowired
    private I18NService i18NService;

    @GetMapping
    @CrossOrigin
    @Secured({"ROLE_USER"})
    public ResponseEntity getAllGamePhases(Locale locale) {
        List<GamePhase> gamePhases = gamePhaseRepository.findAll();
        List<GamePhaseDto> body = gamePhases.stream()
                .map(phase -> new GamePhaseDto(phase, locale, i18NService))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(body);
    }

}
