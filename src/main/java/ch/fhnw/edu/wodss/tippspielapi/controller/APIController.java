package ch.fhnw.edu.wodss.tippspielapi.controller;

import ch.fhnw.edu.wodss.tippspielapi.model.APIInfo;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class APIController {

  @CrossOrigin
  @GetMapping
  public APIInfo getAPIInfo() {
    return new APIInfo("wodss-tippspiel-api", "0.0.1");
  }

  @Secured({"ROLE_ADMIN"})
  @CrossOrigin
  @GetMapping("2")
  public APIInfo getAPIInfo2() {
    return new APIInfo("wodss-tippspiel-api2", "0.0.1");
  }

}