package ch.fhnw.edu.wodss.tippspielapi.controller.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.Collection;

@Data
public class PageDto<Dto> {
    private int totalPages;
    private Collection<Dto> content;

    public PageDto(Page<Dto> page) {
        this.totalPages = page.getTotalPages();
        this.content = page.getContent();
    }
}
