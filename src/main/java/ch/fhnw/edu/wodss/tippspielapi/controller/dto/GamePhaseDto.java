package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.GamePhase;
import lombok.Data;

@Data
public class GamePhaseDto {

    private Long id;
    private String name;

    public GamePhaseDto(GamePhase gamePhase) {
        id = gamePhase.getId();

        // TODO: Add translation
        name = gamePhase.getCode();
    }
}
