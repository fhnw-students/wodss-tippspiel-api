package ch.fhnw.edu.wodss.tippspielapi.TippspielApi.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class TipRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private Integer points;

}
