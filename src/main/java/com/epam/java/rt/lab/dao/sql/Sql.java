package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.EntityProperty;
import com.epam.java.rt.lab.util.GlobalProperties;
import com.epam.java.rt.lab.util.StringArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * service-ms
 */
public class Sql {

    public static final String SIGN_POINT = ".";
    public static final String SIGN_COMMA = ",";
    public static final String SIGN_EQUAL = ",";
    public static final String ALL_COLUMNS = "*";

    private static Properties sqlProperties = new Properties();
    private static Lock propertiesLock = new ReentrantLock();

    List<WildValue> wildValueList;

    Sql() {
        this.wildValueList = new ArrayList<>();
    }

    private static void init() {
        if (propertiesLock.tryLock()) {
            try {
                sqlProperties.load(GlobalProperties.class.getClassLoader().getResourceAsStream("sql.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            propertiesLock.unlock();
        }
    }

    public static String getProperty(String key) {
        if (sqlProperties.size() == 0) init();
        return sqlProperties.getProperty(key);
    }

    private static List<Column> getColumnList(EntityProperty[] entityPropertyArray) {
        List<Column> columnList = new ArrayList<>();
        for (EntityProperty entityProperty : entityPropertyArray)
            columnList.add(getColumn(entityProperty));
        return columnList;
    }

    static Column getColumn(EntityProperty entityProperty) {
        String entityClassName = entityProperty.getClass().getSuperclass().getSimpleName();
        return new Column(
                getProperty(entityClassName),
                getProperty(entityClassName.concat(SIGN_POINT).concat(entityProperty.toString()))
        );
    }

    private static List<Column> getColumnList(String[] tableAndColumnArray) {
        List<Column> columnList = new ArrayList<>();
        for (String tableAndColumn : tableAndColumnArray) {
            String[] split = tableAndColumn.split("\\.");
            columnList.add(new Column(
                    split[0],
                    split[1]
            ));
        }
        return columnList;
    }

    // base clauses

    public static Insert insert() {
        return new Insert();
    }

    /**
     *
     * @param entityPropertyArray
     * @return
     * @throws DaoException
     */
    public static Select select(EntityProperty... entityPropertyArray) throws DaoException {
        if (entityPropertyArray.length == 0)
            throw new DaoException("exception.dao.sql.select.empty-entity-array");
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
            throw new DaoException("exception.dao.sql.select.null-entity-class");
        return new Select(getColumnList(
                StringArray.splitSpaceLessNames(
                        getProperty(
                                entityClass
                                        .getSimpleName()
                                        .concat(SIGN_POINT)
                                        .concat(ALL_COLUMNS)
                        ),
                        SIGN_COMMA
                )
        ));
    }

    public static Update update() {
        return new Update();
    }

    public static Delete delete() {
        return new Delete();
    }

}
