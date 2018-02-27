package ch.fhnw.edu.wodss.tippspielapi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class APIController {

    @CrossOrigin
    @GetMapping("")
    public APIInfo getAPIInfo() {
        return new APIInfo("wodss-tippspiel-web","0.0.0");
    }

}
