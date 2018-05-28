package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.Team;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class NewTeamDto {

    @NotNull
    private String name;

    public NewTeamDto(){ }

}
