package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.entity.EntityProperty;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.StringCombiner;

import java.util.ArrayList;
import java.util.Arrays;

import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;
import static com.epam.java.rt.lab.util.PropertyManager.COMMA_WITH_SPACE;

/**
 * {@code OrderBy} class defines sql statement clause
 */
public class OrderBy implements Clause {

    private static final String ORDER_BY = " ORDER BY ";

    /**
     * {@code Array} of {@code Criteria} objects
     */
    private Criteria[] criteriaArray;

    /**
     * Initiates new {@code OrderBy} object with defined
     * {@code Array} of {@code Criteria} objects
     *
     * @param criteriaArray     {@code Array} of {@code Criteria} objects
     */
    OrderBy(Criteria[] criteriaArray) {
        this.criteriaArray = criteriaArray;
    }

    @Override
    public StringBuilder appendClause(StringBuilder result)
            throws AppException {
        if (result == null) throw new AppException(NULL_NOT_ALLOWED);
        return ((this.criteriaArray == null)
                || (this.criteriaArray.length == 0)) ? result
                : StringCombiner.combine(result.append(ORDER_BY),
                new ArrayList<>(Arrays.asList(this.criteriaArray)),
                COMMA_WITH_SPACE);
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
         * @return                  {@code Criteria} object,
         *                          on which this method called
         * @throws AppException
         *
         * @see EntityProperty
         */
        public static Criteria asc(EntityProperty entityProperty)
                throws AppException {
            if (entityProperty == null) {
                throw new AppException(NULL_NOT_ALLOWED);
            }
            return new Criteria(Sql.getColumn(entityProperty), false);
        }

        /**
         * Returns new {@code Criteria} object with descending ordering type
         * for column defined by {@code EntityProperty} object
         *
         * @param entityProperty    {@code EntityProperty} object
         * @return                  {@code Criteria} object,
         *                          on which this method called
         * @throws AppException
         *
         * @see EntityProperty
         */
        public static Criteria desc(EntityProperty entityProperty)
                throws AppException {
            if (entityProperty == null) {
                throw new AppException(NULL_NOT_ALLOWED);
            }
            return new Criteria(Sql.getColumn(entityProperty), true);
        }

        @Override
        public StringBuilder appendClause(StringBuilder result)
                throws AppException {
            if (result == null) throw new AppException(NULL_NOT_ALLOWED);
            return this.column.appendClause(result).
                    append(this.descending ? DESC : ASC);
        }
    }

}