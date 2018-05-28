package ch.fhnw.edu.wodss.tippspielapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UniqueTeamNameException extends RuntimeException {
    public UniqueTeamNameException() {
        super("UniqueTeamNameException: Teamname must be unique");
    }
}
