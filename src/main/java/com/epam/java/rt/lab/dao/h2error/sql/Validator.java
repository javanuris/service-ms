package com.epam.java.rt.lab.dao.h2error.sql;

import com.epam.java.rt.lab.dao.DaoException;

/**
 * service-ms
 */
class Validator {

    static String toSqlExpression(String rawExpression) throws DaoException {
        if (rawExpression == null) return null;
        if (rawExpression.contains(";")) throw new DaoException("Illegal character detected");
        return rawExpression;
    }

}
