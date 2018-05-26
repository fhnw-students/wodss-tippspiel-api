package ch.fhnw.edu.wodss.tippspielapi.persistence;

import ch.fhnw.edu.wodss.tippspielapi.model.Tip;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RankingRepository extends JpaRepository<Tip, Long> {

  @Query(nativeQuery = true, value = "select"
      + " user.username as username,"
      + " user.id as userId,"
      + " sum(tip.points) as points,"
      + " count(tip.points) as games"
      + " from tip tip"
      + " left join user on tip.user_id = user.id"
      + " group by username, user.id"
      + " order by points desc, games desc, username asc",
      countQuery = "select count(distinct user_id) from tip")
  Page<UserRankingInformation> getUserRankingInformation(Pageable pageable);

  @Query(value = "select count(distinct user_id) from tip", nativeQuery = true)
  int countUserRankings();


  // We only use this class in the service to build the actual dto.
  interface UserRankingInformation {

    String getUsername();

    Long getUserId();

    int getPoints();

    int getGames();
  }
}
