package com.epam.java.rt.lab.dao.h2;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.Dao_;
import com.epam.java.rt.lab.dao.query.Column;
import com.epam.java.rt.lab.dao.query.Select;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.Permission;
import com.epam.java.rt.lab.entity.rbac.User;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * service-ms
 */
abstract class JdbcDao implements Dao_ {
    private static Map<Type, Method> preparedStatementMethodMap = new HashMap<>();
    private static Map<String, PreparedStatement> preparedStatementMap = new HashMap<>();
    private Connection connection = null;
    private List<Join> joinList = null;

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
            }
        } catch (NoSuchMethodException e) {
            throw new DaoException("exception.dao.jdbc.init-prepared-statement-method-map", e.getCause());
        }
    }

    JdbcDao(Connection connection) throws DaoException {
        this.connection = connection;
        if (preparedStatementMethodMap.size() == 0) initPreparedStatementMethodMap();
    }

    PreparedStatement getPreparedStatement(Map<String, PreparedStatement> preparedStatementMap,
                                           String key, String sqlQuery) throws DaoException {
        try {
            PreparedStatement preparedStatement = preparedStatementMap.get(key);
            if (preparedStatement != null) return preparedStatement;
            preparedStatement = this.connection.prepareStatement(sqlQuery);
            preparedStatementMap.put(key, preparedStatement);
            return preparedStatement;
        } catch (SQLException e) {
            throw new DaoException("exception.dao.jdbc.get-prepared-statement", e.getCause());
        }
    }

    void setPreparedStatementValues(PreparedStatement preparedStatement, List<Column> columnList) throws DaoException {
        try {
            int columnIndex = 1;
            Method preparedStatementMethod;
            for (Column column : columnList) {
                if (column.value != null) {
                    preparedStatementMethod = preparedStatementMethodMap.get(column.value.getClass());
                    if (preparedStatementMethod == null)
                        throw new DaoException("exception.dao.jdbc.set-prepared-statement-values.method");
                    preparedStatementMethod.invoke(preparedStatement, columnIndex, column.value);
                    columnIndex++;
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new DaoException("exception.dao.jdbc.set-prepared-statement-values.invoke");
        }
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

    <T> ResultSet query(String preKey, T entity, String fieldNames, String columnNames) throws DaoException {
        try {
            Select select = new Select("User", columnNames);
            fieldNames = fieldNames.replaceAll(" ", "");
            preKey = preKey.concat(fieldNames.replaceAll(",", "-"));
            for (String fieldName : fieldNames.split(",")) {
                Field field = entity.getClass().getDeclaredField(fieldName);
                select.columnList.add(entityColumn(entity, field));
            }
            PreparedStatement preparedStatement = getPreparedStatement
                    (JdbcDao.preparedStatementMap, preKey, select.create());
            setPreparedStatementValues(preparedStatement, select.columnList);
            return preparedStatement.getResultSet();
        } catch (NoSuchFieldException e) {
            throw new DaoException("exception.dao.jdbc.query.get-declared-field", e.getCause());
        } catch (SQLException e) {
            throw new DaoException("exception.dao.jdbc.query.get-result-set", e.getCause());
        }
    }

    @Override
    public <T> T getFirst(T entity, String fieldNames) throws DaoException {
        return getFirst(entity, fieldNames, null);
    }

    @Override
    public <T> T getFirst(T entity, String fieldNames, String columnNames) throws DaoException {
        try {
            ResultSet resultSet = query(preparedStatementMapKeyPrefix()
                    .concat(fieldNames), entity, fieldNames, columnNames);
            if (resultSet == null || !resultSet.first()) return null;
            return (T) getEntityFromResultSet(entity, resultSet);
        } catch (SQLException e) {
            throw new DaoException("exception.dao.jdbc.get-first.get-result-set", e.getCause());
        }
    }

    @Override
    public <T> List<T> getAll(T entity, String fieldNames) throws DaoException {
        return getAll(entity, fieldNames, null);
    }

    @Override
    public <T> List<T> getAll(T entity, String fieldNames, String columnNames) throws DaoException {
        try {
            ResultSet resultSet = query(preparedStatementMapKeyPrefix()
                    .concat(fieldNames), entity, fieldNames, columnNames);
            if (resultSet == null) return null;
            List<T> entityList = new ArrayList<>();
            while(resultSet.next()) {
                entityList.add(getEntityFromResultSet(entity, resultSet));
                entity = (T) entity.getClass().newInstance();
            }
            return entityList;
        } catch (SQLException e) {
            throw new DaoException("exception.dao.jdbc.get-all.get-result-set", e.getCause());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new DaoException("exception.dao.jdbc.get-all.instance", e.getCause());
        }
    }

    abstract String preparedStatementMapKeyPrefix();

    abstract <T> Column entityColumn(T entity, Field field) throws DaoException;

    abstract <T> T getEntityFromResultSet(T entity, ResultSet resultSet) throws DaoException;




    <T> void add(T entity, String fieldNames) {

    }

    private class Join {

    }

}
