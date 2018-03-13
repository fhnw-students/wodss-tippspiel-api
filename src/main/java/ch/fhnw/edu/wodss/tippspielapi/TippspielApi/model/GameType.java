package ch.fhnw.edu.wodss.tippspielapi.TippspielApi.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class GameType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

}
