package ch.fhnw.edu.wodss.tippspielapi.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class TeamInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

}
