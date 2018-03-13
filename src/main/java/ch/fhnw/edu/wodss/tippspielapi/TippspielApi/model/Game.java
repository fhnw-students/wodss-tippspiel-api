package ch.fhnw.edu.wodss.tippspielapi.TippspielApi.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_type_id", nullable = false)
    private GameType type;

    @ManyToOne
    @JoinColumn(name = "home_nation_id", nullable = false)
    private Nation home;

    @ManyToOne
    @JoinColumn(name = "guest_nation_id", nullable = false)
    private Nation guest;

    private Integer homeScore;
    private Integer guestScore;

    @ManyToOne
    @JoinColumn(name = "stadium_id", nullable = false)
    private Stadium stadium;

    @Column(nullable = false, columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

}
