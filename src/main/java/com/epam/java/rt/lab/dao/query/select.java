package com.epam.java.rt.lab.dao.query;

import java.util.ArrayList;
import java.util.List;

/**
 * service-ms
 */
public class Select implements SqlQuery {
    private String tableName;
    private String columnNames;
    private Long offset;
    private Long count;
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

    public Select(String tableName, String columnNames, Long offset, Long count) {
        this.tableName = tableName;
        if (columnNames == null) {
            this.columnNames = "*";
        } else {
            this.columnNames = columnNames;
        }
        this.offset = offset;
        this.count = count;
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
        if (offset != null && count != null) result.append(" LIMIT ").append(offset).append(", ").append(count);
//System.out.println(result);
        return result.toString();
    }

    @Override
    public String createCount() {
        StringBuilder result = new StringBuilder();
        result.append("SELECT COUNT(*) AS count ").append(" FROM \"").append(this.tableName).append("\"");
        if (this.columnList.size() > 0)
            result.append(" WHERE ").append(Column.columnListToString(this.columnList, "AND", "="));
//System.out.println(result);
        return result.toString();
    }

}
