package ch.fhnw.edu.wodss.tippspielapi.TippspielApi.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, columnDefinition = "bit(1) DEFAULT b'0'")
    private Boolean isAdmin;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    Set<TeamMates> teamMates = new HashSet<>();

}
