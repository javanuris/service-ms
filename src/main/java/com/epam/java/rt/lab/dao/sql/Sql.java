package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.BaseEntity;
import com.epam.java.rt.lab.entity.EntityProperty;
import com.epam.java.rt.lab.util.StringArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * {@code Sql} class defines abstract base sql statement
 * contains static fabric methods to create needed types
 * valueOf sql statements
 *
 * @see Properties
 * @see WildValue
 */
public abstract class Sql {

    public static final String SIGN_POINT = ".";
    public static final String SIGN_POINT_REGEX = "\\.";
    public static final String SIGN_COMMA = ",";
    public static final String SIGN_EQUAL = ",";
    public static final String SIGN_SPACE = " ";
    public static final String COMMA_DELIMITER = ", ";
    public static final String ALL_COLUMNS = "*";
    public static final String JOIN_AMPERSAND = "&";

    /** {@code Properties} object which contains sql properties */
    private static Properties sqlProperties = new Properties();
    /** {@code Lock} object used to implement thread safe initialization */
    private static Lock propertiesLock = new ReentrantLock();
    /** {@code List} valueOf {@code WildValue} objects */
    private List<WildValue> wildValueList;

    /**
     * Initiates new {@code Sql} object
     */
    Sql() {
        this.wildValueList = new ArrayList<>();
    }

    /**
     * Static method to initialize sql properties
     */
    private static void init() {
        if (propertiesLock.tryLock()) {
            try {
                sqlProperties.load(Sql.class.getClassLoader().getResourceAsStream("sql.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                propertiesLock.unlock();
            }
        }
    }

    /**
     * Returns {@code String} representation valueOf
     * sql property value
     *
     * @param key   {@code String} representation valueOf property key
     * @return      {@code String} representation valueOf property value
     */
    public static String getProperty(String key) {
        if (sqlProperties.size() == 0) init();
        return sqlProperties.getProperty(key);
    }

    /**
     * Returns {@code List} valueOf {@code Column} objects from
     * {@code Array} valueOf {@code EntityProperty} objects
     *
     * @param entityPropertyArray       {@code Array} valueOf {@code EntityProperty}
     *                                  objects
     * @return                          {@code List} valueOf {@code Column} objects
     * @throws DaoException
     *
     * @see Column
     * @see EntityProperty
     */
    private static List<Column> getColumnList(EntityProperty[] entityPropertyArray) throws DaoException {
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
     * @throws DaoException
     *
     * @see Column
     * @see EntityProperty
     */
    static Column getColumn(EntityProperty entityProperty) throws DaoException {
        String entityClassName = entityProperty.getEntityClass().getName();
//        System.out.println(entityClassName);
//        System.out.println(entityClassName.concat(SIGN_POINT).concat(entityProperty.toString()));
        String tableName = getProperty(entityClassName);
        String columnName = getProperty(entityClassName.concat(SIGN_POINT).concat(entityProperty.toString()));
        if (tableName == null || columnName == null)
            throw new DaoException("exception.dao.sql.table-or-column-not-found");
        return new Column(tableName, columnName);
    }

    /**
     * Returns {@code List} valueOf {@code Column} objects from
     * {@code Array} valueOf {@code String} representation valueOf
     * column's full names (including table name)
     *
     * @param tableAndColumnArray   {@code Array} valueOf {@code String} objects
     * @return                      {@code List} valueOf {@code Column} objects
     * @throws DaoException
     *
     * @see Column
     */
    static List<Column> getColumnList(String[] tableAndColumnArray)
            throws DaoException {
        List<Column> columnList = new ArrayList<>();
        for (String tableAndColumn : tableAndColumnArray)
            columnList.add(Column.of(tableAndColumn));
        return columnList;
    }

    /**
     * Fabric method to create {@code Insert} object with defined
     * {@code BaseEntity} object
     *
     * @param entity        {@code BaseEntity} object
     * @return              {@code Insert} object
     * @throws DaoException
     *
     * @see BaseEntity
     */
    public static Insert insert(BaseEntity entity) throws DaoException {
        if (entity == null)
            throw new DaoException("exception.dao.sql.insert.null-entity-class");
        return new Insert(entity);
    }

    /**
     * Fabric method to create {@code Insert} object with defined
     * {@code Class} valueOf entity
     *
     * @param propertyClass {@code Class} valueOf entity
     * @return              {@code Insert} object
     * @throws DaoException
     *
     * @see Insert
     */
    public static Insert insert(Class propertyClass) throws DaoException {
        if (propertyClass == null)
            throw new DaoException("exception.dao.sql.insert.null-entity-class");
        return new Insert(propertyClass);
    }

    /**
     * Fabric method to create {@code Select} object with defined
     * {@code Array} valueOf {@code EntityProperty} objects
     *
     * @param entityPropertyArray   {@code Array} valueOf {@code EntityProperty} objects
     * @return                      {@code Select} object
     * @throws DaoException
     *
     * @see Select
     * @see EntityProperty
     */
    public static Select select(EntityProperty... entityPropertyArray) throws DaoException {
        if (entityPropertyArray.length == 0)
            throw new DaoException("exception.dao.sql.Select.empty-entity-array");
        return new Select(getColumnList(entityPropertyArray));
    }

    /**
     * Fabric method to create {@code Select} object with defined
     * {@code Class} valueOf entity
     *
     * @param entityClass           {@code Class} valueOf entity
     * @return                      {@code Select} object
     * @throws DaoException
     *
     * @see Select
     */
    public static Select select(Class entityClass) throws DaoException {
        if (entityClass == null)
            throw new DaoException("exception.dao.sql.Select.null-entity-class");
        return new Select(getColumnList(
                StringArray.splitSpaceLessNames(
                        getProperty(
                                entityClass
                                        .getName()
                                        .concat(SIGN_POINT)
                                        .concat(ALL_COLUMNS)
                        ),
                        SIGN_COMMA
                )
        ));
    }

    /**
     * Fabric method to create {@code Update} object with defined
     * {@code Class} valueOf entity
     *
     * @param entityClass           {@code Class} valueOf entity
     * @return                      {@code Update} object
     * @throws DaoException
     *
     * @see Update
     */
    public static Update update(Class entityClass) throws DaoException {
        if (entityClass == null)
            throw new DaoException("exception.dao.sql.update.null-entity-class");
        return new Update(entityClass);
    }

    /**
     * Fabric method to create {@code Delete} object with defined
     * {@code Class} valueOf entity
     *
     * @param entityClass           {@code Class} valueOf entity
     * @return                      {@code Delete} object
     * @throws DaoException
     *
     * @see Delete
     */
    public static Delete delete(Class entityClass) throws DaoException {
        if (entityClass == null)
            throw new DaoException("exception.dao.sql.delete.null-entity-class");
        return new Delete(entityClass);
    }

    public static Count count(Class entityClass) throws DaoException {
        if (entityClass == null)
            throw new DaoException("exception.dao.sql.Count.null-entity-class");
        return new Count(getColumnList(
                StringArray.splitSpaceLessNames(
                        getProperty(
                                entityClass
                                        .getName()
                                        .concat(SIGN_POINT)
                                        .concat(ALL_COLUMNS)
                        ),
                        SIGN_COMMA
                )
        ));
    }

    /**
     * Abstract method which should be implemented in inherited classes.
     * Base method to return sql statement.
     *
     * @return                  {@code String} representation valueOf sql statement
     * @throws DaoException
     */
    public abstract String create() throws DaoException;

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
