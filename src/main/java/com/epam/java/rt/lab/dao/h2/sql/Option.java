package com.epam.java.rt.lab.dao.h2.sql;

import com.epam.java.rt.lab.dao.DaoException;

/**
 * service-ms
 */
public class Option {
    String columnName;
    String compareColumnName;

    public Option(String columnName, String compareColumnName) throws DaoException {
        this.columnName = Validator.toSqlExpression(columnName);
        this.compareColumnName = Validator.toSqlExpression(compareColumnName);
    }

    public Option(String columnName) throws DaoException {
        this.columnName = Validator.toSqlExpression(columnName);
        this.compareColumnName = "?";
    }

    public boolean asOn() {
        return columnName != null && compareColumnName != null && !compareColumnName.equals("?");
    }

    @Override
    public String toString() {
        if (compareColumnName == null) return columnName.concat(" is null");
        return columnName.concat(" = ").concat(compareColumnName);
    }

}
