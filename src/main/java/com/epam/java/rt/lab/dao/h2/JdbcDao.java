package com.epam.java.rt.lab.dao.h2;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.query.*;
import com.epam.java.rt.lab.dao.query.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

/**
 * service-ms
 */
public abstract class JdbcDao implements Dao {
    private static final Logger logger = LoggerFactory.getLogger(JdbcDao.class);
    public enum CRUD {CREATE, READ, UPDATE, DELETE, COUNT};
    private static Map<String, String> cachedQueryMap = new HashMap<>();
    private static Map<Type, Method> preparedStatementMethodMap = new HashMap<>();
    private Connection connection = null;
    private Long selectCount;

    private static void initPreparedStatementMethodMap() throws DaoException {
        try {
            if (preparedStatementMethodMap.size() == 0) {
                preparedStatementMethodMap.put(int.class, PreparedStatement.class.getMethod("setInt", int.class, int.class));
                preparedStatementMethodMap.put(Integer.class, PreparedStatement.class.getMethod("setInt", int.class, int.class));
                preparedStatementMethodMap.put(boolean.class, PreparedStatement.class.getMethod("setBoolean", int.class, boolean.class));
                preparedStatementMethodMap.put(Boolean.class, PreparedStatement.class.getMethod("setBoolean", int.class, boolean.class));
                preparedStatementMethodMap.put(byte.class, PreparedStatement.class.getMethod("setByte", int.class, byte.class));
                preparedStatementMethodMap.put(Byte.class, PreparedStatement.class.getMethod("setByte", int.class, byte.class));
                preparedStatementMethodMap.put(short.class, PreparedStatement.class.getMethod("setShort", int.class, short.class));
                preparedStatementMethodMap.put(Short.class, PreparedStatement.class.getMethod("setShort", int.class, short.class));
                preparedStatementMethodMap.put(long.class, PreparedStatement.class.getMethod("setLong", int.class, long.class));
                preparedStatementMethodMap.put(Long.class, PreparedStatement.class.getMethod("setLong", int.class, long.class));
                preparedStatementMethodMap.put(BigDecimal.class, PreparedStatement.class.getMethod("setBigDecimal", int.class, BigDecimal.class));
                preparedStatementMethodMap.put(double.class, PreparedStatement.class.getMethod("setDouble", int.class, double.class));
                preparedStatementMethodMap.put(Double.class, PreparedStatement.class.getMethod("setDouble", int.class, double.class));
                preparedStatementMethodMap.put(float.class, PreparedStatement.class.getMethod("setFloat", int.class, float.class));
                preparedStatementMethodMap.put(Float.class, PreparedStatement.class.getMethod("setFloat", int.class, float.class));
//            preparedStatementMethodMap.put(Time.class, PreparedStatement.class.getMethod("setTime", int.class, Time.class));
//            preparedStatementMethodMap.put(Date.class, PreparedStatement.class.getMethod("setDate", int.class, Date.class));
//            preparedStatementMethodMap.put(Timestamp.class, PreparedStatement.class.getMethod("setTimestamp", int.class, Timestamp.class));
                preparedStatementMethodMap.put(String.class, PreparedStatement.class.getMethod("setString", int.class, String.class));
                preparedStatementMethodMap.put(Blob.class, PreparedStatement.class.getMethod("setBlob", int.class, Blob.class));
                preparedStatementMethodMap.put(Clob.class, PreparedStatement.class.getMethod("setClob", int.class, Clob.class));
                preparedStatementMethodMap.put(FileInputStream.class, PreparedStatement.class.getMethod("setBinaryStream", int.class, InputStream.class));
            }
        } catch (NoSuchMethodException e) {
            throw new DaoException("exception.dao.jdbc.init-prepared-statement-method-map", e.getCause());
        }
    }

    public JdbcDao(Connection connection) throws DaoException {
        logger.debug("JdbcDao: {}", connection);
        this.connection = connection;
        if (preparedStatementMethodMap.size() == 0) initPreparedStatementMethodMap();
    }

    Connection getConnection() {
        return connection;
    }

    public <T> Query getCachedQuery(CRUD crud, T entity,
                                     String fieldNames, String columnNames, String setNames,
                                     String order, Long offset, Long count) throws DaoException {
        String queryKey = ""; //new StringBuilder().append(crud).append(entity.getClass().getSimpleName())
//                .append(fieldNames).append(columnNames).append(setNames)
//                .append(order).append((offset != null && count != null) ? "LIMIT" : "").toString();
        String queryString = JdbcDao.cachedQueryMap.get(queryKey);
        queryString = null; // TODO: fix sql caching
//        logger.debug("queryKey = {}\nqueryString = {}", queryKey, queryString);
        Query query = null;
        switch (crud) {
            case CREATE:
                break;
            case READ:
                query = new Select(getEntityTableName(), columnNames, null, null);
                fitQueryToEntity(query, entity, setNames, fieldNames);
                query.setSql(queryString == null ? query.create() : queryString);
                break;
            case UPDATE:
                query = new Update(getEntityTableName());
                fitQueryToEntity(query, entity, setNames, fieldNames);
                query.setSql(queryString == null ? query.create() : queryString);
                break;
            case DELETE:
                break;
            case COUNT:
                query = new Select(getEntityTableName(), columnNames, offset, count);
                query.setSql(queryString == null ? query.createCount() : queryString);
                break;
        }
        if (queryString == null && query != null) {
            JdbcDao.cachedQueryMap.put(queryKey, query.getSql());
            queryString = query.getSql();
        }
        if (crud.equals(CRUD.READ) && offset != null && count != null) {
            queryString.concat(new StringBuilder().append(" LIMIT ").append(offset).append(", ").append(count).toString());
            query.setSql(queryString);
        }
        logger.debug(queryString);
        return query;
    }

    private <T> void fitQueryToEntity(Query query, T entity, String setNames, String fieldNames) throws DaoException {
        try {
            if (setNames != null) {
                List<Set> setList = new ArrayList<>();
                setNames = setNames.replaceAll(" ", "");
                for (String setName : setNames.split(",")) {
                    Field field = getField(entity.getClass(), setName);
                    setList.add(getEntitySet(entity, field));
                }
                query.setSetList(setList);
            }
            if (fieldNames != null) {
                List<Column> columnList = new ArrayList<>();
                fieldNames = fieldNames.replaceAll(" ", "");
                for (String fieldName : fieldNames.split(",")) {
                    Field field = getField(entity.getClass(), fieldName);
                    columnList.add(getEntityColumn(entity, field));
                }
                query.setColumnList(columnList);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.fit-query-to-entity", e.getCause());
        }
    }

    private Field getField(Class entityClass, String fieldName) throws NoSuchFieldException {
        Field field = null;
        while (entityClass != null) {
            try {
                field = entityClass.getDeclaredField(fieldName);
                return field;
            } catch (NoSuchFieldException e) {
                entityClass = entityClass.getSuperclass();
            }
        }
        throw new NoSuchFieldException("exception.dao.query.reflect.get-field");
    }

    <T> T fieldValue(Field field, Object entity) throws IllegalAccessException {
        boolean isAccessible = true;
        try {
            isAccessible = field.isAccessible();
            if (isAccessible) return (T) field.get(entity);
            field.setAccessible(true);
            return (T) field.get(entity);
        } finally {
            if (!isAccessible) field.setAccessible(false);
        }
    }

    PreparedStatement setPreparedStatementValues(PreparedStatement preparedStatement,
                                                 List<Set> setList, List<Column> columnList) throws DaoException {
        logger.debug("STMT: {}", preparedStatement);
        try {
            int valueIndex = 1;
            Method preparedStatementMethod;
            if (setList != null) {
                for (Set set : setList) {
                    if (set.value == null) {
                        preparedStatement.setNull(valueIndex, Types.NULL);
                    } else {
                        preparedStatementMethod = preparedStatementMethodMap.get(set.value.getClass());
                        if (preparedStatementMethod == null)
                            throw new DaoException("exception.dao.jdbc.set-prepared-statement-values.method");
                        preparedStatementMethod.invoke(preparedStatement, valueIndex, set.value);
                    }
                    valueIndex++;
                }
            }
            if (columnList != null) {
                for (Column column : columnList) {
                    if (column.value != null && !column.isColumn) {
                        preparedStatementMethod = preparedStatementMethodMap.get(column.value.getClass());
                        if (preparedStatementMethod == null)
                            throw new DaoException("exception.dao.jdbc.set-prepared-statement-values.method");
                        preparedStatementMethod.invoke(preparedStatement, valueIndex, column.value);
                        valueIndex++;
                    }
                }
            }
            return preparedStatement;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.jdbc.set-prepared-statement-values.invoke");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.jdbc.set-prepared-statement-values.set-null-value");
        }
    }

    PreparedStatement setPreparedStatementValues(PreparedStatement preparedStatement,
                                                 Query query) throws DaoException {
        return setPreparedStatementValues(preparedStatement, query.getSetList(), query.getColumnList());
    }

    PreparedStatement setPreparedStatementValues(PreparedStatement preparedStatement,
                                                 List<?> setOrColumnList) throws DaoException {
        // it could be List<T> instead List<?>, and then it should be <T extend someParentClass>, but in this case
        // it is too expensive to create extra number of classes which do almost similar job
        if (setOrColumnList == null) return setPreparedStatementValues(preparedStatement, null, null);
        switch (setOrColumnList.get(0).getClass().getSimpleName()) {
            case "Set":
                return setPreparedStatementValues(preparedStatement, (List<Set>) setOrColumnList, null);
            case "Column":
                return setPreparedStatementValues(preparedStatement, null, (List<Column>) setOrColumnList);
        }
        throw new DaoException("exception.dao.jdbc.set-prepared-statement-values.type-error");
    }

    ResultSet getGeneratedKeysAfterUpdate(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.executeUpdate();
        return preparedStatement.getGeneratedKeys();
    }




//
//
//
//    PreparedStatement rawQuery(CRUD crud, String sqlString, List<Column> columnList) throws DaoException {
//        return rawQuery(crud, sqlString, columnList, PreparedStatement.NO_GENERATED_KEYS);
//    }
//
//    PreparedStatement rawQuery(CRUD crud, String sqlString, List<Column> columnList, int autoGeneratedKeys) throws DaoException {
//        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlString);
//
//        ) {
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        (JdbcDao.preparedStatementMap, sqlString, autoGeneratedKeys);
//        setPreparedStatementValues(preparedStatement, columnList);
//        return preparedStatement;
//    }














//    private PreparedStatement getPreparedStatement(Map<String, PreparedStatement> preparedStatementMap,
//                                                   String sqlQuery) throws DaoException {
//        return getPreparedStatement(preparedStatementMap, sqlQuery, PreparedStatement.NO_GENERATED_KEYS);
//    }
//
//    private PreparedStatement getPreparedStatement(Map<String, PreparedStatement> preparedStatementMap,
//                                                   String sqlQuery, int autoGeneratedKeys) throws DaoException {
//        try {
//            PreparedStatement preparedStatement = preparedStatementMap.get(sqlQuery);
//            if (preparedStatement != null) {
//                preparedStatement.clearParameters();
//                preparedStatement.cancel();
//                return preparedStatement;
//            }
//            if (autoGeneratedKeys > 0) {
//                preparedStatement = this.connection.prepareStatement(sqlQuery, autoGeneratedKeys);
//            } else {
//                preparedStatement = this.connection.prepareStatement(sqlQuery);
//            }
//            preparedStatementMap.put(sqlQuery, preparedStatement);
//            return preparedStatement;
//        } catch (SQLException e) {
//            throw new DaoException("exception.dao.jdbc.get-prepared-statement", e.getCause());
//        }
//    }
//
//    void setPreparedStatementValues(PreparedStatement preparedStatement,
//                                    List<Column> columnList) throws DaoException {
//        setPreparedStatementValues(preparedStatement, null, columnList);
//    }
//
//    void setPreparedStatementValues(PreparedStatement preparedStatement, List<Set> setList,
//                                    List<Column> columnList) throws DaoException {
//        logger.debug("STMT: {}", preparedStatement);
//        try {
//            int valueIndex = 1;
//            Method preparedStatementMethod;
//            if (setList != null) {
//                for (Set set : setList) {
//                    if (set.value == null) {
//                        preparedStatement.setNull(valueIndex, Types.NULL);
//                    } else {
//                        preparedStatementMethod = preparedStatementMethodMap.get(set.value.getClass());
//                        if (preparedStatementMethod == null)
//                            throw new DaoException("exception.dao.jdbc.set-prepared-statement-values.method");
//                        preparedStatementMethod.invoke(preparedStatement, valueIndex, set.value);
//                    }
//                    valueIndex++;
//                }
//            }
//            if (columnList != null) {
//                for (Column column : columnList) {
//                    if (column.value != null && !column.isColumn) {
//                        preparedStatementMethod = preparedStatementMethodMap.get(column.value.getClass());
//                        if (preparedStatementMethod == null)
//                            throw new DaoException("exception.dao.jdbc.set-prepared-statement-values.method");
//                        preparedStatementMethod.invoke(preparedStatement, valueIndex, column.value);
//                        valueIndex++;
//                    }
//                }
//            }
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
//            throw new DaoException("exception.dao.jdbc.set-prepared-statement-values.invoke");
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new DaoException("exception.dao.jdbc.set-prepared-statement-values.set-null-value");
//        }
//    }

//    <T> T fieldValue(Field field, Object entity) throws IllegalAccessException {
//        boolean isAccessible = true;
//        try {
//            isAccessible = field.isAccessible();
//            if (isAccessible) return (T) field.get(entity);
//            field.setAccessible(true);
//            return (T) field.get(entity);
//        } finally {
//            if (!isAccessible) field.setAccessible(false);
//        }
//    }
//
//    private <T> PreparedStatement query(Query query, T entity, String fieldNames, String setNames) throws DaoException {
//        try {
//            if (setNames != null) {
//                setNames = setNames.replaceAll(" ", "");
//                for (String setName : setNames.split(",")) {
//                    Field field = getField(entity.getClass(), setName);
//                    query.getSetList().add(getEntitySet(entity, field));
//                }
//            }
//            if (fieldNames != null) {
//                fieldNames = fieldNames.replaceAll(" ", "");
//                for (String fieldName : fieldNames.split(",")) {
//                    Field field = getField(entity.getClass(), fieldName);
//                    query.getColumnList().add(getEntityColumn(entity, field));
//                }
//            }
//            PreparedStatement preparedStatement = getPreparedStatement
//                    (JdbcDao.preparedStatementMap, query.create());
////            setPreparedStatementValues(preparedStatement, query.getSetList(), query.getColumnList());
//            return preparedStatement;
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//            throw new DaoException("exception.dao.jdbc.query.get-declared-field", e.getCause());
//        }
//    }
//
//    private <T> ResultSet selectQuery(T entity, String fieldNames, String columnNames) throws DaoException {
//        return selectQuery(entity, fieldNames, columnNames, null, null);
//    }
//
//    private <T> ResultSet selectQuery(T entity, String fieldNames, String columnNames, Long offset, Long count)
//            throws DaoException {
//        try {
//            Query select = new Select(getEntityTableName(), columnNames, offset, count);
//            if (offset != null && count != null) {
//                PreparedStatement countStatement = connection.prepareStatement(select.createCount());
//                countStatement.execute();
//                if (countStatement.getResultSet().next())
//                    this.selectCount = countStatement.getResultSet().getLong("count");
//                countStatement.close();
//            }
//            PreparedStatement preparedStatement = query(select, entity, fieldNames, null);
//            preparedStatement.execute();
//            return preparedStatement.getResultSet();
//        } catch (SQLException e) {
//            throw new DaoException("exception.dao.jdbc.selectQuery.get-result-set", e.getCause());
//        }
//    }
//



    @Override
    public <T> T getFirst(T entity, String fieldNames, String order) throws DaoException {
        return getFirst(entity, fieldNames, null, order);
    }

    @Override
    public <T> T getFirst(T entity, String fieldNames, String columnNames, String order) throws DaoException {
        Query query = getCachedQuery(CRUD.READ, entity, fieldNames, columnNames, null, order, 0L, 1L);
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query.getSql());
             ResultSet resultSet = setPreparedStatementValues(preparedStatement, query).executeQuery();) {
            if (resultSet == null || !resultSet.first()) return null;
            return getEntityFromResultSet(entity, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.jdbc.get-first.get-result-set", e.getCause());
        }
    }

    @Override
    public <T> List<T> getAll(String order) throws DaoException {
        return getAll(null, null, null, order, null, null);
    }

    @Override
    public <T> List<T> getAll(T entity, String fieldNames, String order) throws DaoException {
        return getAll(entity, fieldNames, null, order, null, null);
    }

    @Override
    public <T> List<T> getAll(T entity, String fieldNames, String columnNames, String order) throws DaoException {
        return getAll(entity, fieldNames, columnNames, order, null, null);
    }

    @Override
    public <T> List<T> getAll(T entity, String fieldNames, String columnNames, String order, Long offset, Long count) throws DaoException {
        Query query = getCachedQuery(CRUD.READ, entity, fieldNames, columnNames, null, order, 0L, 1L);
        if (offset != null && count != null) {
            try (PreparedStatement countStatement = getConnection().prepareStatement(query.createCount());
                 ResultSet resultSet = countStatement.executeQuery();) {
                if (resultSet == null) return null;
                if (countStatement.getResultSet().next())
                    this.selectCount = countStatement.getResultSet().getLong("count");
            } catch (SQLException eCount) {
                throw new DaoException("exception.dao.get-all.count", eCount.getCause());
            }
        }
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query.getSql());
             ResultSet resultSet = setPreparedStatementValues(preparedStatement, query).executeQuery();) {
            if (resultSet == null) return null;
            List<T> entityList = new ArrayList<>();
            while(resultSet.next()) entityList.add(getEntityFromResultSet(entity, resultSet));
            return entityList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.get-all");
        }
    }

    @Override
    public Long getSelectCount() {
        return this.selectCount;
    }

    abstract String getEntityTableName();

    abstract <T> Column getEntityColumn(T entity, Field field) throws DaoException;

    abstract <T> T getEntityFromResultSet(T entity, ResultSet resultSet) throws DaoException;

    private <T> int updateQuery(T entity, String fieldNames, String setNames) throws DaoException {
        Query query = getCachedQuery(CRUD.UPDATE, entity, fieldNames, null, setNames, null, null, null);
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query.getSql());) {
            return setPreparedStatementValues(preparedStatement, query).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.update-query.sql", e.getCause());
        }
    }

    @Override
    public <T> int update(T entity, String fieldNames, String setNames) throws DaoException {
        return updateQuery(entity, fieldNames, setNames) + relUpdate(entity, setNames);
    }

    abstract <T> Set getEntitySet(T entity, Field field) throws DaoException;

    <T> int relUpdate(T entity, String setNames) throws DaoException {
        return 0;
    }

    @Override
    public <T> Object getRelEntity(T entity, String relEntityName) throws DaoException {
        return null;
    }

    @Override
    public <T> int putRelEntity(T entity, String relEntityName, Object relEntity) throws DaoException {
        return 0;
    }

    @Override
    public <T> int removeRelEntity(T entity, String relEntityName) throws DaoException {
        return 0;
    }

}
