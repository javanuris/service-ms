package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.entity.BaseEntity;
import com.epam.java.rt.lab.entity.EntityProperty;
import com.epam.java.rt.lab.exception.AppException;

import static com.epam.java.rt.lab.dao.sql.SqlExceptionCode.PROPERTY_NOT_ASSIGNABLE_TO_TABLE;
import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;
import static com.epam.java.rt.lab.util.PropertyManager.COMMA;

/**
 * {@code Insert} class defines sql statement,
 * which calls INSERT sql expression
 */
public class Insert extends Sql {

    private static final String INSERT = "INSERT INTO ";
    private static final String BEFORE_NAMES = " (";
    private static final String BETWEEN_NAMES_AND_VALUES = ") VALUES (";
    private static final String AFTER_VALUES = ")";

    /** {@code BaseEntity} object, which should be inserted to database */
    private BaseEntity entity;
    /**
     * {@code String} representation of table name
     */
    private String table;
    /** {@code Array} of {@code InsertValue} objects */
    private InsertValue[] insertValueArray;

    /**
     * Initiates new {@code Insert} object with defined
     * {@code BaseEntity} object
     *
     * @param entity        {@code BaseEntity} object
     * @throws AppException
     */
    Insert(BaseEntity entity) throws AppException {
        if (entity == null) throw new AppException(NULL_NOT_ALLOWED);
        this.entity = entity;
        this.table = getProperty(entity.getClass().getName());
    }

    /**
     * Initiates new {@code Insert} object with defined
     * {@code Class} of entity to define table name
     *
     * @param propertyClass {@code Class} of entity
     * @throws AppException
     */
    Insert(Class propertyClass) throws AppException {
        if (propertyClass == null) throw new AppException(NULL_NOT_ALLOWED);
        this.table = getProperty(propertyClass.getName());
    }

    /**
     * Returns {@code Insert} object, on which this method called
     * with setting its values clause
     *
     * @param insertValueArray  {@code Array} of {@code InsertValue} objects
     * @return                  {@code Insert} object, on which this method called
     * @throws AppException
     *
     * @see InsertValue
     */
    public Insert values(InsertValue... insertValueArray) throws AppException {
        for (InsertValue insertValue : insertValueArray) {
            if (!entity.getClass().equals(insertValue.
                    entityProperty.getEntityClass())) {
                throw new AppException(PROPERTY_NOT_ASSIGNABLE_TO_TABLE);
            }
            insertValue.value.link(getWildValueList());
        }
        this.insertValueArray = insertValueArray;
        return this;
    }

    @Override
    public String create() throws AppException {
        boolean first = true;
        StringBuilder result = new StringBuilder();
        StringBuilder values = new StringBuilder();
        result.append(INSERT).append(table).append(BEFORE_NAMES);
        values.append(BETWEEN_NAMES_AND_VALUES);
        for (InsertValue insertValue : this.insertValueArray) {
            if (first) {
                first = false;
            } else {
                result.append(COMMA);
                values.append(COMMA);
            }
            Column column = getColumn(insertValue.entityProperty);
            result.append(column.getColumnName());
            values.append(insertValue.value.makeWildcard());
        }
        result.append(values).append(AFTER_VALUES);
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
         * {@code EntityProperty} object and generic value, getDate which
         * created new {@code WildValue} object
         *
         * @param entityProperty    {@code EntityProperty} object
         * @param value             generic value
         * @param <T>
         * @throws AppException
         *
         * @see EntityProperty
         * @see WildValue
         */
        public <T> InsertValue(EntityProperty entityProperty, T value)
                throws AppException {
            this.entityProperty = entityProperty;
            this.value = new WildValue<T>(value);
        }

    }
}
