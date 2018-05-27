package ch.fhnw.edu.wodss.tippspielapi.persistence;

import ch.fhnw.edu.wodss.tippspielapi.model.Team;
import ch.fhnw.edu.wodss.tippspielapi.model.TeamInvitation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamInvitationRepository extends JpaRepository<TeamInvitation, Long> {

    TeamInvitation findByTeamAndEmail(Team team, String email);

    Page<TeamInvitation> findByEmail(String email, Pageable pageRequest);

//    Page<Todo> findAll(Pageable pageRequest);

    void deleteByTeam(Team team);
}
