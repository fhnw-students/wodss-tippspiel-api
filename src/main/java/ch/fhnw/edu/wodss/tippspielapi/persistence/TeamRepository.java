package ch.fhnw.edu.wodss.tippspielapi.persistence;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.TeamMateDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.UserTeamDto;
import ch.fhnw.edu.wodss.tippspielapi.model.Team;
import ch.fhnw.edu.wodss.tippspielapi.model.Tip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("select new ch.fhnw.edu.wodss.tippspielapi.controller.dto.UserTeamDto(t, tm) " +
            "from Team t " +
            "join TeamMate tm on t.id = tm.team.id " +
            "where tm.user.id = :userId")
    List<UserTeamDto> findAllTeamsByUserId(@Param("userId") Long userId);

    @Query("select new ch.fhnw.edu.wodss.tippspielapi.controller.dto.TeamMateDto(u, tm) " +
            "from TeamMate tm " +
            "join User u on tm.user.id = u.id " +
            "where tm.team.id = :teamId")
    List<TeamMateDto> findAllTeamMatesByTeamId(@Param("teamId") Long teamID);
}
