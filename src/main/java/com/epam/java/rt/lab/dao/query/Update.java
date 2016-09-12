package com.epam.java.rt.lab.dao.query;

import java.util.ArrayList;
import java.util.List;

/**
 * service-ms
 */
public class Update implements SqlQuery {
    private String tableName;
    private List<Set> setList;
    private List<Column> columnList;

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
    public List<Column> getColumnList() {
        return this.columnList;
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

}