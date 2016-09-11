package com.epam.java.rt.lab.dao.query;

import java.util.ArrayList;
import java.util.List;

/**
 * service-ms
 */
public class Select {
    private String tableName;
    private String columnNames;
    public List<Column> columnList;
    public List<Join> joinList;

    public Select(String tableName, String columnNames) {
        this.tableName = tableName;
        if (columnNames == null) {
            this.columnNames = "*";
        } else {
            this.columnNames = columnNames;
        }
        this.columnList = new ArrayList<>();
    }

    public String create() {
        StringBuilder result = new StringBuilder();
        result.append("SELECT ").append(this.columnNames).append(" FROM \"").append(this.tableName).append("\"");
        result.append(" WHERE ").append(Column.columnArrayToString((Column[]) columnList.toArray()));
        return result.toString();
    }

}
