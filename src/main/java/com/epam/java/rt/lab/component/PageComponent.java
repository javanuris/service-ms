package com.epam.java.rt.lab.component;

/**
 * service-ms
 */
public class PageComponent {
    private Long currentPage;
    private Long itemsOnPage;
    private Long countPages;

    public PageComponent(Long currentPage, Long itemsOnPage) {
        this.currentPage = currentPage;
        this.itemsOnPage = itemsOnPage;
    }

    public Long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Long currentPage) {
        this.currentPage = currentPage;
    }

    public Long getItemsOnPage() {
        return itemsOnPage;
    }

    public void setItemsOnPage(Long itemsOnPage) {
        this.itemsOnPage = itemsOnPage;
    }

    public Long getCountPages() {
        return countPages;
    }

    public void setCountPages(Long countPages) {
        this.countPages = countPages;
    }
}
