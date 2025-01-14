package com.pioli.users.domain.pagination;

import java.util.List;

public class Page<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    @SuppressWarnings("unused")
    private int totalPages;

    public Page(List<T> content, int page, int size, long totalElements, int totalPages) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public List<T> getContent() {
        return content;
    }

    public long getTotalElements() {
        return totalElements;
    }
} 