package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.Nation;
import lombok.Data;

import java.util.Date;

@Data
public class NewGameDto {

    private Date date;

    private GamePhaseDto phase;
    private LocationDto location;

    private NationDto host;
    private NationDto guest;

    public NewGameDto() {}

}
