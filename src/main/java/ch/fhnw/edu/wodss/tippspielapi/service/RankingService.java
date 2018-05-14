package ch.fhnw.edu.wodss.tippspielapi.service;

import ch.fhnw.edu.wodss.tippspielapi.persistence.RankingRepository;
import ch.fhnw.edu.wodss.tippspielapi.persistence.RankingRepository.UserRanking;
import ch.fhnw.edu.wodss.tippspielapi.persistence.TipRepository;
import ch.fhnw.edu.wodss.tippspielapi.persistence.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RankingService {

  @Autowired
  private RankingRepository rankingRepository;

  /**
   * Determines the current ranking using the tips from the database
   *
   * @return a {@link List<UserRanking>} containing the ranking of each user by its tips
   */
  public List<UserRanking> generateRanking(int offset, int limit) {
    List<UserRanking> ranking = rankingRepository.getUserRanking(offset, limit);
    return ranking;
  }

}
