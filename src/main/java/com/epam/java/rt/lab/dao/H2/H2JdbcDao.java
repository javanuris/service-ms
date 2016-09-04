package com.epam.java.rt.lab.dao.H2;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.H2.sql.Option;
import com.epam.java.rt.lab.dao.H2.sql.Query;
import com.epam.java.rt.lab.dao.H2.sql.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

/**
 * service-ms
 */
public abstract class H2JdbcDao implements Dao {
    private static final Logger logger = LoggerFactory.getLogger(H2JdbcDao.class);
    private static Map<Type, Method> preparedStatementMethodMap = new HashMap<>();
    private static String daoSelect;
    private static String daoWhere;
    private static String daoCreate;
    private static String daoReadBy;
    private static String daoReadJoin;
    private static String daoUpdate;
    private static String daoDelete;
    private String sql = null;
    private Query sqlQuery = null;
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;

    public void resetProperties() throws DaoException {
        if (daoSelect == null || daoWhere == null ||
                daoCreate == null || daoReadBy == null || daoReadJoin == null || daoUpdate == null || daoDelete == null)
            resetProperties("h2sql.properties");
    }

    public void resetProperties(String fileName) throws DaoException {
        try {
            Properties properties = new Properties();
            properties.load(H2JdbcDao.class.getClassLoader().getResourceAsStream(fileName));
            Query.resetProperties(properties);

            String daoSelect = properties.getProperty("dao.select");
            String daoWhere = properties.getProperty("dao.where");
            String daoCreate = properties.getProperty("dao.create");
            String daoReadBy = properties.getProperty("dao.read.by");
            String daoReadJoin = properties.getProperty("dao.read.join");
            String daoUpdate = properties.getProperty("dao.update");
            String daoDelete = properties.getProperty("dao.delete");
            if (daoSelect == null || daoWhere == null ||
                    daoCreate == null || daoReadBy == null || daoReadJoin == null || daoUpdate == null || daoDelete == null)
                throw new DaoException("H2 database CRUD properties error");
            H2JdbcDao.daoSelect = daoSelect;
            H2JdbcDao.daoWhere = daoWhere;
            H2JdbcDao.daoCreate = daoCreate;
            H2JdbcDao.daoReadBy = daoReadBy;
            H2JdbcDao.daoReadJoin = daoReadJoin;
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

    static Method getPreparedStatementMethod(Type valueType) {
        return preparedStatementMethodMap.get(valueType);
    }

    abstract String getEntityTableName();

    abstract Object getEntity(ResultSet resultSet) throws SQLException;

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() throws DaoException {
        try {
            if (preparedStatement != null) preparedStatement.close();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public ResultSet getResultSet() throws DaoException {
        try {
            if (sqlQuery != null) executeQuery();
            if (preparedStatement == null) throw new DaoException("Result set not defined");
            return preparedStatement.getResultSet();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Dao query(String... fieldNameArray) throws DaoException {
        logger.debug("query > {}", fieldNameArray.toString());
        sqlQuery = new Query();
        sqlQuery.setTableName(getEntityTableName());
        for (String column : fieldNameArray) sqlQuery.addColumn(column);
        return this;
    }

    @Override
    public <T> Dao filter(String fieldName, T fieldValue) throws DaoException {
        logger.debug("filter > {}, {}", fieldName, fieldValue);
        if (fieldValue == null) {
            sqlQuery.addWhere(new Option(fieldName, null));
        } else {
            sqlQuery.addWhere(new Option(fieldName), new Value(fieldValue));
        }
        return this;
    }

    @Override
    public Dao join(String tableName) throws DaoException {
        sqlQuery.addJoin(tableName);
        return this;
    }

    @Override
    public Dao join(String tableName, String alias) throws DaoException {
        sqlQuery.addJoin(tableName, alias);
        return this;
    }

    @Override
    public Dao on(String fieldName, String compareFieldName) throws DaoException {
        sqlQuery.addOn(new Option(fieldName, compareFieldName));
        return this;
    }

    private void executeQuery() throws DaoException {
        logger.debug("executeQuery > {}", sqlQuery.create());
        try {
            if (sqlQuery != null) {
                if (preparedStatement != null) preparedStatement.close();
                preparedStatement = connection.prepareStatement(sqlQuery.create());
                int index = 1;
                Method method;
                for (Value value : sqlQuery.getValueList()) {
                    method = H2JdbcDao.getPreparedStatementMethod(value.get().getClass());
                    if (method == null) throw new DaoException("Prepared statement method not found");
                    method.invoke(preparedStatement, index, value.get());
                    index++;
                }
                preparedStatement.executeQuery();
                sqlQuery = null;
            }
        } catch (IllegalAccessException | SQLException | InvocationTargetException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Dao execute() throws DaoException {
        logger.debug("execute >");
        executeQuery();
        return this;
    }

    @Override
    public <T> T first() throws DaoException {
        try {
            ResultSet resultSet = getResultSet();
            logger.debug("resultSet = {}", resultSet);
            if (resultSet == null || !resultSet.first()) return null;
            return (T) getEntity(resultSet);
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public <T> List<T> all() throws DaoException {
        try {
            ResultSet resultSet = getResultSet();
            logger.debug("resultSet = {}", resultSet);
            if (resultSet == null) return null;
            List<T> entityList = new ArrayList<T>();
            T entity;
            while(resultSet.next()) {
                entity = (T) getEntity(resultSet);
                entityList.add(entity);
            }
            return entityList;
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

}
