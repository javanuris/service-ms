package com.epam.java.rt.lab.component;

import com.epam.java.rt.lab.util.FormManager;

/**
 * service-ms
 */
public class PageComponent {
    private static final Long ITEMS_ON_PAGE = 10L;
    private Long currentPage;
    private Long itemsOnPage;
    private Long countPages;
    private Long countItems;

    public PageComponent(Long currentPage, Long itemsOnPage) {
        this.currentPage = currentPage;
        this.itemsOnPage = itemsOnPage != null ? itemsOnPage : ITEMS_ON_PAGE;
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

    public Long getCountItems() {
        return countItems;
    }

    public void setCountItems(Long countItems) {
        this.countItems = countItems;
        this.countPages = countItems == null ? null : (long) Math.ceil((countItems * 1.0) / itemsOnPage);
        if (currentPage > countPages) currentPage = countPages;
    }

    public Long getItemsOnPageDefault() {
        return ITEMS_ON_PAGE;
    }

    public String getRequestOnPage(Long page) {
        return "page=".concat(page.toString()).concat("&").concat("items=").concat(itemsOnPage.toString());
    }

    public String getRequestOnItems(Long items) {
        return "page=".concat(currentPage.toString()).concat("&").concat("items=").concat(items.toString());
    }
}
