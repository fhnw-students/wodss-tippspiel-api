package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.Location;
import ch.fhnw.edu.wodss.tippspielapi.service.I18NService;
import lombok.Data;

import java.util.Locale;

@Data
public class LocationDto {

    private Long id;
    private String name;
    private Integer utcDiff;

    public LocationDto() {}

    public LocationDto(Location location, Locale locale, I18NService i18NService){
        id = location.getId();
        utcDiff = location.getUtcDiff();

        name = i18NService.getLocalizedString(location.getCode(), locale);
    }

}
