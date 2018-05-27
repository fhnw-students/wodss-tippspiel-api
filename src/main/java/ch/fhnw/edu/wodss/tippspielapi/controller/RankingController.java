package ch.fhnw.edu.wodss.tippspielapi.controller;

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
    public ResponseEntity getUserRanking(@RequestParam("offset") int offset,
                                         @RequestParam("limit") int limit) {
        UserRankingDto body = rankingService.generateUserRanking(offset, limit);
        return ResponseEntity.ok().body(body);
    }

    @GetMapping("/teams/{teamId}")
    @CrossOrigin
    @Secured({"ROLE_USER"})
    public ResponseEntity getTeamUserRanking(@PathVariable String teamId,
                                             @RequestParam("offset") int offset,
                                             @RequestParam("limit") int limit) {
        UserRankingDto body = rankingService.generateTeamUserRanking(Long.parseLong(teamId), offset, limit);
        return ResponseEntity.ok().body(body);
    }

//    @GetMapping("/teams")
//    @CrossOrigin
//    @Secured({"ROLE_USER"})
//    public ResponseEntity getTeamRanking(@RequestParam("offset") int offset,
//                                         @RequestParam("limit") int limit) {
//        UserRankingDto body = rankingService.generateTeamRanking(offset, limit);
//        return ResponseEntity.ok().body(body);
//    }

}
