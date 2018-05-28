package ch.fhnw.edu.wodss.tippspielapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TeamQuotaIsAchievedException extends RuntimeException {
    public TeamQuotaIsAchievedException() {
        super("TeamQuatoIsAchievedException: User is already in 5 Teams.");
    }
}
