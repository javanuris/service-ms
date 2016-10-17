package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.EntityProperty;
import com.epam.java.rt.lab.util.StringCombiner;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * {@code OrderBy} class defines sql statement clause
 */
public class OrderBy implements Clause {

    private static final String ORDER_BY = " ORDER BY ";

    /** {@code Array} valueOf {@code Criteria} objects */
    private Criteria[] criteriaArray;

    /**
     * Initiates new {@code OrderBy} object with defined
     * {@code Array} valueOf {@code Criteria} objects
     *
     * @param criteriaArray     {@code Array} valueOf {@code Criteria} objects
     */
    OrderBy(Criteria[] criteriaArray) {
        this.criteriaArray = criteriaArray;
    }

    @Override
    public StringBuilder appendClause(StringBuilder result) throws DaoException {
        try {
            return this.criteriaArray == null || this.criteriaArray.length == 0 ? result :
                    StringCombiner.combine(result.append(ORDER_BY), new ArrayList<>(Arrays.asList(this.criteriaArray)), Sql.COMMA_DELIMITER);
        } catch (Exception e) {
            throw new DaoException("exception.dao.sql.order-by.combine", e.getCause());
        }

    }

    /**
     * {@code Criteria} class defines ordering criteria
     *
     * @see Column
     * @see EntityProperty
     */
    public static class Criteria implements Clause {

        private static final String ASC = " ASC";
        private static final String DESC = " DESC";

        /** {@code Column} object */
        private Column column;
        /** {@code boolean} value indicating ordering type */
        private boolean descending;

        /**
         * Initiates new {@code Criteria} object with defined
         * {@code Column} object and {@code boolean} value indicating
         * ordering type
         *
         * @param column        {@code Column} object
         * @param descending    {@code boolean} value indication ordering type
         */
        Criteria(Column column, boolean descending) {
            this.column = column;
            this.descending = descending;
        }

        /**
         * Returns new {@code Criteria} object with ascending ordering type
         * for column defined by {@code EntityProperty} object
         *
         * @param entityProperty    {@code EntityProperty} object
         * @return                  {@code Criteria} object, on which this method called
         * @throws DaoException
         *
         * @see EntityProperty
         */
        public static Criteria asc(EntityProperty entityProperty) throws DaoException {
            return new Criteria(Sql.getColumn(entityProperty), false);
        }

        /**
         * Returns new {@code Criteria} object with descending ordering type
         * for column defined by {@code EntityProperty} object
         *
         * @param entityProperty    {@code EntityProperty} object
         * @return                  {@code Criteria} object, on which this method called
         * @throws DaoException
         *
         * @see EntityProperty
         */
        public static Criteria desc(EntityProperty entityProperty) throws DaoException {
            return new Criteria(Sql.getColumn(entityProperty), true);
        }

        @Override
        public StringBuilder appendClause(StringBuilder result) throws DaoException {
            return this.column.appendClause(result).append(this.descending ? DESC : ASC);
        }
    }

}
