package ch.fhnw.edu.wodss.tippspielapi.persistence;

import ch.fhnw.edu.wodss.tippspielapi.model.Game;
import ch.fhnw.edu.wodss.tippspielapi.model.TippedGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("select new ch.fhnw.edu.wodss.tippspielapi.model.TippedGame(g, t) " +
            "from Game g " +
            "left join Tip t on t.game.id = g.id and t.user.id = :userId")
    List<TippedGame> findAllTippedGamesByUserId(@Param("userId") Long userId);

    @Query("select new ch.fhnw.edu.wodss.tippspielapi.model.TippedGame(g, t) " +
            "from Game g left join Tip t on t.game.id = g.id " +
            "where t.id = :tipId")
    TippedGame findTippedGamesByUserId(@Param("tipId") Long tipId);

}
