package ch.fhnw.edu.wodss.tippspielapi.controller;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.TeamRankingDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.UserRankingDto;
import ch.fhnw.edu.wodss.tippspielapi.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ranking")
public class RankingController {

    @Autowired
    private RankingService rankingService;

    @GetMapping("/users")
    @CrossOrigin
    @Secured({"ROLE_USER"})
    public ResponseEntity getUserRanking(@RequestParam("page") int page,
                                         @RequestParam("size") int size,
                                         @RequestParam(value = "username", required = false) String username) {
        UserRankingDto body = rankingService.generateUserRanking(username, page, size);
        return ResponseEntity.ok().body(body);
    }

    @GetMapping("/teams/{teamId}")
    @CrossOrigin
    @Secured({"ROLE_USER"})
    public ResponseEntity getTeamUserRanking(@PathVariable String teamId,
                                             @RequestParam("page") int page,
                                             @RequestParam("size") int size) {
        UserRankingDto body = rankingService.generateTeamUserRanking(Long.parseLong(teamId), page, size);
        return ResponseEntity.ok().body(body);
    }

    @GetMapping("/teams")
    @CrossOrigin
    @Secured({"ROLE_USER"})
    public ResponseEntity getTeamRanking(@RequestParam("page") int page,
                                         @RequestParam("size") int size) {
        TeamRankingDto body = rankingService.generateTeamRanking(page, size);
        return ResponseEntity.ok().body(body);
    }

}
