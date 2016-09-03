package com.epam.java.rt.lab.dao.h2;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.rbac.Login;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * service-ms
 */
public abstract class H2JdbcDao implements Dao {
    private static final Logger logger = LoggerFactory.getLogger(H2JdbcDao.class);
    private static Map<Type, Method> preparedStatementMethodMap = new HashMap<>();
    private static String daoCreate;
    private static String daoReadBy;
    private static String daoUpdate;
    private static String daoDelete;
    private PreparedStatement preparedStatement;

    public void resetProperties() throws DaoException {
        if (daoCreate == null || daoReadBy == null || daoUpdate == null || daoDelete == null)
            resetProperties("h2crud.properties");
    }

    public void resetProperties(String fileName) throws DaoException {
        try {
            Properties properties = new Properties();
            properties.load(H2JdbcDao.class.getClassLoader().getResourceAsStream(fileName));
            String daoCreate = properties.getProperty("dao.create");
            String daoReadBy = properties.getProperty("dao.read.by");
            String daoUpdate = properties.getProperty("dao.update");
            String daoDelete = properties.getProperty("dao.delete");
            if (daoCreate == null || daoReadBy == null || daoUpdate == null || daoDelete == null)
                throw new DaoException("h2 database CRUD properties error");
            H2JdbcDao.daoCreate = daoCreate;
            H2JdbcDao.daoReadBy = daoReadBy;
            H2JdbcDao.daoUpdate = daoUpdate;
            H2JdbcDao.daoDelete = daoDelete;
            setPreparedStatementMethodMap();
        } catch (IOException e) {
            throw new DaoException(e.getMessage());
        }
    }

    private void setPreparedStatementMethodMap() throws DaoException {
        logger.debug("initializing prepared statement methods");
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
            logger.error("no such prepared statement method");
            throw new DaoException(e.getMessage());
        }
    }

    static String getDaoReadBy() {
        return daoReadBy;
    }

    static Method getPreparedStatementMethod(Type valueType) {
        return preparedStatementMethodMap.get(valueType);
    }

    abstract String getEntityTableName();

    abstract Object getEntity(ResultSet resultSet) throws SQLException;

    @Override
    public void close() throws DaoException {
        try {
            if (preparedStatement != null) preparedStatement.close();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        if (preparedStatement == null) return null;
        return preparedStatement.getResultSet();
    }

    @Override
    public <T> T first() throws SQLException {
        try {
            if (preparedStatement == null) return null;
            ResultSet resultSet = preparedStatement.getResultSet();
            logger.debug("resultSet = {}", resultSet);
            if (resultSet == null || !resultSet.first()) return null;
            return (T) getEntity(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> Dao find(Connection connection, String fieldName, T fieldValue) throws DaoException {
        logger.debug("find > {}, {}", fieldName, fieldValue);
        try {
            String sql = H2JdbcDao.getDaoReadBy();
            sql = sql.replaceFirst("\\?", "*");
            sql = sql.replaceFirst("\\?", getEntityTableName());
            sql = sql.replaceFirst("\\?", fieldName.concat(" = ?"));
            logger.debug("sql = {}", sql);
            if (preparedStatement != null) preparedStatement.close();
            preparedStatement = connection.prepareStatement(sql);
            Method method = H2JdbcDao.getPreparedStatementMethod(fieldValue.getClass());
            if (method == null) throw new DaoException("Prepared statement method not found");
            method.invoke(preparedStatement, 1, fieldValue);
            preparedStatement.executeQuery();
            return this;
        } catch (SQLException | InvocationTargetException | IllegalAccessException e) {
            throw new DaoException(e.getMessage());
        }
    }

}
