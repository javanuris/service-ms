package com.epam.java.rt.lab.dao.query;

import java.util.ArrayList;
import java.util.List;

/**
 * service-ms
 */
public class Select implements SqlQuery {
    private String tableName;
    private String columnNames;
    private List<Column> columnList;

    public Select(String tableName, String columnNames) {
        this.tableName = tableName;
        if (columnNames == null) {
            this.columnNames = "*";
        } else {
            this.columnNames = columnNames;
        }
        this.columnList = new ArrayList<>();
    }

    @Override
    public List<Set> getSetList() {
        return null;
    }

    @Override
    public List<Column> getColumnList() {
        return this.columnList;
    }

    @Override
    public String create() {
        StringBuilder result = new StringBuilder();
        result.append("SELECT ").append(this.columnNames).append(" FROM \"").append(this.tableName).append("\"");
        if (this.columnList.size() > 0)
            result.append(" WHERE ").append(Column.columnListToString(this.columnList, "AND", "="));
        return result.toString();
    }

}
