package com.epam.java.rt.lab.dao.query;

import java.util.List;

/**
 * service-ms
 */
public class Column<T> {
    public String name;
    public T value;
    public boolean isColumn = false;

    public Column(String name, T value) {
        this.name = SqlAdapter.convertName(name);
        this.value = value;
    }

    public Column(String name, T value, boolean isColumn) {
        this.name = SqlAdapter.convertName(name);
        this.value = value;
        this.isColumn = isColumn;
    }

    public static String columnListToString(List<Column> columnList, String delimiter, String sign) {
        boolean firstColumn = true;
        StringBuilder result = new StringBuilder();
        for(Column column : columnList) {
            if (firstColumn) {
                firstColumn = false;
            } else {
                result.append(" ").append(delimiter).append(" ");
            }
            if (column.value == null) {
                result.append(column.name).append(" is null");
            } else {
                if (column.isColumn) {
                    result.append(column.name).append(" ").append(sign).append(" ").append(column.value);
                } else {
                    result.append(column.name).append(" ").append(sign).append(" ?");
                }
            }
        }
        return result.toString();
    }

}
