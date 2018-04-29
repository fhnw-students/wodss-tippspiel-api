package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import lombok.Data;

@Data
public class UserTeamDto {
    private Long id;
    private String name;
    private Integer averagePoints;

    public UserTeamDto(Long id, String name, Integer averagePoints){
        this.id = id;
        this.name = name;
        this.averagePoints = averagePoints;
    }
}

