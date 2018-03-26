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

    @Column(nullable = false, columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @ManyToOne
    @JoinColumn(name = "game_phase_id", nullable = false)
    private GamePhase phase;

    @ManyToOne
    @JoinColumn(name = "home_nation_id", nullable = false)
    private Nation home;

    @ManyToOne
    @JoinColumn(name = "guest_nation_id", nullable = false)
    private Nation guest;

    private Integer hostScore;
    private Integer guestScore;

}
