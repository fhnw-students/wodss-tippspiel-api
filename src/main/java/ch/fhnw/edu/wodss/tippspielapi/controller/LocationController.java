package ch.fhnw.edu.wodss.tippspielapi.controller;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.GamePhaseDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.LocationDto;
import ch.fhnw.edu.wodss.tippspielapi.model.GamePhase;
import ch.fhnw.edu.wodss.tippspielapi.model.Location;
import ch.fhnw.edu.wodss.tippspielapi.persistence.GamePhaseRepository;
import ch.fhnw.edu.wodss.tippspielapi.persistence.LocationRepository;
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
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private I18NService i18NService;

    @GetMapping
    @CrossOrigin
    @Secured({"ROLE_USER"})
    public ResponseEntity getAll(Locale locale) {
        List<Location> locations = locationRepository.findAll();
        List<LocationDto> body = locations.stream()
                .map(location -> new LocationDto(location, locale, i18NService))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(body);
    }

}
