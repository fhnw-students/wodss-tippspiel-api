package ch.fhnw.edu.wodss.tippspielapi.tippspielApi.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Tip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    private Integer hostScore;
    private Integer guestScore;

}
