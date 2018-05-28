package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.TeamRanking;
import ch.fhnw.edu.wodss.tippspielapi.model.UserRanking;

import java.util.List;

public class TeamRankingDto {

    private List<TeamRanking> content;
    private int totalPages;

    public List<TeamRanking> getContent() {
        return content;
    }

    public void setContent(List<TeamRanking> content) {
        this.content = content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}