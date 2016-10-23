package com.epam.java.rt.lab.web.component;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.web.validator.Validator;
import com.epam.java.rt.lab.web.validator.ValidatorFactory;

import static com.epam.java.rt.lab.util.PropertyManager.ASTERISK;
import static com.epam.java.rt.lab.web.validator.ValidatorFactory.DIGITS;

public class Page {

    private static final Long ITEMS_ON_PAGE = 10L;
    private static final String PAGE_PREFIX = "page=";
    private static final String ITEMS_PREFIX = "items=";

    private Long currentPage;
    private Long itemsOnPage;
    private Long countPages;
    private Long countItems;

    public Page(Long currentPage, Long itemsOnPage) {
        this.currentPage = ((currentPage != null) && (currentPage > 0))
                ? currentPage : 1L;
        this.itemsOnPage = ((itemsOnPage != null) && (itemsOnPage > 0))
                ? itemsOnPage : ITEMS_ON_PAGE;
    }

    public Page(String currentPage, String itemsOnPage) {
        try {
            Validator digitValidator =
                    ValidatorFactory.getInstance().create(DIGITS);
            Long currentPageValue =
                    (digitValidator.validate(currentPage) != null)
                            ? 1L
                            : Long.valueOf(currentPage);
            this.currentPage = (currentPageValue > 0)
                    ? currentPageValue
                    : 1L;
            Long itemsOnPageValue =
                    (digitValidator.validate(itemsOnPage) != null)
                            ? ITEMS_ON_PAGE
                            : Long.valueOf(itemsOnPage);
            this.itemsOnPage = (itemsOnPageValue > 0)
                    ? itemsOnPageValue
                    : ITEMS_ON_PAGE;
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
        this.countPages = (countItems == null) ? 0
                : (long) Math.ceil((countItems * 1.0) / itemsOnPage);
        if (currentPage > countPages) currentPage = countPages;
    }

    public Long getItemsOnPageDefault() {
        return ITEMS_ON_PAGE;
    }

    public String getRequestOnPage(Long page) {
        return PAGE_PREFIX + page.toString() + ASTERISK
                + ITEMS_PREFIX + itemsOnPage.toString();
    }

    public String getRequestOnItems(Long items) {
        return PAGE_PREFIX + currentPage.toString() + ASTERISK
                + ITEMS_PREFIX + items.toString();
    }
}
