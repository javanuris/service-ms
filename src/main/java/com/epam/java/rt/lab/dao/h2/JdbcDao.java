package com.epam.java.rt.lab.dao.h2;

import com.epam.java.rt.lab.dao.Argument;
import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
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
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

/**
 * service-ms
 */
public abstract class JdbcDao implements Dao {
    private static final Logger logger = LoggerFactory.getLogger(JdbcDao.class);
    private static Map<String, String> cachedQueryMap = new HashMap<>();
    private static Map<Type, Method> preparedStatementMethodMap = new HashMap<>();
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
//                query.setSql(queryString == null ? query.create() : queryString);
                break;
            case READ:
                query = new Select(getEntityTableName(), columnNames, offset, count, order);
                fitQueryToEntity(query, entity, setNames, fieldNames);
//                query.setSql(queryString == null ? query.create() : queryString);
                break;
            case UPDATE:
                query = new Update(getEntityTableName());
                fitQueryToEntity(query, entity, setNames, fieldNames);
//                query.setSql(queryString == null ? query.create() : queryString);
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

    private PreparedStatement setStatementValues(PreparedStatement preparedStatement, Argument argument) throws DaoException {
        logger.debug("STMT: {}", preparedStatement);
        try {
            int setIndex = 1;
            Object fieldArgument = argument.get(ArgumentType.SET_FIELD_LIST);
            if (fieldArgument != null) {
                if (!(fieldArgument instanceof ArrayList<?>))
                    throw new DaoException("exception.dao.jdbc.set-statement-values.field-list");
                for (Argument.Field field : (List<Argument.Field>) fieldArgument) {
                    setStatementValue(preparedStatement, setIndex, field.getValue());
                    setIndex++;
                }
            }
            Object whereArgument = argument.get(ArgumentType.WHERE_LIST);
            if (whereArgument != null) {
                if (!(whereArgument instanceof ArrayList<?>))
                    throw new DaoException("exception.dao.jdbc.set-statement-values.where-list");
                for (Argument.Field field : (List<Argument.Field>) whereArgument) {
                    if (field.getCompareFieldName() == null) {
                        setStatementValue(preparedStatement, setIndex, field.getValue());
                        setIndex++;
                    }
                }
            }
            logger.debug("VALUED: {}", preparedStatement);
            return preparedStatement;
        } catch (IllegalAccessException | SQLException | InvocationTargetException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.jdbc.set-statement-values.set-statement-value", e.getCause());
        }
    }

    @Override
    public Long count(Argument argument) throws DaoException {
        if (argument == null)
            throw new DaoException("exception.dao.jdbc.count.argument");
        extendArgument(argument);
        argument.put(ArgumentType.QUERY_TYPE, QueryType.FUNC);
        argument.put(ArgumentType.FUNC_NAME, "COUNT");
        String sql = _query.create(argument);
        try (PreparedStatement statement = getConnection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet == null || !resultSet.next()) return null;
            return resultSet.getLong("count");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.jdbc.count.sql", e.getCause());
        }
    }

    @Override
    public <T> List<T> getAll(Argument argument) throws DaoException {
        if (argument == null)
            throw new DaoException("exception.dao.jdbc.get-all.argument");
        extendArgument(argument);
        argument.put(ArgumentType.QUERY_TYPE, QueryType.READ);
        logger.debug("argument: {}", argument);
        String sql = _query.create(argument);
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
             ResultSet resultSet = setStatementValues(preparedStatement, argument).executeQuery()) {
            if (resultSet == null) return null;
            return getEntityList(resultSet, argument);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.getTransfer-all");
        }
    }

    @Override
    public <T> T getFirst(Argument argument) throws DaoException {
        if (argument == null)
            throw new DaoException("exception.dao.jdbc.get-first.argument");
        extendArgument(argument);
        argument.put(ArgumentType.QUERY_TYPE, QueryType.READ);
        logger.debug("argument: {}", argument);
        String sql = _query.create(argument);
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
             ResultSet resultSet = setStatementValues(preparedStatement, argument).executeQuery()) {
            if (resultSet == null || !resultSet.first()) return null;
            return getEntity(resultSet, argument);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.getTransfer-all");
        }
    }

    abstract <T> List<T> getEntityList(ResultSet resultSet, Argument argument) throws SQLException, DaoException;

    abstract <T> T getEntity(ResultSet resultSet, Argument argument) throws SQLException, DaoException;

    abstract String getDefaultFrom();

    abstract Argument.Field getJoinWhere(String joinTable) throws DaoException;

    abstract List<String> getAllSelectColumnList();

    private List<String> getSelectColumnList(Object resultFieldArgument) throws DaoException {
        if (resultFieldArgument instanceof String) {
            return new ArrayList<>(Arrays.asList(((String) resultFieldArgument).replaceAll(" ", "").split(",")));
        } else if (resultFieldArgument instanceof ArrayList<?>) {
            return new ArrayList<>((List<String>) resultFieldArgument);
        } else {
            throw new DaoException("exception.dao.jdbc.get-select-column-list");
        }
    }

    private void fillJoinTableWhereList(List<String> joinTableList, List<Argument.Field> joinWhereList,
                                        List<String> columnList) throws DaoException {
        for (int i = 0; i < columnList.size(); i++) {
            String[] split = Argument.splitSelectColumn(columnList.get(i));
            if (split[0].length() == 0) {
                columnList.set(i, getDefaultFrom().concat(split[1]));
            } else {
                columnList.set(i, split[1]);
            }
            if (!joinTableList.contains(split[0])) {
                joinTableList.add(split[0]);
                if (split[0].length() > 0 ) joinWhereList.add(getJoinWhere(split[0]));
            }
        }
    }

    private List<Argument.Field> getWhereList(Object whereListArgument) throws DaoException {
        if (whereListArgument == null) {
            return new ArrayList<>();
        } else if (whereListArgument != null && whereListArgument instanceof ArrayList<?>) {
            return (List<Argument.Field>) whereListArgument;
        } else {
            throw new DaoException("exception.dao.jdbc.get-where-list");
        }
    }

    private void fillJoinTableList(List<String> joinTableList,
                                   List<Argument.Field> whereList) throws DaoException {
        for (Argument.Field where : whereList) {
            String tableName = where.getName().split("\\.")[0];
            if (!joinTableList.contains(tableName)) joinTableList.add(tableName);
        }
    }

    private void fillWhereList(List<Argument.Field> whereList, List<Argument.Field> joinWhereList) {
        for (Argument.Field joinWhere : joinWhereList) {
            boolean exists = false;
            for (Argument.Field where : whereList) {
                if (joinWhere.equals(where)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) whereList.add(joinWhere);
        }
    }

    private void extendArgument(Argument argument) throws DaoException {
        Object resultFieldArgument = argument.get(ArgumentType.RESULT_FIELDS);
        List<String> selectColumnList = null;
        if (resultFieldArgument == null) {
            argument.put(ArgumentType.FROM_TABLE, getDefaultFrom());
            argument.put(ArgumentType.SELECT_COLUMN_LIST, getAllSelectColumnList());
        } else {
            List<String> joinTableList = new ArrayList<>();
            List<Argument.Field> joinWhereList = new ArrayList<>();
            selectColumnList = getSelectColumnList(resultFieldArgument);
            fillJoinTableWhereList(joinTableList, joinWhereList, selectColumnList);
            Object whereListArgument = argument.get(ArgumentType.WHERE_LIST);
            List<Argument.Field> whereList = getWhereList(whereListArgument);
            argument.put(ArgumentType.WHERE_LIST, whereList);
            fillJoinTableList(joinTableList, whereList);
            if (joinTableList.contains("")) {
                argument.put(ArgumentType.FROM_TABLE, getDefaultFrom());
                joinTableList.remove("");
            } else {
                if (joinTableList.size() > 0) {
                    argument.put(ArgumentType.FROM_TABLE, joinTableList.get(0));
                    joinTableList.remove(0);
                }
            }
            if (joinTableList.size() > 0) {
                argument.put(ArgumentType.JOIN_TABLES,
                        StringArray.combineList(joinTableList, StringArray.DELIMITER_COMMA_AND_SPACE));
                fillWhereList(whereList, joinWhereList);
            }
        }
        argument.put(ArgumentType.SELECT_COLUMN_LIST, selectColumnList);
    }

}
