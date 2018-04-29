package ch.fhnw.edu.wodss.tippspielapi.persistence;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.UserTeamDto;
import ch.fhnw.edu.wodss.tippspielapi.model.Team;
import ch.fhnw.edu.wodss.tippspielapi.model.Tip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    //join TeamMate tm on t.id = tm.team.id where tm.user.id = :userID
    @Query("select new ch.fhnw.edu.wodss.tippspielapi.controller.dto.UserTeamDto(t.id,t.name,0) from Team t join TeamMate tm on t.id = tm.team.id where tm.user.id = :userId")
    List<UserTeamDto> findAllTeamsByUserId(@Param("userId") Long userId);

}
