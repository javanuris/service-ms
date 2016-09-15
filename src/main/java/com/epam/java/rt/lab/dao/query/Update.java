package com.epam.java.rt.lab.dao.query;

import java.util.ArrayList;
import java.util.List;

/**
 * service-ms
 */
public class Update implements Query {
    private String tableName;
    private List<Set> setList;
    private List<Column> columnList;
    private String sql;

    public Update(String tableName) {
        this.tableName = tableName;
        this.setList = new ArrayList<>();
        this.columnList = new ArrayList<>();
    }

    @Override
    public List<Set> getSetList() {
        return this.setList;
    }

    @Override
    public void setSetList(List<Set> setList) {
        this.setList = setList;
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
        if (this.columnList.size() == 0) return null;
        StringBuilder result = new StringBuilder();
        result.append("UPDATE ").append("\"").append(this.tableName).append("\" SET ")
                .append(Set.setListToString(this.setList))
                .append(" WHERE ").append(Column.columnListToString(columnList, "AND", "="));
        return result.toString();
    }

    @Override
    public String createCount() {
        return null;
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