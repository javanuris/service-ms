package com.epam.java.rt.lab.web.component;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.web.validator.ValidatorFactory;

/**
 * category-ms
 */
public class Page {
    private static final Long ITEMS_ON_PAGE = 10L;
    private Long currentPage;
    private Long itemsOnPage;
    private Long countPages;
    private Long countItems;

    public Page(Long currentPage, Long itemsOnPage) {
        this.currentPage = (currentPage != null && currentPage > 0) ? currentPage : 1L;
        this.itemsOnPage = (itemsOnPage != null && itemsOnPage > 0) ? itemsOnPage : ITEMS_ON_PAGE;
    }

    public Page(String currentPage, String itemsOnPage) {
        try {
            this.currentPage = ValidatorFactory.getInstance().create("digits").validate(currentPage) != null ?
                    1L : Long.valueOf(currentPage) > 0 ? Long.valueOf(currentPage) : 1L;
            this.itemsOnPage = ValidatorFactory.getInstance().create("digits").validate(itemsOnPage) != null ?
                    ITEMS_ON_PAGE : Long.valueOf(itemsOnPage) > 0 ? Long.valueOf(itemsOnPage) : ITEMS_ON_PAGE;
        } catch (AppException e) {
            this.currentPage = 1L;
            this.itemsOnPage = ITEMS_ON_PAGE;
        }
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
