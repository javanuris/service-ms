package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.exception.AppException;

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
     *                  result of the clause
     * @return          {@code StringBuilder} updated variable with
     *                  appended clause
     * @throws AppException
     */
    StringBuilder appendClause(StringBuilder result) throws AppException;

}

