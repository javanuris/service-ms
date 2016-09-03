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


    //    abstract List<ReflectiveJdbcDao.TableDefinition> getTableDefinitionList(Class<?> entityClass);
//
//    abstract List<StringBuilder> getSqlPartNames(List<ReflectiveJdbcDao.TableDefinition> tableDefinitionList, boolean withDefinition);
//
//    abstract StringBuilder getSqlPartNames(ReflectiveJdbcDao.TableDefinition tableDefinition, boolean withDefinition);
//
//    abstract StringBuilder getSqlPartWildcardedValues(ReflectiveJdbcDao.TableDefinition tableDefinition);
//
//    abstract ReflectiveJdbcDao.TableDefinition getTableDefinitionForNotNullObjectFields
//            (ReflectiveJdbcDao.TableDefinition tableDefinition, Object entityObject) throws SQLException, IllegalAccessException;
//
//    abstract <T> void setValuesToPreparedStatement
//            (PreparedStatement preparedStatement, ReflectiveJdbcDao.TableDefinition tableDefinition, Object entityObject)
//            throws SQLException, IllegalAccessException, InvocationTargetException;
//
//    abstract void setGeneratedKeysToEntityObject(PreparedStatement preparedStatement,
//                                                 ReflectiveJdbcDao.TableDefinition tableDefinition, Object entityObject) throws SQLException, IllegalAccessException;
//
//    abstract List<RelationEntity> getRelationEntityList(List<ReflectiveJdbcDao.TableDefinition> tableDefinitionList, Object entityObject);
//
//    @Override
//    public final String generateCreateTableExpression(Class<?> entityClass) {
//        List<ReflectiveJdbcDao.TableDefinition> tableDefinitionList = getTableDefinitionList(entityClass);
//        if (tableDefinitionList == null) return null;
//        List<StringBuilder> sqlPartNamesList = getSqlPartNames(tableDefinitionList, true);
//        StringBuilder resultExpression = new StringBuilder();
//        int replacePosition = 0;
//        for (int i = 0; i < tableDefinitionList.size(); i++) {
//            resultExpression.append(H2JdbcDao.PROPERTIES.getProperty("dao.operation.table.create"));
//            replacePosition = resultExpression.indexOf("<?>", replacePosition);
//            resultExpression.replace(replacePosition, replacePosition + 3,
//                    "\"".concat(tableDefinitionList.get(i).name).concat("\""));
//            replacePosition = resultExpression.indexOf("<?>", replacePosition);
//            resultExpression.replace(replacePosition, replacePosition + 3,
//                    sqlPartNamesList.get(i).toString());
//        }
//        return resultExpression.toString();
//    }
//
//    @Override
//    public boolean execute(Connection connection, String sqlExpression) throws SQLException {
//        try {
//            Statement statement = connection.createStatement();
//            return statement.execute(sqlExpression);
//        } catch (SQLException e) {
//            logger.error("Statement execution error ({})", sqlExpression, e);
//            throw e;
//        }
//    }
//
//    private StringBuilder generateInsertIntoTableWildcardedExpression
//            (ReflectiveJdbcDao.TableDefinition tableDefinition, Object entityObject)
//            throws SQLException, IllegalAccessException {
//        StringBuilder sqlPartNames = getSqlPartNames(tableDefinition, false);
//        StringBuilder sqlPartWildcardedValues = getSqlPartWildcardedValues(tableDefinition);
//        StringBuilder resultExpression = new StringBuilder
//                (H2JdbcDao.PROPERTIES.getProperty("dao.operation.record.insert"));
//        int replacePosition = resultExpression.indexOf("<?>", 0);
//        resultExpression.replace(replacePosition, replacePosition + 3,
//                "\"".concat(tableDefinition.name).concat("\""));
//        replacePosition = resultExpression.indexOf("<?>", replacePosition);
//        resultExpression.replace(replacePosition, replacePosition + 3,
//                sqlPartNames.toString());
//        replacePosition = resultExpression.indexOf("<?>", replacePosition);
//        resultExpression.replace(replacePosition, replacePosition + 3,
//                sqlPartWildcardedValues.toString());
//        return resultExpression;
//    }
//
//    @Override
//    public final int insert(Connection connection, Object entityObject)
//            throws SQLException, IllegalAccessException, InvocationTargetException {
//        List<ReflectiveJdbcDao.TableDefinition> tableDefinitionList = getTableDefinitionList(entityObject.getClass());
//        if (tableDefinitionList == null) throw new SQLException("Table definition not found");
//        ReflectiveJdbcDao.TableDefinition tableDefinition =
//                getTableDefinitionForNotNullObjectFields(tableDefinitionList.get(0), entityObject);
//        PreparedStatement preparedStatement = null;
//        try {
//            preparedStatement = getPreparedStatement(connection,
//                    generateInsertIntoTableWildcardedExpression(tableDefinition, entityObject).toString(),
//                    PreparedStatement.RETURN_GENERATED_KEYS);
//            setValuesToPreparedStatement(preparedStatement, tableDefinition, entityObject);
//            int result = preparedStatement.executeUpdate();
//            if (result == 0) throw new SQLException("Insert error");
//            setGeneratedKeysToEntityObject(preparedStatement, tableDefinitionList.get(0), entityObject);
//            if (tableDefinitionList.size() > 1) result = result + insertRelations(tableDefinitionList, entityObject);
//            return result;
//        } catch (SQLException | IllegalAccessException | InvocationTargetException e) {
//            logger.error("Prepared statement creating or execution error", e);
//            throw e;
//        } finally {
//            closePreparedStatement(preparedStatement);
//        }
//    }
//
//    private final int insertRelations(List<ReflectiveJdbcDao.TableDefinition> tableDefinitionList, Object entityObject) {
//        List<RelationEntity> relationEntityList = getRelationEntityList(tableDefinitionList, entityObject);
//        StringBuilder sqlExpression;
//        for (RelationEntity relationEntity : relationEntityList) {
//            // TODO: check for existence of relationEntity
//
//            // TODO: generate insert into table expression
//
//            // TODO: executeUpdate
//
//            // TODO: add result value to global result
//
//        }
//
//        return 0;
//    }
//
//    @Override
//    public final ResultSet query(Connection connection, String sqlExpression) throws SQLException {
//        try {
//            Statement statement = connection.createStatement();
//            return statement.executeQuery(sqlExpression);
//        } catch (SQLException e) {
//            logger.error("Statement querying error ({})", sqlExpression, e);
//            throw e;
//        }
//    }

}
