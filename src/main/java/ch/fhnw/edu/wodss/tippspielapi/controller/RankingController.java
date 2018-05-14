package ch.fhnw.edu.wodss.tippspielapi.controller;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.UserRankingDto;
import ch.fhnw.edu.wodss.tippspielapi.service.I18NService;
import ch.fhnw.edu.wodss.tippspielapi.service.RankingService;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ranking")
public class RankingController {

  @Autowired
  private I18NService i18NService;

  @Autowired
  private RankingService rankingService;

  @GetMapping("/users")
  @CrossOrigin
  @Secured({"ROLE_USER"})
  public ResponseEntity getUserRankings(Locale locale) {
    List<UserRankingDto> body = rankingService.generateRanking();
    return ResponseEntity.ok().body(body);
  }

}
