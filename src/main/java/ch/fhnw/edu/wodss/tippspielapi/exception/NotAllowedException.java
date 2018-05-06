package ch.fhnw.edu.wodss.tippspielapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class NotAllowedException extends RuntimeException {
    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    public NotAllowedException() {
        super("Not allowed!");
    }

}