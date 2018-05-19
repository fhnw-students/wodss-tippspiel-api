package ch.fhnw.edu.wodss.tippspielapi.model;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.NewTeamDto;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public Team(NewTeamDto newTeamDto) {
        name = newTeamDto.getName();
    }

    public Team() {
    }

}
