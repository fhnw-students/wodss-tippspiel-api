package ch.fhnw.edu.wodss.tippspielapi.model;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_phase_id", nullable = false)
    private GamePhase phase;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "host_nation_id", nullable = false)
    private Nation host;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "guest_nation_id", nullable = false)
    private Nation guest;

    private Integer hostScore;
    private Integer guestScore;

}
