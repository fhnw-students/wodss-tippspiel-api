package ch.fhnw.edu.wodss.tippspielapi.persistence;

import ch.fhnw.edu.wodss.tippspielapi.model.Team;
import ch.fhnw.edu.wodss.tippspielapi.model.TeamMate;
import ch.fhnw.edu.wodss.tippspielapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamMateRepository extends JpaRepository<TeamMate, String> {

    Optional<TeamMate> findByUserAndTeam(User user, Team team);

    void deleteByUserIdAndTeam(Long userID, Team team);

    List<TeamMate> findByTeam(Team team);

    void deleteByTeam(Team team);

}
