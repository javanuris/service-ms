package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.DaoException;

/**
 * category-ms
 */
public interface Clause {

    StringBuilder appendClause(StringBuilder result) throws DaoException;

}
