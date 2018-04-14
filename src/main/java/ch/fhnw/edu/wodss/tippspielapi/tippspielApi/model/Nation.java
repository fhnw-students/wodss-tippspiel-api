package ch.fhnw.edu.wodss.tippspielapi.tippspielApi.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Data
public class Nation {

    @Id
    @Size(min = 2, max = 3)
    @Column(length = 3)
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    public enum TournamentGroup {
        A, B, C, D, E, F, G, H
    }

    @Enumerated(EnumType.STRING)
    private TournamentGroup tournamentGroup;

}
