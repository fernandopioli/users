package com.pioli.users.domain.pagination;

import java.util.Map;

public class Pagination {
    private int page;
    private int size;
    private String sortField;
    private String sortDirection;
    private Map<String, String> filters;

    public Pagination(int page, int size, String sortField, String sortDirection, Map<String, String> filters) {
        this.page = page;
        this.size = size;
        this.sortField = sortField;
        this.sortDirection = sortDirection;
        this.filters = filters;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }   

    public String getSortField() {
        return sortField;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public Map<String, String> getFilters() {
        return filters;
    }

} 