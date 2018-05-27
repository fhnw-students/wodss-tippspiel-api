package ch.fhnw.edu.wodss.tippspielapi.service;

import ch.fhnw.edu.wodss.tippspielapi.controller.dto.TeamRankingDto;
import ch.fhnw.edu.wodss.tippspielapi.controller.dto.UserRankingDto;
import ch.fhnw.edu.wodss.tippspielapi.model.TeamRanking;
import ch.fhnw.edu.wodss.tippspielapi.model.UserRanking;
import ch.fhnw.edu.wodss.tippspielapi.persistence.RankingRepository;
import ch.fhnw.edu.wodss.tippspielapi.persistence.RankingRepository.UserRankingInformation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    public UserRankingDto generateUserRanking(String username, int offset, int limit) {
        if (username != null && username.length() > 0) {
            List<UserRankingInformation> allRankingInformation = rankingRepository.getAllUserRankingInformation();

            List<UserRanking> userRankings = generateRanking(allRankingInformation, 0, 0, limit).getContent().stream()
                    .filter(u -> u.getUsername().contains(username)).collect(Collectors.toList());

            UserRankingDto userRankingDto = new UserRankingDto();
            userRankingDto.setContent(getListOfPage(userRankings, offset, limit));
            userRankingDto.setTotalPages(getTotalPages(userRankings.size(), limit));
            return userRankingDto;
        } else {
            Page<UserRankingInformation> rankingInformation = rankingRepository.getUserRankingInformation(PageRequest.of(offset, limit));
            return generateRanking(rankingInformation.getContent(), rankingInformation.getTotalPages(), offset, limit);
        }
    }

    public int getTotalPages(int size, int limit) {
        if (limit <= 0) {
            return 0;
        }
        int pageSize = size / limit;
        return pageSize + ((size % limit == 0) ? 0 : 1);
    }

    public List<UserRanking> getListOfPage(List<UserRanking> userRankings, int offset, int limit) {
        int totalPages = getTotalPages(userRankings.size(), limit);

        if (offset > totalPages || offset < 0 || totalPages == 0) {
            return Arrays.asList();
        }

        int start = offset * limit;
        int end = start + limit;

        if (offset == totalPages || end > userRankings.size()) {
            end = userRankings.size();
        }

        try {
            return userRankings.subList(start, end);
        } catch (Exception e) {
            return Arrays.asList();
        }
    }

    public UserRankingDto generateTeamUserRanking(long teamId, int offset, int limit) {
        Page<UserRankingInformation> rankingInformation = rankingRepository.getTeamUserRankingInformation(teamId, PageRequest.of(offset, limit));
        return generateRanking(rankingInformation.getContent(), rankingInformation.getTotalPages(), offset, limit);
    }

    public UserRankingDto generateRanking(List<UserRankingInformation> content, int totalPages, int offset, int limit) {
        List<UserRanking> rankings = new ArrayList<>();
        for (int i = 0; i < content.size(); i++) {
            int rank = (i + 1) + offset * limit;
            UserRanking userRanking = new UserRanking(content.get(i), rank);
            rankings.add(userRanking);
        }

        UserRankingDto userRankingDto = new UserRankingDto();
        userRankingDto.setContent(rankings);
        userRankingDto.setTotalPages(totalPages);
        return userRankingDto;
    }

    public TeamRankingDto generateTeamRanking(int offset, int limit) {
        Page<RankingRepository.TeamRankingInformation> rankingInformation = rankingRepository.getTeamRankingInformation(PageRequest.of(offset, limit));
        List<TeamRanking> rankings = new ArrayList<>();

        List<RankingRepository.TeamRankingInformation> content = rankingInformation.getContent();
        for (int i = 0; i < content.size(); i++) {
            int rank = (i + 1) + offset * limit;
            TeamRanking teamRanking = new TeamRanking(content.get(i), rank);
            rankings.add(teamRanking);
        }

        TeamRankingDto teamRankingDto = new TeamRankingDto();
        teamRankingDto.setContent(rankings);
        teamRankingDto.setTotalPages(rankingInformation.getTotalPages());
        return teamRankingDto;
    }
}
