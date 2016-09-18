package com.epam.java.rt.lab.dao.query;

import java.util.ArrayList;
import java.util.List;

/**
 * service-ms
 */
public class Create implements Query {
    private String tableName;
    private List<Set> setList;
    private String sql;

    public Create(String tableName) {
        this.tableName = tableName;
        this.setList = new ArrayList<>();
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
        return null;
    }

    @Override
    public void setColumnList(List<Column> columnList) {
    }

    @Override
    public String create() {
        if (this.setList.size() == 0) return null;
        StringBuilder result = new StringBuilder();
        result.append("INSERT INTO ").append("\"").append(this.tableName).append("\" SET ")
                .append(Set.setListToString(this.setList));
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