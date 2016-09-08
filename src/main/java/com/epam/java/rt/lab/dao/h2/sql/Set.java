package com.epam.java.rt.lab.dao.h2.sql;

import com.epam.java.rt.lab.dao.DaoException;

/**
 * service-ms
 */
public class Set {
    String columnName;

    public Set(String columnName) throws DaoException {
        this.columnName = Validator.toSqlExpression(columnName);
    }

    @Override
    public String toString() {
        return columnName.concat(" = ?");
    }

}
