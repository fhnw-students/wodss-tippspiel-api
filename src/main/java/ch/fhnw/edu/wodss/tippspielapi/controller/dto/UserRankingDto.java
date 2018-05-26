package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.UserRanking;
import java.util.List;

public class UserRankingDto {

    private List<UserRanking> rankings;
    private int totalPages;

    public List<UserRanking> getRankings() {
        return rankings;
    }

    public void setRankings(List<UserRanking> rankings) {
        this.rankings = rankings;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}