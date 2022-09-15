package ar.edu.itba.paw.model;

import java.util.List;

public class Page<T>{
    private final List<T> content;
    private final long currentPage, totalPages;


    public Page(List<T> content, long currentPage, long totalPages) {
        this.content = content;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    public List<T> getContent() {
        return content;
    }

    public long getCurrentPage() {
        return currentPage;
    }

    public long getTotalPages() {
        return totalPages;
    }
}
