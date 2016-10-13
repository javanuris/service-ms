package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.BaseEntity;
import com.epam.java.rt.lab.entity.EntityProperty;
import com.epam.java.rt.lab.util.StringArray;

import java.util.List;
import java.util.Map;

/**
 * {@code Insert} class defines sql statement,
 * which calls INSERT sql expression
 */
public class Insert extends Sql {

    private static final String INSERT = "INSERT INTO ";
    private static final String BEFORE_NAMES = " (";
    private static final String BEFORE_VALUES = ") VALUES (";
    private static final String AFTER_VALUES = ")";

    /** {@code BaseEntity} object, which should be inserted to database */
    private BaseEntity entity;
    /** {@code String} representation pf table name */
    private String table;
    /** {@code Array} valueOf {@code InsertValue} objects */
    private InsertValue[] insertValueArray;

    /**
     * Initiates new {@code Insert} object with defined
     * {@code BaseEntity} object
     *
     * @param entity        {@code BaseEntity} object
     * @throws DaoException
     */
    Insert(BaseEntity entity) throws DaoException {
        if (entity == null)
            throw new DaoException("exception.dao.sql.insert.empty-entity");
        this.entity = entity;
        this.table = getProperty(entity.getClass().getName());
    }

    /**
     * Initiates new {@code Insert} object with defined
     * {@code Class} valueOf entity to define table name
     *
     * @param propertyClass {@code Class} valueOf entity
     * @throws DaoException
     */
    Insert(Class propertyClass) throws DaoException {
        if (propertyClass == null)
            throw new DaoException("exception.dao.sql.insert.empty-entity");
        this.table = getProperty(propertyClass.getName());
    }

    /**
     * Returns {@code Insert} object, on which this method called
     * with setting its values clause
     *
     * @param insertValueArray  {@code Array} valueOf {@code InsertValue} objects
     * @return                  {@code Insert} object, on which this method called
     * @throws DaoException
     *
     * @see InsertValue
     */
    public Insert values(InsertValue... insertValueArray) throws DaoException {
        for (InsertValue insertValue : insertValueArray) {
            if (!entity.getClass().equals(insertValue.entityProperty.getEntityClass()))
                throw new DaoException("exception.dao.sql.insert.entity-class-property");
            insertValue.value.link(getWildValueList());
        }
        this.insertValueArray = insertValueArray;
        return this;
    }

    @Override
    public String create() throws DaoException {
        boolean first = true;
        StringBuilder result = new StringBuilder();
        StringBuilder values = new StringBuilder();
        result.append(INSERT).append(table).append(BEFORE_NAMES);
        values.append(BEFORE_VALUES);
        for (InsertValue insertValue : this.insertValueArray) {
            if (first) {
                first = false;
            } else {
                result.append(COMMA_DELIMITER);
                values.append(COMMA_DELIMITER);
            }
            result.append(getColumn(insertValue.entityProperty).getColumnName());
            values.append(insertValue.value.makeWildcard());
        }
        result.append(values).append(AFTER_VALUES);
//        System.out.println(result);
        return result.toString();
    }

    /**
     * {@code InsertValue} class defines inserting values
     * through {@code EntityProperty} object and
     * {@code WildValue} object
     *
     * @see EntityProperty
     * @see WildValue
     */
    public static class InsertValue {

        /** {@code EntityProperty} object */
        private EntityProperty entityProperty;
        /** {@code WildValue} object */
        private WildValue value;

        /**
         * Initiates new {@code InsertValue} object with defined
         * {@code EntityProperty} object and generic value, from which
         * created new {@code WildValue} object
         *
         * @param entityProperty    {@code EntityProperty} object
         * @param value             generic value
         * @param <T>
         * @throws DaoException
         *
         * @see EntityProperty
         * @see WildValue
         */
        public <T> InsertValue(EntityProperty entityProperty, T value) throws DaoException {
            this.entityProperty = entityProperty;
            this.value = new WildValue(value);
        }

    }
}
