package ch.fhnw.edu.wodss.tippspielapi.persistence;

import ch.fhnw.edu.wodss.tippspielapi.model.GamePhase;
import ch.fhnw.edu.wodss.tippspielapi.model.Nation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NationRepository extends JpaRepository<Nation, String> {

}
