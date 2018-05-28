package ch.fhnw.edu.wodss.tippspielapi.persistence;

import ch.fhnw.edu.wodss.tippspielapi.model.Tip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipRepository extends JpaRepository<Tip, Long> {

    Tip findByUserIdAndGameId(Long userId, Long gameId);

    List<Tip> findByGameId(Long gameId);

    List<Tip> findByUserId(Long userId);

}
