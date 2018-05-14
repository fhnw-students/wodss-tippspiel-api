package ch.fhnw.edu.wodss.tippspielapi.persistence;

import ch.fhnw.edu.wodss.tippspielapi.model.Tip;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RankingRepository extends JpaRepository<Tip, Long> {

  @Query(nativeQuery = true, value = "select"
      + " user.username as username,"
      + " user.id as userId,"
      + " sum(tip.points) as points,"
      + " count(tip.points) as games"
      + " from tip"
      + " left join user on tip.user_id = user.id"
      + " group by user.username, user.id"
      + " order by points desc, user.username asc", countQuery = "limit")
  List<UserRanking> getUserRanking(int offset, int limit);

  static interface UserRanking {
    String getUsername();
    Long getUserId();
    int getPoints();
    int getGames();
  }
}
