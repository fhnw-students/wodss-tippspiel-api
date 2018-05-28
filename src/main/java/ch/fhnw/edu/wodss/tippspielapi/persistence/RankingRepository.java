package ch.fhnw.edu.wodss.tippspielapi.persistence;

import ch.fhnw.edu.wodss.tippspielapi.model.Tip;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RankingRepository extends JpaRepository<Tip, Long> {

    @Query(nativeQuery = true, value = "SELECT"
            + " user.username AS username,"
            + " user.email AS email,"
            + " user.id AS userId,"
            + " sum(tip.points) AS points,"
            + " count(tip.points) AS games"
            + " FROM tip tip"
            + " INNER JOIN user ON tip.user_id = user.id"
            + " GROUP BY username, user.id"
            + " ORDER BY points DESC, games DESC, username ASC",
            countQuery = "SELECT count(DISTINCT user_id) FROM tip")
    Page<UserRankingInformation> getUserRankingInformation(Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT"
            + " user.username AS username,"
            + " user.email AS email,"
            + " user.id AS userId,"
            + " sum(tip.points) AS points,"
            + " count(tip.points) AS games"
            + " FROM tip tip"
            + " INNER JOIN user ON tip.user_id = user.id"
            + " GROUP BY username, user.id"
            + " ORDER BY points DESC, games DESC, username ASC",
            countQuery = "SELECT count(DISTINCT user_id) FROM tip")
    List<UserRankingInformation> getAllUserRankingInformation();

    @Query(nativeQuery = true, value = "SELECT"
            + " user.username AS username,"
            + " user.email AS email,"
            + " user.id AS userId,"
            + " sum(tip.points) AS points,"
            + " count(tip.points) AS games"
            + " FROM tip tip"
            + " INNER JOIN user ON tip.user_id = user.id"
            + " INNER JOIN team_mate ON team_mate.user_id = user.id"
            + " WHERE team_mate.team_id = :teamId"
            + " GROUP BY username, user.id"
            + " ORDER BY points DESC, games DESC, username ASC",
            countQuery = "SELECT count(DISTINCT tip.user_id)"
                    + " FROM tip"
                    + " INNER JOIN team_mate ON team_mate.user_id = tip.user_id"
                    + " WHERE team_mate.team_id = :teamId")
    Page<UserRankingInformation> getTeamUserRankingInformation(@Param("teamId") long teamId, Pageable pageable);

    // We only use this class in the service to build the actual dto.
    interface UserRankingInformation {

        String getUsername();

        String getEmail();

        Long getUserId();

        int getPoints();

        int getGames();
    }

    @Query(nativeQuery = true, value = "SELECT"
            + " team.name AS name,"
            + " team.id AS teamId,"
            + " sum(tip.points) AS points,"
            + " count(tip.points) AS games"
            + " FROM tip tip"
            + " INNER JOIN user ON tip.user_id = user.id"
            + " INNER JOIN team_mate ON team_mate.user_id = user.id"
            + " INNER JOIN team ON team_mate.team_id = team.id"
            + " GROUP BY team.id"
            + " ORDER BY points DESC, games DESC, team.name ASC",
            countQuery = "SELECT count(DISTINCT tip.user_id)"
                    + " FROM tip"
                    + " INNER JOIN team_mate ON team_mate.user_id = tip.user_id"
                    + " INNER JOIN team ON team_mate.team_id = team.id"
                    + " GROUP BY team.id")
    Page<TeamRankingInformation> getTeamRankingInformation(Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT"
            + " team.name AS name,"
            + " team.id AS teamId,"
            + " sum(tip.points) AS points,"
            + " count(tip.points) AS games"
            + " FROM tip tip"
            + " INNER JOIN user ON tip.user_id = user.id"
            + " INNER JOIN team_mate ON team_mate.user_id = user.id"
            + " INNER JOIN team ON team_mate.team_id = team.id"
            + " GROUP BY team.id"
            + " ORDER BY points DESC, games DESC, team.name ASC",
            countQuery = "SELECT count(DISTINCT tip.user_id)"
                    + " FROM tip"
                    + " INNER JOIN team_mate ON team_mate.user_id = tip.user_id"
                    + " INNER JOIN team ON team_mate.team_id = team.id"
                    + " GROUP BY team.id")
    List<TeamRankingInformation> getAllTeamRankingInformation();

    // We only use this class in the service to build the actual dto.
    interface TeamRankingInformation {

        String getName();

        Long getTeamId();

        int getPoints();

        int getGames();
    }
}
