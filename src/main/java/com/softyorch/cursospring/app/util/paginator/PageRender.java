package com.softyorch.cursospring.app.util.paginator;

import org.springframework.data.domain.Page;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class PageRender<T> {

    private String url;
    private Page<T> page;

    private int totalPages;
    private int numElementsPerPage;
    private int currentPage;

    private List<PageItem> pages;

    public PageRender(String url, Page<T> page) {
        this.url = url;
        this.page = page;
        this.pages = new ArrayList<>();

        numElementsPerPage = page.getSize();
        totalPages = page.getTotalPages();
        currentPage = page.getNumber() + 1;

        int start, end;
        if (totalPages <= numElementsPerPage) {
            start = 1;
            end = totalPages;
        } else {
            if (currentPage <= numElementsPerPage/2) {
                start = 1;
                end = numElementsPerPage;
            } else if (currentPage >= totalPages - numElementsPerPage/2) {
                start = totalPages - numElementsPerPage + 1;
                end = numElementsPerPage;
            } else {
                start = currentPage - numElementsPerPage/2;
                end = numElementsPerPage;
            }
        }

        for (int idx = 0; idx < end; idx++) {
            pages.add(new PageItem(start + idx, currentPage == start + idx));
        }

    }

    public String getUrl() {
        return url;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public List<PageItem> getPages() {
        return pages;
    }

    public boolean isFirst() {
        return page.isFirst();
    }

    public boolean isLast() {
        return page.isLast();
    }

    public boolean isHasNext() {
        return page.hasNext();
    }

    public boolean isHasPrevious() {
        return page.hasPrevious();
    }

}
