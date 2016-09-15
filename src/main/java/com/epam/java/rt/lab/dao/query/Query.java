package com.epam.java.rt.lab.dao.query;

import java.util.List;

/**
 * service-ms
 */
public interface Query {

    List<Set> getSetList();

    void setSetList(List<Set> setList);

    List<Column> getColumnList();

    void setColumnList(List<Column> columnList);

    String create();

    String createCount();

    String getSql();

    void setSql(String sql);

}
