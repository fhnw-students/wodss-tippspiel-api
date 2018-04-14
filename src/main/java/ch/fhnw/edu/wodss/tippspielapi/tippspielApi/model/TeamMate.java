package ch.fhnw.edu.wodss.tippspielapi.tippspielApi.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class TeamMate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Team team;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "bit(1) DEFAULT b'0'")
    private Boolean isOwner;

}