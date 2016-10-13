package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.DaoException;

/**
 * This interface used to define appendClause implementation
 * for every sql statement clauses
 */
public interface Clause {

    /**
     * Each clause which implements this method appends
     * clause part to result and returns updated {@code StringBuilder}
     * variable
     *
     * @param result    {@code StringBuilder} initiated variable, which receives
     *                  result valueOf the clause
     * @return          {@code StringBuilder} updated variable with appended clause
     * @throws DaoException
     */
    StringBuilder appendClause(StringBuilder result) throws DaoException;

}

