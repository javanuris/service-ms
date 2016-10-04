package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.EntityProperty;
import com.epam.java.rt.lab.util.StringArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * category-ms
 */
public class OrderBy implements Clause {

    private static final String ORDER_BY = " ORDER BY ";

    private Criteria[] criteriaArray;

    OrderBy(Criteria[] criteriaArray) {
        this.criteriaArray = criteriaArray;
    }

    @Override
    public StringBuilder appendClause(StringBuilder result) throws DaoException {
        try {
            return this.criteriaArray == null || this.criteriaArray.length == 0 ? result :
                    StringArray.combine(result.append(ORDER_BY), new ArrayList<>(Arrays.asList(this.criteriaArray)), Sql.COMMA_DELIMITER);
        } catch (Exception e) {
            throw new DaoException("exception.dao.sql.order-by.combine", e.getCause());
        }

    }

    public static class Criteria implements Clause {

        private static final String ASC = " ASC";
        private static final String DESC = " DESC";

        private Column column;
        private boolean descending;

        Criteria(Column column, boolean descending) {
            this.column = column;
            this.descending = descending;
        }

        // get

        public static Criteria asc(EntityProperty entityProperty) throws DaoException {
            return new Criteria(Sql.getColumn(entityProperty), false);
        }

        public static Criteria desc(EntityProperty entityProperty) throws DaoException {
            return new Criteria(Sql.getColumn(entityProperty), true);
        }

        @Override
        public StringBuilder appendClause(StringBuilder result) throws DaoException {
            return this.column.appendClause(result).append(this.descending ? DESC : ASC);
        }
    }

}
