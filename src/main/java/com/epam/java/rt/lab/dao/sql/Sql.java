package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.entity.BaseEntity;
import com.epam.java.rt.lab.entity.EntityProperty;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.StringCombiner;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.epam.java.rt.lab.dao.sql.SqlExceptionCode.SQL_TABLE_NAME_NOT_FOUND;
import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;
import static com.epam.java.rt.lab.exception.AppExceptionCode.PROPERTY_READ_ERROR;
import static com.epam.java.rt.lab.util.PropertyManager.*;

/**
 * {@code Sql} class defines abstract base sql statement
 * contains static fabric methods to create needed types
 * valueOf sql statements
 *
 * @see Properties
 * @see WildValue
 */
public abstract class Sql {

    private static final String SQL_PROPERTY_FILE = "sql.properties";

    /** {@code Properties} object which contains sql properties */
    private static Properties sqlProperties;
    /** {@code Lock} object used to implement thread safe initialization */
    private static Lock propertiesLock = new ReentrantLock();
    /**
     * {@code List} of {@code WildValue} objects
     */
    private List<WildValue> wildValueList = new ArrayList<>();

    /**
     * Initiates new {@code Sql} object
     */
    Sql() {
    }

    /**
     * Static method to initialize sql properties
     */
    public static void initSqlProperties() throws AppException {
        if (propertiesLock.tryLock()) {
            Sql.sqlProperties = new Properties();
            ClassLoader classLoader = Sql.class.getClassLoader();
            InputStream inputStream =
                    classLoader.getResourceAsStream(SQL_PROPERTY_FILE);
            try {
                sqlProperties.load(inputStream);
            } catch (IOException e) {
                String[] detailArray = {SQL_PROPERTY_FILE};
                throw new AppException(PROPERTY_READ_ERROR,
                        e.getMessage(), e.getCause(), detailArray);
            } finally {
                propertiesLock.unlock();
            }
        }
    }

    /**
     * Returns {@code String} representation of
     * sql property value
     *
     * @param key   {@code String} representation of property key
     * @return      {@code String} representation of property value
     */
    public static String getProperty(String key) {
        return sqlProperties.getProperty(key);
    }

    /**
     * Returns {@code List} of {@code Column} objects getDate
     * {@code Array} of {@code EntityProperty} objects
     *
     * @param entityPropertyArray       {@code Array} of {@code EntityProperty}
     *                                  objects
     * @return                          {@code List} of {@code Column} objects
     * @throws AppException
     *
     * @see Column
     * @see EntityProperty
     */
    private static List<Column> getColumnList(
            EntityProperty[] entityPropertyArray) throws AppException {
        if (entityPropertyArray == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        List<Column> columnList = new ArrayList<>();
        for (EntityProperty entityProperty : entityPropertyArray)
            columnList.add(getColumn(entityProperty));
        return columnList;
    }

    /**
     * Returns {@code Column} object from {@code EntityProperty} object
     *
     * @param entityProperty        {@code EntityProperty} object
     * @return                      {@code Column} object
     * @throws AppException
     *
     * @see Column
     * @see EntityProperty
     */
    static Column getColumn(EntityProperty entityProperty)
            throws AppException {
        if (entityProperty == null) throw new AppException(NULL_NOT_ALLOWED);
        String entityClassName = entityProperty.getEntityClass().getName();
        String tableName = getProperty(entityClassName);
        String columnName = getProperty(entityClassName
                + POINT + entityProperty);
        if (tableName == null || columnName == null) {
            String[] detailArray = {entityClassName, entityProperty.toString()};
            throw new AppException(SQL_TABLE_NAME_NOT_FOUND, detailArray);
        }
        return new Column(tableName, columnName);
    }

    /**
     * Returns {@code List} of {@code Column} objects getDate
     * {@code Array} of {@code String} representation valueOf
     * column's full names (including table name)
     *
     * @param tableAndColumnArray   {@code Array} of {@code String} objects
     * @return                      {@code List} of {@code Column} objects
     * @throws AppException
     *
     * @see Column
     */
    static List<Column> getColumnList(String[] tableAndColumnArray)
            throws AppException {
        if (tableAndColumnArray == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        List<Column> columnList = new ArrayList<>();
        for (String tableAndColumn : tableAndColumnArray)
            columnList.add(Column.from(tableAndColumn));
        return columnList;
    }

    /**
     * Fabric method to create {@code Insert} object with defined
     * {@code BaseEntity} object
     *
     * @param entity        {@code BaseEntity} object
     * @return              {@code Insert} object
     * @throws AppException
     *
     * @see BaseEntity
     */
    public static Insert insert(BaseEntity entity) throws AppException {
        if (entity == null) throw new AppException(NULL_NOT_ALLOWED);
        return new Insert(entity);
    }

    /**
     * Fabric method to create {@code Insert} object with defined
     * {@code Class} of entity
     *
     * @param propertyClass {@code Class} of entity
     * @return              {@code Insert} object
     * @throws AppException
     *
     * @see Insert
     */
    public static Insert insert(Class propertyClass) throws AppException {
        if (propertyClass == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        return new Insert(propertyClass);
    }

    /**
     * Fabric method to create {@code Select} object with defined
     * {@code Array} of {@code EntityProperty} objects
     *
     * @param entityPropertyArray   {@code Array} of {@code EntityProperty}
     *                                           objects
     * @return                      {@code Select} object
     * @throws AppException
     *
     * @see Select
     * @see EntityProperty
     */
    public static Select select(EntityProperty... entityPropertyArray)
            throws AppException {
        if (entityPropertyArray == null || entityPropertyArray.length == 0) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        return new Select(getColumnList(entityPropertyArray));
    }

    /**
     * Fabric method to create {@code Select} object with defined
     * {@code Class} of entity
     *
     * @param entityClass           {@code Class} of entity
     * @return                      {@code Select} object
     * @throws AppException
     *
     * @see Select
     */
    public static Select select(Class entityClass) throws AppException {
        if (entityClass == null) throw new AppException(NULL_NOT_ALLOWED);
        String propertyValue = getProperty(entityClass.getName()
                + POINT + ASTERISK);
        String[] tableAndColumnArray = StringCombiner.
                splitSpaceLessNames(propertyValue, COMMA);
        return new Select(getColumnList(tableAndColumnArray));
    }

    /**
     * Fabric method to create {@code Update} object with defined
     * {@code Class} of entity
     *
     * @param entityClass           {@code Class} of entity
     * @return                      {@code Update} object
     * @throws AppException
     *
     * @see Update
     */
    public static Update update(Class entityClass) throws AppException {
        if (entityClass == null) throw new AppException(NULL_NOT_ALLOWED);
        return new Update(entityClass);
    }

    /**
     * Fabric method to create {@code Delete} object with defined
     * {@code Class} of entity
     *
     * @param entityClass           {@code Class} of entity
     * @return                      {@code Delete} object
     * @throws AppException
     *
     * @see Delete
     */
    public static Delete delete(Class entityClass) throws AppException {
        if (entityClass == null) throw new AppException(NULL_NOT_ALLOWED);
        return new Delete(entityClass);
    }

    public static Count count(Class entityClass) throws AppException {
        if (entityClass == null) throw new AppException(NULL_NOT_ALLOWED);
        String propertyValue = getProperty(entityClass.getName()
                + POINT + ASTERISK);
        String[] tableAndColumnArray = StringCombiner.
                splitSpaceLessNames(propertyValue, COMMA);
        return new Count(getColumnList(tableAndColumnArray));
    }

    /**
     * Abstract method which should be implemented in inherited classes.
     * Base method to return sql statement.
     *
     * @return                  {@code String} representation of sql statement
     * @throws AppException
     */
    public abstract String create() throws AppException;

    /**
     * Returns {@code List} valueOf {@code WildValue} objects to replace values
     * in sql statements by wildcards and fill this {@code List} to set
     * prepared statement
     *
     * @return      {@code List} valueOf {@code WildValue} objects
     *
     * @see WildValue
     */
    public List<WildValue> getWildValueList() {
        return wildValueList;
    }

}
