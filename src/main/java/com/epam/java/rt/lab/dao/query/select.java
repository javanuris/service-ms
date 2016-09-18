package com.epam.java.rt.lab.dao.query;

import java.util.ArrayList;
import java.util.List;

/**
 * service-ms
 */
public class Select implements Query {
    private String tableName;
    private String columnNames;
    private Long offset;
    private Long count;
    private List<Column> columnList;
    private String order;
    private String sql;

    public Select(String tableName, String columnNames) {
        this.tableName = tableName;
        if (columnNames == null) {
            this.columnNames = "*";
        } else {
            this.columnNames = columnNames;
        }
        this.columnList = new ArrayList<>();
        this.order = null;
    }

    public Select(String tableName, String columnNames, Long offset, Long count, String order) {
        this.tableName = tableName;
        if (columnNames == null) {
            this.columnNames = "*";
        } else {
            this.columnNames = columnNames;
        }
        this.offset = offset;
        this.count = count;
        this.columnList = new ArrayList<>();
        this.order = order;
    }

    @Override
    public List<Set> getSetList() {
        return null;
    }

    @Override
    public void setSetList(List<Set> setList) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Column> getColumnList() {
        return this.columnList;
    }

    @Override
    public void setColumnList(List<Column> columnList) {
        this.columnList = columnList;
    }

    @Override
    public String create() {
        StringBuilder result = new StringBuilder();
        result.append("SELECT ").append(this.columnNames).append(" FROM \"").append(this.tableName).append("\"");
        if (this.columnList.size() > 0)
            result.append(" WHERE ").append(Column.columnListToString(this.columnList, "AND", "="));
        if (order != null && order.length() > 0) result.append(" ORDER BY ").append(order);
        if (offset != null && count != null) result.append(" LIMIT ").append(offset).append(", ").append(count);
        return result.toString();
    }

    @Override
    public String createCount() {
        StringBuilder result = new StringBuilder();
        result.append("SELECT COUNT(*) AS count ").append(" FROM \"").append(this.tableName).append("\"");
        if (this.columnList.size() > 0)
            result.append(" WHERE ").append(Column.columnListToString(this.columnList, "AND", "="));
        return result.toString();
    }

    @Override
    public String getSql() {
        return this.sql;
    }

    @Override
    public void setSql(String sql) {
        this.sql = sql;
    }
}
