package com.epam.java.rt.lab.web.component;

import java.util.ArrayList;
import java.util.List;

/**
 * category-ms
 */
public class Table<T> {
    private List<ListColumn> listColumnList = new ArrayList<>();
    private List<T> entityList;
    private String hrefPrefix;

    public List<T> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<T> entityList) {
        this.entityList = entityList;
    }

    public void addColumn(int width, String header, String fieldName, String localePrefix) {
        this.listColumnList.add(new ListColumn(width, header, fieldName, localePrefix));
    }

    public List<ListColumn> getListColumnList() {
        return listColumnList;
    }

    public void setHrefPrefix(String hrefPrefix) {
        this.hrefPrefix = hrefPrefix;
    }

    public String getHrefPrefix() {
        return hrefPrefix;
    }

    public static class ListColumn {
        private int width;
        private String header;
        private String fieldName;
        private String localePrefix;

        public ListColumn(int width, String header, String fieldName, String localePrefix) {
            this.width = width;
            this.header = header;
            this.fieldName = fieldName;
            this.localePrefix = localePrefix;
        }

        public int getWidth() {
            return width;
        }

        public String getHeader() {
            return header;
        }

        public String getFieldName() {
            return fieldName;
        }

        public String getLocalePrefix() {
            return localePrefix;
        }
    }

}