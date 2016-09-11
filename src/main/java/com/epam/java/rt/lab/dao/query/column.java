package com.epam.java.rt.lab.dao.query;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * service-ms
 */
public class Column<T> {
    public String name;
    public T value;
    public boolean isColumn = false;

    public Column(String name, T value) {
        this.name = convertName(name);
        this.value = value;
    }

    public Column(String name, T value, boolean isColumn) {
        this.name = convertName(name);
        this.value = value;
        this.isColumn = isColumn;
    }

    private String convertName(String name) {
        Pattern pattern = Pattern.compile("[A-Z]");
        Matcher matcher = pattern.matcher(name);
        int startIndex = name.indexOf(".");
        if(startIndex < 0) startIndex = 0;
        while(matcher.find(startIndex)) {
            name = name.substring(0, matcher.start()).concat("_")
                    .concat(name.substring(matcher.start(), matcher.end()).toLowerCase())
                    .concat(name.substring(matcher.end()));
            matcher = pattern.matcher(name);
        }
        return name;
    }

    public static String columnListToString(List<Column> columnList) {
        boolean firstColumn = true;
        StringBuilder result = new StringBuilder();
        for(Column column : columnList) {
            if (firstColumn) {
                firstColumn = false;
            } else {
                result.append(" AND ");
            }
            if (column.value == null) {
                result.append(column.name).append(" is null");
            } else {
                if (column.isColumn) {
                    result.append(column.name).append(" = ").append(column.value);
                } else {
                    result.append(column.name).append(" = ?");
                }
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
