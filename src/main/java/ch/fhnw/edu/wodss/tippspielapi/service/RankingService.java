package ch.fhnw.edu.wodss.tippspielapi.service;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.UserRankingDto;
import ch.fhnw.edu.wodss.tippspielapi.model.UserRanking;
import ch.fhnw.edu.wodss.tippspielapi.persistence.RankingRepository;
import ch.fhnw.edu.wodss.tippspielapi.persistence.RankingRepository.UserRankingInformation;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
  public UserRankingDto generateRanking(int offset, int limit) {
    Page<UserRankingInformation> rankingInformation = rankingRepository
        .getUserRankingInformation(PageRequest.of(offset, limit));
    List<UserRanking> rankings = new ArrayList<>();

    List<UserRankingInformation> content = rankingInformation.getContent();
    for (int i = 0; i < content.size(); i++) {
      int rank = (i + 1 ) + offset * limit;
      UserRanking userRanking = new UserRanking(content.get(i), rank);
      rankings.add(userRanking);
    }

    UserRankingDto userRankingDto = new UserRankingDto();
    userRankingDto.setRankings(rankings);
    userRankingDto.setTotalPages(rankingInformation.getTotalPages());
    return userRankingDto;
  }

}
