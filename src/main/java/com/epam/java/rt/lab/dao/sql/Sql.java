package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.BaseEntity;
import com.epam.java.rt.lab.entity.EntityProperty;
import com.epam.java.rt.lab.util.GlobalProperties;
import com.epam.java.rt.lab.util.StringArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * service-ms
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

    private static Properties sqlProperties = new Properties();
    private static Lock propertiesLock = new ReentrantLock();

    private List<WildValue> wildValueList;

    Sql() {
        this.wildValueList = new ArrayList<>();
    }

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

    public static String getProperty(String key) {
        if (sqlProperties.size() == 0) init();
        return sqlProperties.getProperty(key);
    }

    private static List<Column> getColumnList(EntityProperty[] entityPropertyArray) throws DaoException {
        List<Column> columnList = new ArrayList<>();
        for (EntityProperty entityProperty : entityPropertyArray)
            columnList.add(getColumn(entityProperty));
        return columnList;
    }

    static Column getColumn(EntityProperty entityProperty) throws DaoException {
        String entityClassName = entityProperty.getEntityClass().getName();
        System.out.println(entityClassName);
        System.out.println(entityClassName.concat(SIGN_POINT).concat(entityProperty.toString()));
        String tableName = getProperty(entityClassName);
        String columnName = getProperty(entityClassName.concat(SIGN_POINT).concat(entityProperty.toString()));
        if (tableName == null || columnName == null)
            throw new DaoException("exception.dao.sql.table-or-column-not-found");
        return new Column(tableName, columnName);
    }

    static List<Column> getColumnList(String[] tableAndColumnArray)
            throws DaoException {
        List<Column> columnList = new ArrayList<>();
        for (String tableAndColumn : tableAndColumnArray)
            columnList.add(Column.of(tableAndColumn));
        return columnList;
    }

    // base clauses

    /**
     *
     * @param entity
     * @return
     * @throws DaoException
     */
    public static Insert insert(BaseEntity entity) throws DaoException {
        if (entity == null)
            throw new DaoException("exception.dao.sql.insert.null-entity-class");
        return new Insert(entity);
    }

    /**
     *
     * @param
     * @return
     * @throws DaoException
     */
    public static Insert insert(Class propertyClass) throws DaoException {
        if (propertyClass == null)
            throw new DaoException("exception.dao.sql.insert.null-entity-class");
        return new Insert(propertyClass);
    }

    /**
     *
     * @param entityPropertyArray
     * @return
     * @throws DaoException
     */
    public static Select select(EntityProperty... entityPropertyArray) throws DaoException {
        if (entityPropertyArray.length == 0)
            throw new DaoException("exception.dao.sql.Select.empty-entity-array");
        return new Select(getColumnList(entityPropertyArray));
    }

    /**
     *
     * @param entityClass
     * @return
     * @throws DaoException
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

    public static Update update(Class entityClass) throws DaoException {
        if (entityClass == null)
            throw new DaoException("exception.dao.sql.update.null-entity-class");
        return new Update(entityClass);
    }

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

    // resulting methods

    public abstract String create() throws DaoException;

    public List<WildValue> getWildValueList() {
        return wildValueList;
    }

}
