package com.epam.java.rt.lab.dao.h2;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.Parameter;
import com.epam.java.rt.lab.dao.query.*;
import com.epam.java.rt.lab.dao.query.Set;
import com.epam.java.rt.lab.util.StringArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

/**
 * service-ms
 */
public abstract class JdbcDao implements Dao {
    private static final Logger logger = LoggerFactory.getLogger(JdbcDao.class);
    private static Map<String, String> cachedQueryMap = new HashMap<>();
    private static Map<java.lang.reflect.Type, Method> preparedStatementMethodMap = new HashMap<>();
    private Connection connection = null;

    private enum CRUD {
        CREATE, READ, UPDATE, DELETE, COUNT
    }

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
                preparedStatementMethodMap.put(Timestamp.class, PreparedStatement.class.getMethod("setTimestamp", int.class, Timestamp.class));
                preparedStatementMethodMap.put(String.class, PreparedStatement.class.getMethod("setString", int.class, String.class));
                preparedStatementMethodMap.put(Blob.class, PreparedStatement.class.getMethod("setBlob", int.class, Blob.class));
                preparedStatementMethodMap.put(Clob.class, PreparedStatement.class.getMethod("setClob", int.class, Clob.class));
                preparedStatementMethodMap.put(FileInputStream.class, PreparedStatement.class.getMethod("setBinaryStream", int.class, InputStream.class));
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
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
                query = new Create(getEntityTableName());
                fitQueryToEntity(query, entity, setNames, fieldNames);
//                query.setSql(queryString == null ? query.getSql() : queryString);
                break;
            case READ:
                query = new Select(getEntityTableName(), columnNames, offset, count, order);
                fitQueryToEntity(query, entity, setNames, fieldNames);
//                query.setSql(queryString == null ? query.getSql() : queryString);
                break;
            case UPDATE:
                query = new Update(getEntityTableName());
                fitQueryToEntity(query, entity, setNames, fieldNames);
//                query.setSql(queryString == null ? query.getSql() : queryString);
                break;
            case DELETE:
                break;
            case COUNT:
                query = new Select(getEntityTableName(), columnNames, null, null, null);
//                query.setSql(queryString == null ? query.createCount() : queryString);
                break;
        }
        if (queryString == null && query != null) {
            JdbcDao.cachedQueryMap.put(queryKey, query.getSql());
//            queryString = query.getSql();
        }
        if (crud.equals(CRUD.READ) && offset != null && count != null) {
//            queryString.concat(new StringBuilder().append(" LIMIT ").append(offset).append(", ").append(count).toString());
//            query.setSql(queryString);
        }
//        logger.debug(queryString);
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
        throw new NoSuchFieldException("exception.dao.query.reflect.getTransfer-field: " + fieldName);
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
        // it is too expensive to getSql extra number of classes which do almost similar job
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

    @Override
    public <T> T getFirst(T entity, String fieldNames, String order) throws DaoException {
        return getFirst(entity, fieldNames, null, order);
    }

    @Override
    public <T> T getFirst(T entity, String fieldNames, String columnNames, String order) throws DaoException {
        Query query = getCachedQuery(CRUD.READ, entity, fieldNames, columnNames, null, order, null, null);
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query.create());
             ResultSet resultSet = setPreparedStatementValues(preparedStatement, query).executeQuery();) {
            if (resultSet == null || !resultSet.first()) return null;
            return getEntityFromResultSet(entity, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.jdbc.getTransfer-first.getTransfer-result-set", e.getCause());
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
        Query query = getCachedQuery(CRUD.READ, entity, fieldNames, columnNames, null, order, offset, count);
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query.create());
             ResultSet resultSet = setPreparedStatementValues(preparedStatement, query).executeQuery();) {
            if (resultSet == null) return null;
            List<T> entityList = new ArrayList<>();
            while(resultSet.next()) entityList.add(getEntityFromResultSet(entity, resultSet));
            return entityList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.getTransfer-all");
        }
    }

    @Override
    public <T> Long count(T entity, String columnNames) throws DaoException {
        Query query = getCachedQuery(CRUD.READ, entity, null, columnNames, null, null, null, null);
        try (PreparedStatement countStatement = getConnection().prepareStatement(query.createCount());
             ResultSet resultSet = countStatement.executeQuery();) {
            if (resultSet == null || !countStatement.getResultSet().next()) return null;
            return countStatement.getResultSet().getLong("count");
        } catch (SQLException e) {
            throw new DaoException("exception.dao.jdbc.count", e.getCause());
        }
    }

    abstract String getEntityTableName();

    abstract <T> Column getEntityColumn(T entity, Field field) throws DaoException;

    abstract <T> T getEntityFromResultSet(T entity, ResultSet resultSet) throws DaoException;

    private <T> int updateQuery(T entity, String fieldNames, String setNames) throws DaoException {
        Query query = getCachedQuery(CRUD.UPDATE, entity, fieldNames, null, setNames, null, null, null);
        logger.debug("UPDATE_QUERY {}", query.create());
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query.create());) {
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
    public <T> int setRelEntity(T entity, String relEntityName, Object relEntity) throws DaoException {
        return 0;
    }

    @Override
    public <T> int removeRelEntity(T entity, String relEntityName) throws DaoException {
        return 0;
    }

    @Override
    public <T> int create(T entity) throws DaoException {
        Query query = getCachedQuery(CRUD.CREATE, entity, null, null, getEntitySetNames(entity), null, null, null);
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query.create());) {
            return setPreparedStatementValues(preparedStatement, query).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.update-query.sql", e.getCause());
        }
    }

    abstract <T> String getEntitySetNames(T entity);


    // newly dao implementation

    private <T> void setStatementValue(PreparedStatement preparedStatement, int setIndex, T setValue)
            throws SQLException, InvocationTargetException, IllegalAccessException {
        if (setValue == null) {
            preparedStatement.setNull(setIndex, Types.NULL);
        } else {
            Method method = preparedStatementMethodMap.get(setValue.getClass());
            if (method == null) throw new SQLException();
            method.invoke(preparedStatement, setIndex, setValue);
        }
    }

    private PreparedStatement setStatementValues(PreparedStatement preparedStatement, Parameter parameter) throws DaoException {
        logger.debug("STATEMENT: {}", preparedStatement);
        try {
            int setIndex = 1;
            Object fieldArgument = parameter.get(Parameter.Type.SET_FIELD_ARRAY);
            if (fieldArgument != null) {
                if (!(fieldArgument instanceof ArrayList<?>))
                    throw new DaoException("exception.dao.jdbc.set-statement-values.field-list");
                for (Parameter.Field field : (List<Parameter.Field>) fieldArgument) {
                    setStatementValue(preparedStatement, setIndex, field.getValue());
                    setIndex++;
                }
            }
            Object whereArgument = parameter.get(Parameter.Type._WHERE_COLUMN_LIST);
            if (whereArgument != null) {
                if (!(whereArgument instanceof ArrayList<?>))
                    throw new DaoException("exception.dao.jdbc.set-statement-values.where-list");
                for (Parameter.Field field : (List<Parameter.Field>) whereArgument) {
                    if (field.getCompareFieldName() == null) {
                        setStatementValue(preparedStatement, setIndex, field.getValue());
                        setIndex++;
                    }
                }
            }
            return preparedStatement;
        } catch (IllegalAccessException | SQLException | InvocationTargetException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.jdbc.set-statement-values.set-statement-value", e.getCause());
        }
    }

    @Override
    public Long count(Parameter parameter) throws DaoException {
        if (parameter == null)
            throw new DaoException("exception.dao.jdbc.count.parameter");
        extendArgument(parameter);
        parameter.put(Parameter.Type._QUERY_TYPE, QueryBuilder.Type.FUNC);
        parameter.put(Parameter.Type._FUNC_NAME, "COUNT");
        try (PreparedStatement statement = getConnection().prepareStatement(QueryBuilder.getSql(parameter));
             ResultSet resultSet = setStatementValues(statement, parameter).executeQuery()) {
            if (resultSet == null || !resultSet.next()) return null;
            return resultSet.getLong("count");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.jdbc.count.sql", e.getCause());
        }
    }

    @Override
    public <T> List<T> getAll(Parameter parameter) throws DaoException {
        if (parameter == null)
            throw new DaoException("exception.dao.jdbc.create-all.parameter");
        extendArgument(parameter);
        parameter.put(Parameter.Type._QUERY_TYPE, QueryBuilder.Type.READ);
        logger.debug("parameter: {}", parameter);
        try (PreparedStatement statement = getConnection().prepareStatement(QueryBuilder.getSql(parameter));
             ResultSet resultSet = setStatementValues(statement, parameter).executeQuery()) {
            if (resultSet == null) return null;
            return getEntityList(resultSet, parameter);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.jdbc.create-all.sql");
        }
    }

    @Override
    public <T> T getFirst(Parameter parameter) throws DaoException {
        if (parameter == null)
            throw new DaoException("exception.dao.jdbc.create-first.parameter");
        extendArgument(parameter);
        parameter.put(Parameter.Type._QUERY_TYPE, QueryBuilder.Type.READ);
        logger.debug("parameter: {}", parameter);
        String sql = QueryBuilder.getSql(parameter);
        try (PreparedStatement statement = getConnection().prepareStatement(sql);
             ResultSet resultSet = setStatementValues(statement, parameter).executeQuery()) {
            if (resultSet == null || !resultSet.first()) return null;
            return getEntity(resultSet, parameter);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.jdbc.create-first");
        }
    }

    abstract <T> List<T> getEntityList(ResultSet resultSet, Parameter parameter) throws SQLException, DaoException;

    abstract <T> T getEntity(ResultSet resultSet, Parameter parameter) throws SQLException, DaoException;

    abstract String getDefaultFrom();

    abstract Parameter.Field getJoinWhereItem(String joinTable) throws DaoException;

    abstract List<String> getAllSelectColumnList();

    private List<String> getStringList(Object delimitedWithCommasStringOrArray) {
        if (delimitedWithCommasStringOrArray instanceof String) {
            return new ArrayList<>(Arrays.asList(((String) delimitedWithCommasStringOrArray)
                    .replaceAll(" ", "").split(",")));
        } else if (delimitedWithCommasStringOrArray instanceof String[]) {
            return new ArrayList<>(Arrays.asList((String[]) delimitedWithCommasStringOrArray));
        }
        return null;
    }

    private List<String> getSelectColumnList(Object resultFieldParameter) throws DaoException {
        List<String> selectColumnList = getStringList(resultFieldParameter);
        if (selectColumnList != null) return selectColumnList;
        throw new DaoException("exception.dao.jdbc.create-select-column-list");
    }

    private void fillJoinTableListAndJoinWhereList(List<String> joinTableList, List<Parameter.Field> joinWhereList,
                                                   List<String> columnList) throws DaoException {
        for (int i = 0; i < columnList.size(); i++) {
            String[] split = Parameter.splitAndConvertFieldNames(columnList.get(i));
            if (split[0].length() == 0) {
                columnList.set(i, getDefaultFrom().concat(split[1]));
            } else {
                columnList.set(i, split[1]);
            }
            if (!joinTableList.contains(split[0])) {
                joinTableList.add(split[0]);
                if (split[0].length() > 0 ) joinWhereList.add(getJoinWhereItem(split[0]));
            }
        }
    }

    private List<Parameter.Field> getWhereColumnList(Object whereFieldArrayParameter) throws DaoException {
        if (whereFieldArrayParameter == null) {
            return new ArrayList<>();
        } else if (whereFieldArrayParameter instanceof Parameter.Field[]) {
            return new ArrayList<>(Arrays.asList((Parameter.Field[]) whereFieldArrayParameter));
        } else {
            throw new DaoException("exception.dao.jdbc.create-where-column-list");
        }
    }

    private void fillJoinTableList(List<String> joinTableList,
                                   List<Parameter.Field> whereColumnList) throws DaoException {
        for (Parameter.Field where : whereColumnList) {
            String tableName = where.getName().split("\\.")[0];
            if (!joinTableList.contains(tableName)) joinTableList.add(tableName);
        }
    }

    private void fillWhereColumnList(List<Parameter.Field> whereColumnList, List<Parameter.Field> joinWhereList) {
        for (Parameter.Field joinWhere : joinWhereList) {
            boolean exists = false;
            for (Parameter.Field where : whereColumnList) {
                if (joinWhere.equals(where)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) whereColumnList.add(joinWhere);
        }
    }

    private List<String> getOrderColumnList(Object orderFieldNameArrayParameter) {
        if (orderFieldNameArrayParameter == null) return null;
        List<String> orderColumnList = getStringList(orderFieldNameArrayParameter);
        if (orderColumnList == null) return null;
        for (int i = 0; i < orderColumnList.size(); i++) {
            String[] split = Parameter.splitAndConvertFieldNames(orderColumnList.get(i));
            if (split[0].length() == 0) {
                orderColumnList.set(i, getDefaultFrom().concat(split[1]));
            } else {
                orderColumnList.set(i, split[1]);
            }
        }
        return orderColumnList;
    }

    private void extendArgument(Parameter parameter) throws DaoException {
        List<String> selectColumnList;
        Object resultFieldArrayParameter = parameter.get(Parameter.Type.RESULT_FIELD_ARRAY);
        if (resultFieldArrayParameter == null) {
            parameter.put(Parameter.Type._FROM_TABLE, getDefaultFrom());
            selectColumnList = getAllSelectColumnList();
        } else {
            // prepare join section and where section according to join
            selectColumnList = getSelectColumnList(resultFieldArrayParameter);
            List<String> joinTableList = new ArrayList<>();
            List<Parameter.Field> joinWhereList = new ArrayList<>();
            fillJoinTableListAndJoinWhereList(joinTableList, joinWhereList, selectColumnList);
            // prepare where section
            Object whereFieldArrayParameter = parameter.get(Parameter.Type.WHERE_FIELD_ARRAY);
            List<Parameter.Field> whereColumnList = getWhereColumnList(whereFieldArrayParameter);
            parameter.put(Parameter.Type._WHERE_COLUMN_LIST, whereColumnList);
            // another prepare join section
            fillJoinTableList(joinTableList, whereColumnList);
            if (joinTableList.contains("")) { // removing empty joinTable meaning defaultFrom
                parameter.put(Parameter.Type._FROM_TABLE, getDefaultFrom());
                joinTableList.remove("");
            } else if (joinTableList.size() > 0) { // removing index #0 joinTable meaning defaultFrom
                parameter.put(Parameter.Type._FROM_TABLE, joinTableList.get(0));
                joinTableList.remove(0);
            }
            // complete join section and where section
            if (joinTableList.size() > 0) {
                parameter.put(Parameter.Type._JOIN_TABLES,
                        StringArray.combine(joinTableList, StringArray.DELIMITER_COMMA_AND_SPACE));
                fillWhereColumnList(whereColumnList, joinWhereList);
            }
        }
        // prepare order section
        Object orderFieldNameArrayParameter = parameter.get(Parameter.Type.ORDER_FIELD_NAME_ARRAY);
        parameter.put(Parameter.Type._ORDER_COLUMN_LIST, getOrderColumnList(orderFieldNameArrayParameter));
        // complete select section
        parameter.put(Parameter.Type._SELECT_COLUMN_LIST, selectColumnList);
    }
}
