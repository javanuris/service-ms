package com.epam.java.rt.lab.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * service-ms
 */
public class ListComponent {
    public static final Long MAX_LIST_ITEM_ON_PAGE = 10L;
    private List<ListColumn> listColumnList = new ArrayList<>();
    private List<Map<String, String>> valueMapList = new ArrayList<>();

    public void addValueMap(Map<String, String> valueMap) {
        this.valueMapList.add(valueMap);
    }

    public List<Map<String, String>> getValueMapList() {
        return valueMapList;
    }

    public void addColumn(int width, String header, String fieldName) {
        this.listColumnList.add(new ListColumn(width, header, fieldName));
    }

    public List<ListColumn> getListColumnList() {
        return listColumnList;
    }

    public static class ListColumn {
        private int width;
        private String header;
        private String fieldName;

        public ListColumn(int width, String header, String fieldName) {
            this.width = width;
            this.header = header;
            this.fieldName = fieldName;
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
    }

}