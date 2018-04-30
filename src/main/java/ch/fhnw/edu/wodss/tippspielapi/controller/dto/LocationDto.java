package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.Location;
import lombok.Data;

@Data
public class LocationDto {

    private Long id;
    private String name;
    private Integer utcDiff;

    public LocationDto(Location location){
        // TODO: Add translation
        name = location.getCode();
        id = location.getId();
        utcDiff = location.getUtcDiff();
    }

}
