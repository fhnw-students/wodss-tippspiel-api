package ch.fhnw.edu.wodss.tippspielapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ToLateException extends RuntimeException {

    public ToLateException() {
        super("Game has already started or is played!");
    }

}