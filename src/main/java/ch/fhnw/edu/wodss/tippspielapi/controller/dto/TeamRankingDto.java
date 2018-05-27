package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.TeamRanking;
import ch.fhnw.edu.wodss.tippspielapi.model.UserRanking;

import java.util.List;

public class TeamRankingDto {

    private List<TeamRanking> rankings;
    private int totalPages;

    public List<TeamRanking> getRankings() {
        return rankings;
    }

    public void setRankings(List<TeamRanking> rankings) {
        this.rankings = rankings;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}