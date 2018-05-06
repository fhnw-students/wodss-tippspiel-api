package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.GamePhase;
import ch.fhnw.edu.wodss.tippspielapi.service.I18NService;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Data
public class GamePhaseDto {

    private Long id;
    private String name;

    public GamePhaseDto() {}

    public GamePhaseDto(GamePhase gamePhase, Locale locale, I18NService i18NService) {
        id = gamePhase.getId();
        name = i18NService.getLocalizedString(gamePhase.getCode(), locale)  ;
    }
}
