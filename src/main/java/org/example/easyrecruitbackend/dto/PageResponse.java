package org.example.easyrecruitbackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PageResponse<T> {
    // Getters et setters
    private List<T> content;
    private int totalPages;
    private long totalElements;

    public PageResponse(List<T> content, int totalPages, long totalElements) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

}
