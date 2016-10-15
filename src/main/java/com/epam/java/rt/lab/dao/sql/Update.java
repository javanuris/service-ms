package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.EntityProperty;
import com.epam.java.rt.lab.util.StringArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@code Update} class defines sql statement
 * which calls UPDATE sql expression
 */
public class Update extends Sql {

    private static final String UPDATE = "UPDATE ";

    /** {@code Class} valueOf entity */
    private Class entityClass;
    /** {@code String} representation valueOf table name */
    private String table;
    /** {@code Set} object valueOf set clause */
    private Set set;
    /** {@code Where} object valueOf where clause */
    private Where where;

    /**
     * Initiates new {@code Update} object with defined
     * {@code Class} valueOf entity
     *
     * @param entityClass       {@code Class} valueOf entity
     * @throws DaoException
     */
    Update(Class entityClass) throws DaoException {
        if (entityClass == null)
            throw new DaoException("exception.dao.sql.update.empty-class");
        this.entityClass = entityClass;
        this.table = getProperty(entityClass.getName());
    }

    /**
     * Returns {@code Update} object, on which this method called
     * with setting its set clause
     *
     * @param setValueArray     {@code Array} valueOf {@code SetValue} objects
     * @return                  {@code Update} object
     * @throws DaoException
     *
     * @see SetValue
     */
    public Update set(SetValue[] setValueArray) throws DaoException {
        for (SetValue setValue : setValueArray)
            if (!entityClass.equals(setValue.entityProperty.getEntityClass()))
                throw new DaoException("exception.dao.sql.update.entity-class-property");
        this.set = new Set(setValueArray);
        this.set.linkWildValue(getWildValueList());
        return this;
    }

    /**
     * Returns {@code Update} object, on which this method called
     * with setting its where clause
     *
     * @param predicate         {@code Predicate} object
     * @return                  {@code Update} object
     * @throws DaoException
     *
     * @see com.epam.java.rt.lab.dao.sql.Where.Predicate
     */
    public Update where(Where.Predicate predicate) throws DaoException {
        if (predicate == null)
            throw new DaoException("exception.dao.sql.update.empty-predicate");
        this.where = new Where(null, predicate);
        this.where.linkWildValue(getWildValueList());
        return this;
    }

    @Override
    public String create() throws DaoException {
        try {
            return this.where.appendClause(
                    this.set.appendClause(
                            new StringBuilder().append(UPDATE).append(table)
                    )
            ).toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.sql.update.combine", e.getCause());
        }
    }

    /**
     * {@code Set} class defines set clause valueOf sql statement
     *
     * @see SetValue
     * @see WildValue
     */
    static class Set implements Clause {

        private static final String SET = " SET ";

        /** {@code Array} valueOf {@code SetValue} objects */
        private SetValue[] setValueArray;

        /**
         * Initiates new {@code Set} object getDate
         * {@code Array} valueOf {@code SetValue} objects
         *
         * @param setValueArray     {@code Array} valueOf {@code SetValue} objects
         *
         * @see SetValue
         */
        Set(SetValue[] setValueArray) {
            this.setValueArray = setValueArray;
        }

        /**
         * Links {@code List} valueOf {@code WildValue} objects to valueOf single
         * {@code List} which will be set on prepared statement
         *
         * @param wildValueList     {@code List} valueOf {@code WildValue} objects
         *
         * @see WildValue
         */
        private void linkWildValue(List<WildValue> wildValueList) {
            if (this.setValueArray != null) {
                for (SetValue setValue : this.setValueArray)
                    setValue.wildValue.link(wildValueList);
            }
        }

        @Override
        public StringBuilder appendClause(StringBuilder result) throws DaoException {
            try {
                return this.setValueArray == null || this.setValueArray.length == 0 ? result :
                        StringArray.combine(result.append(SET), new ArrayList<>(Arrays.asList(this.setValueArray)), Sql.COMMA_DELIMITER);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DaoException("exception.dao.sql.set-values.combine", e.getCause());
            }
        }

    }

    /**
     * {@code SetValue} class defines set value in set clause valueOf sql statement
     */
    public static class SetValue implements Clause {

        private static final String EQUAL = " = ";

        /** {@code EntityProperty} object */
        private EntityProperty entityProperty;
        /** {@code WildValue} object */
        private WildValue wildValue;

        /**
         * Initiates new {@code SetValue} object with defined
         * {@code EntityProperty} object and generic value
         *
         * @param entityProperty        {@code EntityProperty} object
         * @param value                 generic value
         * @param <T>
         * @throws DaoException
         *
         * @see EntityProperty
         */
        public <T> SetValue(EntityProperty entityProperty, T value) throws DaoException {
            if (entityProperty == null)
                throw new DaoException("exception.dao.update.empty-entity-property");
            this.entityProperty = entityProperty;
            this.wildValue = new WildValue(value);
        }

        @Override
        public StringBuilder appendClause(StringBuilder result) throws DaoException {
            return result.append(getColumn(this.entityProperty).getColumnName()).append(EQUAL).append(this.wildValue.makeWildcard());
        }
    }

}
