package ch.fhnw.edu.wodss.tippspielapi.persistence;

import ch.fhnw.edu.wodss.tippspielapi.model.GamePhase;
import ch.fhnw.edu.wodss.tippspielapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GamePhaseRepository extends JpaRepository<GamePhase, Long> {

}
