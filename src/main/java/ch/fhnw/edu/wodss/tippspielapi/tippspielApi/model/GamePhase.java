package ch.fhnw.edu.wodss.tippspielapi.tippspielApi.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class GamePhase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

}
