package com.epam.java.rt.lab.dao.query;

import java.lang.reflect.Field;

/**
 * service-ms
 */
public class Column<T> {
    public String name;
    public T value;

    public Column(String name, T value) {
        this.name = name;
        this.value = value;
    }

    static String columnArrayToString(Column[] columnArray) {
        boolean firstColumn = true;
        StringBuilder result = new StringBuilder();
        for(Column column : columnArray) {
            if (firstColumn) {
                firstColumn = false;
            } else {
                result.append(" AND ");
            }
            if (column.value == null) {
                result.append(column.name).append(" is null");
            } else {
                result.append(column.name).append(" = ?");
            }
        }
        return result.toString();
    }

    static String columnNameArrayToString(String[] columnNameArray) {
        boolean firstColumn = true;
        StringBuilder result = new StringBuilder();
        for(String columnName : columnNameArray) {
            if (firstColumn) {
                firstColumn = false;
            } else {
                result.append(", ");
            }
            result.append(columnName);
        }
        return result.toString();
    }

}
