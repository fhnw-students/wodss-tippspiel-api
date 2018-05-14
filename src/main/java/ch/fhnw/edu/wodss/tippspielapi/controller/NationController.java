package ch.fhnw.edu.wodss.tippspielapi.controller;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.GamePhaseDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.NationDto;
import ch.fhnw.edu.wodss.tippspielapi.model.GamePhase;
import ch.fhnw.edu.wodss.tippspielapi.model.Nation;
import ch.fhnw.edu.wodss.tippspielapi.persistence.GamePhaseRepository;
import ch.fhnw.edu.wodss.tippspielapi.persistence.NationRepository;
import ch.fhnw.edu.wodss.tippspielapi.service.I18NService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/nations")
public class NationController {

    @Autowired
    private NationRepository nationRepository;

    @Autowired
    private I18NService i18NService;

    @GetMapping
    @CrossOrigin
    @Secured({"ROLE_USER"})
    public ResponseEntity getAll(Locale locale) {
        List<Nation> nations = nationRepository.findAll();
        List<NationDto> body = nations.stream()
                .map(nation -> new NationDto(nation, locale, i18NService))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(body);
    }

}
