package ch.fhnw.edu.wodss.tippspielapi.TippspielApi.model;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class TeamMates {

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
