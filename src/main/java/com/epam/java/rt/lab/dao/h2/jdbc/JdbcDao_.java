package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.Dao_;
import com.epam.java.rt.lab.entity.EntityProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

/**
 * service-ms
 */
public abstract class JdbcDao_ implements Dao_ {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcDao_.class);

    public enum RolePermission implements EntityProperty {
        ID,
        ROLE_ID,
        PERMISSION_ID;


        @Override
        public Class getEntityClass() {
            return null;
        }
    }

    private Connection connection = null;

    public JdbcDao_(Connection connection) throws DaoException {
        this.connection = connection;
    }

    Connection getConnection() {
        return connection;
    }

//    public <T> Query getCachedQuery(CRUD crud, T entity,
//                                     String fieldNames, String columnNames, String setNames,
//                                     String order, Long offset, Long count) throws DaoException {
//        String queryKey = ""; //new StringBuilder().append(crud).append(entity.getClass().getSimpleName())
////                .append(fieldNames).append(columnNames).append(setNames)
////                .append(order).append((offset != null && count != null) ? "LIMIT" : "").toString();
//        String queryString = JdbcDao_.cachedQueryMap.get(queryKey);
//        queryString = null; // TODO: fix sql caching
////        logger.debug("queryKey = {}\nqueryString = {}", queryKey, queryString);
//        Query query = null;
//        switch (crud) {
//            case CREATE:
//                query = new Create(getEntityTableName());
//                fitQueryToEntity(query, entity, setNames, fieldNames);
////                query.setSql(queryString == null ? query.getSql() : queryString);
//                break;
//            case READ:
//                query = new Select(getEntityTableName(), columnNames, offset, count, order);
//                fitQueryToEntity(query, entity, setNames, fieldNames);
////                query.setSql(queryString == null ? query.getSql() : queryString);
//                break;
//            case UPDATE:
//                query = new Update(getEntityTableName());
//                fitQueryToEntity(query, entity, setNames, fieldNames);
////                query.setSql(queryString == null ? query.getSql() : queryString);
//                break;
//            case DELETE:
//                break;
//            case COUNT:
//                query = new Select(getEntityTableName(), columnNames, null, null, null);
////                query.setSql(queryString == null ? query.createCount() : queryString);
//                break;
//        }
//        if (queryString == null && query != null) {
//            JdbcDao_.cachedQueryMap.put(queryKey, query.getSql());
////            queryString = query.getSql();
//        }
//        if (crud.equals(CRUD.READ) && offset != null && count != null) {
////            queryString.concat(new StringBuilder().append(" LIMIT ").append(offset).append(", ").append(count).toString());
////            query.setSql(queryString);
//        }
////        logger.debug(queryString);
//        return query;
//    }
//
//    private <T> void fitQueryToEntity(Query query, T entity, String setNames, String fieldNames) throws DaoException {
//        try {
//            if (setNames != null) {
//                List<Set> setList = new ArrayList<>();
//                setNames = setNames.replaceAll(" ", "");
//                for (String setName : setNames.split(",")) {
//                    Property field = getField(entity.getClass(), setName);
//                    setList.add(getEntitySet(entity, field));
//                }
//                query.setSetList(setList);
//            }
//            if (fieldNames != null) {
//                List<Column> columnList = new ArrayList<>();
//                fieldNames = fieldNames.replaceAll(" ", "");
//                for (String fieldName : fieldNames.split(",")) {
//                    Property field = getField(entity.getClass(), fieldName);
//                    columnList.add(getEntityColumn(entity, field));
//                }
//                query.setColumnList(columnList);
//            }
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//            throw new DaoException("exception.dao.fit-query-to-entity", e.getCause());
//        }
//    }
//
//    private Property getField(Class entityClass, String fieldName) throws NoSuchFieldException {
//        Property field = null;
//        while (entityClass != null) {
//            try {
//                field = entityClass.getDeclaredField(fieldName);
//                return field;
//            } catch (NoSuchFieldException e) {
//                entityClass = entityClass.getSuperclass();
//            }
//        }
//        throw new NoSuchFieldException("exception.dao.query.reflect.getTransfer-field: " + fieldName);
//    }
//
//    <T> T fieldValue(Property field, Object entity) throws IllegalAccessException {
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
//    PreparedStatement setPreparedStatementValues(PreparedStatement preparedStatement,
//                                                 List<Set> setList, List<Column> columnList) throws DaoException {
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
//            return preparedStatement;
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
//            throw new DaoException("exception.dao.jdbc.set-prepared-statement-values.invoke");
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new DaoException("exception.dao.jdbc.set-prepared-statement-values.set-null-value");
//        }
//    }
//
//    PreparedStatement setPreparedStatementValues(PreparedStatement preparedStatement,
//                                                 Query query) throws DaoException {
//        return setPreparedStatementValues(preparedStatement, query.getSetList(), query.getColumnList());
//    }
//
//    PreparedStatement setPreparedStatementValues(PreparedStatement preparedStatement,
//                                                 List<?> setOrColumnList) throws DaoException {
//        // it could be List<T> instead List<?>, and then it should be <T extend someParentClass>, but in this case
//        // it is too expensive to getSql extra number of classes which do almost similar job
//        if (setOrColumnList == null) return setPreparedStatementValues(preparedStatement, null, null);
//        switch (setOrColumnList.get(0).getClass().getSimpleName()) {
//            case "Set":
//                return setPreparedStatementValues(preparedStatement, (List<Set>) setOrColumnList, null);
//            case "Column":
//                return setPreparedStatementValues(preparedStatement, null, (List<Column>) setOrColumnList);
//        }
//        throw new DaoException("exception.dao.jdbc.set-prepared-statement-values.type-error");
//    }
//
//    ResultSet getGeneratedKeysAfterUpdate(PreparedStatement preparedStatement) throws SQLException {
//        preparedStatement.executeUpdate();
//        return preparedStatement.getGeneratedKeys();
//    }
//
//    @Override
//    public <T> T getFirst(T entity, String fieldNames, String order) throws DaoException {
//        return getFirst(entity, fieldNames, null, order);
//    }
//
//    @Override
//    public <T> T getFirst(T entity, String fieldNames, String columnNames, String order) throws DaoException {
//        Query query = getCachedQuery(CRUD.READ, entity, fieldNames, columnNames, null, order, null, null);
//        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query.create());
//             ResultSet resultSet = setPreparedStatementValues(preparedStatement, query).executeQuery();) {
//            if (resultSet == null || !resultSet.first()) return null;
//            return getEntityFromResultSet(entity, resultSet);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new DaoException("exception.dao.jdbc.getTransfer-first.getTransfer-result-set", e.getCause());
//        }
//    }
//
//    @Override
//    public <T> List<T> getAll(String order) throws DaoException {
//        return getAll(null, null, null, order, null, null);
//    }
//
//    @Override
//    public <T> List<T> getAll(T entity, String fieldNames, String order) throws DaoException {
//        return getAll(entity, fieldNames, null, order, null, null);
//    }
//
//    @Override
//    public <T> List<T> getAll(T entity, String fieldNames, String columnNames, String order) throws DaoException {
//        return getAll(entity, fieldNames, columnNames, order, null, null);
//    }
//
//    @Override
//    public <T> List<T> getAll(T entity, String fieldNames, String columnNames, String order, Long offset, Long count) throws DaoException {
//        Query query = getCachedQuery(CRUD.READ, entity, fieldNames, columnNames, null, order, offset, count);
//        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query.create());
//             ResultSet resultSet = setPreparedStatementValues(preparedStatement, query).executeQuery();) {
//            if (resultSet == null) return null;
//            List<T> entityList = new ArrayList<>();
//            while(resultSet.next()) entityList.add(getEntityFromResultSet(entity, resultSet));
//            return entityList;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new DaoException("exception.dao.getTransfer-all");
//        }
//    }
//
//    @Override
//    public <T> Long count(T entity, String columnNames) throws DaoException {
//        Query query = getCachedQuery(CRUD.READ, entity, null, columnNames, null, null, null, null);
//        try (PreparedStatement countStatement = getConnection().prepareStatement(query.createCount());
//             ResultSet resultSet = countStatement.executeQuery();) {
//            if (resultSet == null || !countStatement.getResultSet().next()) return null;
//            return countStatement.getResultSet().getLong("count");
//        } catch (SQLException e) {
//            throw new DaoException("exception.dao.jdbc.count", e.getCause());
//        }
//    }
//
//    abstract String getEntityTableName();
//
//    abstract <T> Column getEntityColumn(T entity, Property field) throws DaoException;
//
//    abstract <T> T getEntityFromResultSet(T entity, ResultSet resultSet) throws DaoException;
//
//    private <T> int updateQuery(T entity, String fieldNames, String setNames) throws DaoException {
//        Query query = getCachedQuery(CRUD.UPDATE, entity, fieldNames, null, setNames, null, null, null);
//        logger.debug("UPDATE_QUERY {}", query.create());
//        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query.create());) {
//            return setPreparedStatementValues(preparedStatement, query).executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new DaoException("exception.dao.update-query.sql", e.getCause());
//        }
//    }
//
//    @Override
//    public <T> int update(T entity, String fieldNames, String setNames) throws DaoException {
//        return updateQuery(entity, fieldNames, setNames) + relUpdate(entity, setNames);
//    }
//
//    abstract <T> Set getEntitySet(T entity, Property field) throws DaoException;
//
//    <T> int relUpdate(T entity, String setNames) throws DaoException {
//        return 0;
//    }
//
//    @Override
//    public <T> Object getRelEntity(T entity, String relEntityName) throws DaoException {
//        return null;
//    }
//
//    @Override
//    public <T> int setRelEntity(T entity, String relEntityName, Object relEntity) throws DaoException {
//        return 0;
//    }
//
//    @Override
//    public <T> int removeRelEntity(T entity, String relEntityName) throws DaoException {
//        return 0;
//    }
//
//    @Override
//    public <T> int create(T entity) throws DaoException {
//        Query query = getCachedQuery(CRUD.CREATE, entity, null, null, getEntitySetNames(entity), null, null, null);
//        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query.create());) {
//            return setPreparedStatementValues(preparedStatement, query).executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new DaoException("exception.dao.update-query.sql", e.getCause());
//        }
//    }
//
//    abstract <T> String getEntitySetNames(T entity);


    // newly dao implementation

//    private int setStatementValues(PreparedStatement statement, int setIndex, List<JdbcParameter.Field> fieldList)
//            throws DaoException {
//        if (fieldList != null) {
//            for (JdbcParameter.Field field : fieldList) {
//                if (field.getCompareColumn() == null) {
//                    DaoStatement.getInstance().setValue(statement, setIndex, field.getValue());
//                    setIndex++;
//                }
//            }
//        }
//        return setIndex;
//    }
//
//    private PreparedStatement setStatementValues(PreparedStatement statement, JdbcParameter jdbcParameter)
//            throws DaoException {
//        LOGGER.debug("STATEMENT: {}", statement);
//        Object setFieldListParameter = jdbcParameter.get(JdbcParameterType.SET_FIELD_LIST);
//        List<JdbcParameter.Field> fieldList = CastManager.getList(setFieldListParameter, JdbcParameter.Field.class);
//        if (setFieldListParameter != null && fieldList == null)
//            throw new DaoException("exception.dao.jdbc.set-statement-values.set-field-list");
//        int setIndex = setStatementValues(statement, 1, fieldList);
//        Object whereFieldListParameter = jdbcParameter.get(JdbcParameterType.WHERE_FIELD_LIST);
//        fieldList = CastManager.getList(setFieldListParameter, JdbcParameter.Field.class);
//        if (whereFieldListParameter != null && fieldList == null)
//            throw new DaoException("exception.dao.jdbc.set-statement-values.where-field-list");
//        setStatementValues(statement, setIndex, fieldList);
//        return statement;
//    }
//
//    @Override
//    public Long count(Parameter_ parameter) throws DaoException {
//        JdbcParameter jdbcParameter = JdbcParameter.of(parameter, this);
//        jdbcParameter.put(JdbcParameterType.QUERY_TYPE, QueryType.FUNCTION);
//        jdbcParameter.put(JdbcParameterType.FUNCTION, FunctionType.COUNT);
//        LOGGER.debug("jdbcParameter: {}", jdbcParameter);
//        try (PreparedStatement statement = getConnection().prepareStatement(QueryBuilder_.getSql(jdbcParameter));
//             ResultSet resultSet = setStatementValues(statement, jdbcParameter).executeQuery()) {
//            if (resultSet == null || !resultSet.next()) return null;
//            return resultSet.getLong(QueryBuilder_.FUNCTION_COUNT_RESULT);
//        } catch (SQLException e) {
//            throw new DaoException("exception.dao.jdbc.count.sql", e.getCause());
//        }
//    }
//
//    @Override
//    public <T> List<T> getAll(Parameter_ parameter)
//            throws DaoException {
//        return getAll(JdbcParameter.of(parameter, this));
//    }
//
//    <T> List<T> getAll(JdbcParameter jdbcParameter)
//            throws DaoException {
//        jdbcParameter.put(JdbcParameterType.QUERY_TYPE, QueryType.READ);
//        LOGGER.debug("jdbcParameter: {}", jdbcParameter);
//        try (PreparedStatement statement = getConnection().prepareStatement(QueryBuilder_.getSql(jdbcParameter));
//             ResultSet resultSet = setStatementValues(statement, jdbcParameter).executeQuery()) {
//            if (resultSet == null) return null;
//            return getEntityList(resultSet, jdbcParameter);
//        } catch (SQLException e) {
//            throw new DaoException("exception.dao.jdbc.get-all.sql", e.getCause());
//        }
//    }
//
//    @Override
//    public <T> T getFirst(Parameter_ parameter)
//            throws DaoException {
//        return getFirst(JdbcParameter.of(parameter, this));
//    }
//
//    <T> T getFirst(JdbcParameter jdbcParameter)
//            throws DaoException {
//        jdbcParameter.put(JdbcParameterType.QUERY_TYPE, QueryType.READ);
//        LOGGER.debug("jdbcParameter: {}", jdbcParameter);
//        try (PreparedStatement statement = getConnection().prepareStatement(QueryBuilder_.getSql(jdbcParameter));
//             ResultSet resultSet = setStatementValues(statement, jdbcParameter).executeQuery()) {
//            if (resultSet == null || !resultSet.first()) return null;
//            return getEntity(resultSet, jdbcParameter);
//        } catch (SQLException e) {
//            throw new DaoException("exception.dao.jdbc.get-first.sql", e.getCause());
//        }
//    }
//
//    abstract <T> List<T> getEntityList(ResultSet resultSet, JdbcParameter jdbcParameter) throws SQLException, DaoException;
//
//    abstract <T> T getEntity(ResultSet resultSet, JdbcParameter jdbcParameter) throws SQLException, DaoException;
//
//    abstract JdbcParameter.Column getColumn(EntityProperty entityProperty);
//
//    abstract String getParameterDefaultFrom();
//
//    abstract List<String> getParameterAllSelectColumnList();
//
//    abstract JdbcParameter.Field getParameterJoinWhereField(String joinTable) throws DaoException;

}
