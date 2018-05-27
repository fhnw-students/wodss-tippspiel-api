package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import ch.fhnw.edu.wodss.tippspielapi.model.UserRanking;
import java.util.List;

public class UserRankingDto {

    private List<UserRanking> content;
    private int totalPages;

    public List<UserRanking> getContent() {
        return content;
    }

    public void setContent(List<UserRanking> content) {
        this.content = content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}