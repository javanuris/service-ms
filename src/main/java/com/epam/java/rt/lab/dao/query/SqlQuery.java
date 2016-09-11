package com.epam.java.rt.lab.dao.query;

import java.util.List;

/**
 * service-ms
 */
public interface SqlQuery {

    List<Set> getSetList();

    List<Column> getColumnList();

    String create();

}
