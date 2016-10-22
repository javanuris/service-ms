package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.entity.EntityProperty;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.StringCombiner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.epam.java.rt.lab.dao.sql.SqlExceptionCode.ENTITY_PROPERTY_NOT_FROM_ENTITY;
import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;
import static com.epam.java.rt.lab.util.PropertyManager.COMMA_WITH_SPACE;

/**
 * {@code Update} class defines sql statement
 * which calls UPDATE sql expression
 */
public class Update extends Sql {

    private static final String UPDATE = "UPDATE ";

    /**
     * {@code Class} of entity
     */
    private Class entityClass;
    /** {@code String} representation of table name */
    private String table;
    /** {@code Set} object of set clause */
    private Set set;
    /** {@code Where} object of where clause */
    private Where where;

    /**
     * Initiates new {@code Update} object with defined
     * {@code Class} of entity
     *
     * @param entityClass       {@code Class} of entity
     * @throws AppException
     */
    Update(Class entityClass) throws AppException {
        if (entityClass == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        this.entityClass = entityClass;
        this.table = getProperty(entityClass.getName());
    }

    /**
     * Returns {@code Update} object, on which this method called
     * with setting its set clause
     *
     * @param setValueArray     {@code Array} of {@code SetValue} objects
     * @return                  {@code Update} object
     * @throws AppException
     *
     * @see SetValue
     */
    public Update set(SetValue[] setValueArray) throws AppException {
        if (setValueArray == null) throw new AppException(NULL_NOT_ALLOWED);
        for (SetValue setValue : setValueArray) {
            if (!entityClass.equals(setValue.entityProperty.getEntityClass())) {
                throw new AppException(ENTITY_PROPERTY_NOT_FROM_ENTITY);
            }
        }
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
     * @throws AppException
     *
     * @see com.epam.java.rt.lab.dao.sql.Where.Predicate
     */
    public Update where(Where.Predicate predicate) throws AppException {
        if (predicate == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        this.where = new Where(null, predicate);
        this.where.linkWildValue(getWildValueList());
        return this;
    }

    @Override
    public String create() throws AppException {
        return this.where.appendClause(this.set.
                appendClause(new StringBuilder().
                        append(UPDATE).append(table))).toString();
    }

    /**
     * {@code Set} class defines set clause of sql statement
     *
     * @see SetValue
     * @see WildValue
     */
    static class Set implements Clause {

        private static final String SET = " SET ";

        /** {@code Array} of {@code SetValue} objects */
        private SetValue[] setValueArray;

        /**
         * Initiates new {@code Set} object getDate
         * {@code Array} of {@code SetValue} objects
         *
         * @param setValueArray     {@code Array} of {@code SetValue} objects
         *
         * @see SetValue
         */
        Set(SetValue[] setValueArray) {
            this.setValueArray = setValueArray;
        }

        /**
         * Links {@code List} valueOf {@code WildValue} objects to of single
         * {@code List} which will be set on prepared statement
         *
         * @param wildValueList     {@code List} of {@code WildValue} objects
         *
         * @see WildValue
         */
        private void linkWildValue(List<WildValue> wildValueList) {
            if (this.setValueArray != null) {
                for (SetValue setValue : this.setValueArray) {
                    setValue.wildValue.link(wildValueList);
                }
            }
        }

        @Override
        public StringBuilder appendClause(StringBuilder result)
                throws AppException {
            return ((this.setValueArray == null)
                    || (this.setValueArray.length == 0)) ? result
                    : StringCombiner.combine(result.append(SET),
                    new ArrayList<>(Arrays.asList(this.setValueArray)),
                    COMMA_WITH_SPACE);
        }

    }

    /**
     * {@code SetValue} class defines set value in set clause of sql statement
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
         * @throws AppException
         *
         * @see EntityProperty
         */
        public <T> SetValue(EntityProperty entityProperty, T value)
                throws AppException {
            if (entityProperty == null) {
                throw new AppException(NULL_NOT_ALLOWED);
            }
            this.entityProperty = entityProperty;
            this.wildValue = new WildValue<T>(value);
        }

        @Override
        public StringBuilder appendClause(StringBuilder result)
                throws AppException {
            return result.append(getColumn(this.entityProperty).
                    getColumnName()).append(EQUAL).
                    append(this.wildValue.makeWildcard());
        }

    }

}
