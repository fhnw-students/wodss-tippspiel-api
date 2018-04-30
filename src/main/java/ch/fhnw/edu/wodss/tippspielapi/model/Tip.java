package ch.fhnw.edu.wodss.tippspielapi.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Tip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    private Integer hostScore;
    private Integer guestScore;

    private Boolean ruleHostScore;
    private Boolean ruleGuestScore;
    private Boolean ruleWinner;
    private Boolean ruleBalanceAndWinner;

    private Integer points;

}
