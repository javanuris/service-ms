package com.epam.java.rt.lab.dao.h2;

import com.epam.java.rt.lab.dao.Argument;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.query.Column;
import com.epam.java.rt.lab.dao.query.Set;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.util.GlobalProperties;
import com.epam.java.rt.lab.util.TimestampCompare;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * service-ms
 */
public class LoginJdbcDao extends JdbcDao {
    private static final String DEFAULT_FROM = "\"Login\"";
    private static final Logger logger = LoggerFactory.getLogger(LoginJdbcDao.class);

    public LoginJdbcDao(Connection connection) throws DaoException {
        super(connection);
    }

    @Override
    String getEntityTableName() {
        return "Login";
    }

    @Override
    <T> Column getEntityColumn(T entity, Field field) throws DaoException {
        try {
            switch (field.getName()) {
                case "id":
                    return new Column("id", fieldValue(field, entity));
                case "email":
                    return new Column("email", fieldValue(field, entity));
                case "password":
                    return new Column("password", fieldValue(field, entity));
                default:
                    throw new DaoException("exception.dao.jdbc.getTransfer-entity-column.field-name");
            }
        } catch (IllegalAccessException e) {
            throw new DaoException("exception.dao.jdbc.getTransfer-entity-column.add-column", e.getCause());
        }
    }

    @Override
    <T> Set getEntitySet(T entity, Field field) throws DaoException {
        try {
            switch (field.getName()) {
                case "id":
                    return new Set("id", fieldValue(field, entity));
                case "email":
                    return new Set("email", fieldValue(field, entity));
                case "password":
                    return new Set("password", fieldValue(field, entity));
                case "attemptLeft":
                    return new Set("attempt_left", fieldValue(field, entity));
                case "status":
                    return new Set("status", fieldValue(field, entity));
                default:
                    throw new DaoException("exception.dao.jdbc.getTransfer-entity-column.field-name");
            }
        } catch (IllegalAccessException e) {
            throw new DaoException("exception.dao.jdbc.getTransfer-entity-set.add-column", e.getCause());
        }
    }

    @Override
    <T> T getEntityFromResultSet(T entity, ResultSet resultSet) throws DaoException {
        try {
            Login login = (Login) entity;
            if (login == null) login = new Login();
            login.setId(resultSet.getLong("id"));
            login.setEmail(resultSet.getString("email"));
            login.setPassword(resultSet.getString("password"));
            login.setAttemptLeft(resultSet.getInt("attempt_left"));
            login.setStatus(resultSet.getInt("status"));
            return (T) login;
        } catch (SQLException e) {
            throw new DaoException("exception.dao.jdbc.getTransfer-entity-from-result-set", e.getCause());
        }
    }

    @Override
    public <T> Object getRelEntity(T entity, String relEntityName) throws DaoException {
        switch(relEntityName) {
            case "Activation":
                return getActivation(entity);
            case "Forgot":
                return getForgot(entity);
            default:
                return null;
        }
    }

    @Override
    public <T> int setRelEntity(T entity, String relEntityName, Object relEntity) throws DaoException {
        switch (relEntityName) {
            case "Activation":
                return setActivation(entity, relEntity);
            case "Forgot":
                return setForgot(entity, relEntity);
            default:
                return 0;
        }
    }

    @Override
    public <T> int removeRelEntity(T entity, String relEntityName) throws DaoException {
        switch (relEntityName) {
            case "Activation":
                return removeActivation(entity);
            case "Forgot":
                return removeForgot(entity);
            default:
                return 0;
        }
    }

    private <T> Map<String, Object> getActivation(T entity) throws DaoException {
        Login login = (Login) entity;
        List<Column> columnList = new ArrayList<>();
        columnList.add(new Column("email", login.getEmail()));
        String sqlString = "SELECT * FROM \"Activation\""
                .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "="));
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlString);
             ResultSet resultSet = setPreparedStatementValues(preparedStatement, columnList).executeQuery();) {
            if (resultSet == null || !resultSet.first()) return null;
            Map<String, Object> activationMap = new HashMap<>();
            activationMap.put("id", resultSet.getLong("id"));
            activationMap.put("email", resultSet.getString("email"));
            activationMap.put("password", resultSet.getString("password"));
            activationMap.put("code", resultSet.getString("code"));
            activationMap.put("valid", resultSet.getTimestamp("valid"));
            return activationMap;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.getTransfer-activation", e.getCause());
        }
    }

    private <T> int setActivation(T entity, Object relEntity) throws DaoException {
        Login login = (Login) entity;
        String activationCode = (String) relEntity;
        Long id = null;
        List<Column> columnList = new ArrayList<>();
        columnList.add(new Column("email", login.getEmail()));
        String sqlString = "SELECT id FROM \"Activation\""
                .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "="));
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlString);
             ResultSet resultSet = setPreparedStatementValues(preparedStatement, columnList).executeQuery();) {
            if (resultSet != null && resultSet.first())
                id = resultSet.getLong("id");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.set-activation.sql-select", e.getCause());
        }
        List<Set> setList = new ArrayList<>();
        setList.add(new Set("email", login.getEmail()));
        setList.add(new Set("password", login.getPassword()));
        setList.add(new Set("code", activationCode));
        setList.add(new Set("valid", TimestampCompare.daysToTimestamp(TimestampCompare.getCurrentTimestamp(),
                Integer.valueOf(GlobalProperties.getProperty("activation.days.valid")))));
        if (id == null) {
            logger.debug("INSERT ACTIVATION");
            sqlString = "INSERT INTO \"Activation\" (email, password, code, valid) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement =
                         getConnection().prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);) {
                return setPreparedStatementValues(preparedStatement, setList).executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DaoException("exception.dao.set-activation.sql-insert", e.getCause());
            }
        } else {
            logger.debug("UPDATE ACTIVATION");
            sqlString = "UPDATE \"Activation\" SET email = ?, password = ?, code = ?, valid = ?"
                    .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "="));
            try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlString);) {
                return setPreparedStatementValues(preparedStatement, setList, columnList).executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DaoException("exception.dao.set-activation.sql-update", e.getCause());
            }
        }
    }

    private <T> int removeActivation(T entity) throws DaoException {
        Login login = (Login) entity;
        List<Column> columnList = new ArrayList<>();
        columnList.add(new Column("email", login.getEmail()));
        String sqlString = "DELETE FROM \"Activation\""
                .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "="));
        try (PreparedStatement deleteStatement = getConnection().prepareStatement(sqlString);) {
            return setPreparedStatementValues(deleteStatement, columnList).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.remove-activation.sql-delete", e.getCause());
        }
    }

    private <T> Map<String, Object> getForgot(T entity) throws DaoException {
        Login login = (Login) entity;
        List<Column> columnList = new ArrayList<>();
        columnList.add(new Column("email", login.getEmail()));
        String sqlString = "SELECT * FROM \"Forgot\""
                .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "="));
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlString);
             ResultSet resultSet = setPreparedStatementValues(preparedStatement, columnList).executeQuery();) {
            if (resultSet == null || !resultSet.first()) return null;
            Map<String, Object> forgotMap = new HashMap<>();
            forgotMap.put("id", resultSet.getLong("id"));
            forgotMap.put("email", resultSet.getString("email"));
            forgotMap.put("code", resultSet.getString("code"));
            forgotMap.put("valid", resultSet.getTimestamp("valid"));
            return forgotMap;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.getTransfer-activation", e.getCause());
        }
    }

    private <T> int setForgot(T entity, Object relEntity) throws DaoException {
        Login login = (Login) entity;
        String forgotCode = (String) relEntity;
        Long id = null;
        List<Column> columnList = new ArrayList<>();
        columnList.add(new Column("email", login.getEmail()));
        String sqlString = "SELECT id FROM \"Forgot\""
                .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "="));
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlString);
             ResultSet resultSet = setPreparedStatementValues(preparedStatement, columnList).executeQuery();) {
            if (resultSet != null && resultSet.first())
                id = resultSet.getLong("id");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.set-forgot.sql-select", e.getCause());
        }
        List<Set> setList = new ArrayList<>();
        setList.add(new Set("email", login.getEmail()));
        setList.add(new Set("code", forgotCode));
        setList.add(new Set("valid", TimestampCompare.daysToTimestamp(TimestampCompare.getCurrentTimestamp(),
                Integer.valueOf(GlobalProperties.getProperty("forgot.seconds.valid")))));
        if (id == null) {
            logger.debug("INSERT FORGOT");
            sqlString = "INSERT INTO \"Forgot\" (email, code, valid) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement =
                         getConnection().prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);) {
                return setPreparedStatementValues(preparedStatement, setList).executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DaoException("exception.dao.set-forgot.sql-insert", e.getCause());
            }
        } else {
            logger.debug("UPDATE FORGOT");
            sqlString = "UPDATE \"Forgot\" SET email = ?, code = ?, valid = ?"
                    .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "="));
            try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlString);) {
                return setPreparedStatementValues(preparedStatement, setList, columnList).executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DaoException("exception.dao.set-forgot.sql-update", e.getCause());
            }
        }
    }

    private <T> int removeForgot(T entity) throws DaoException {
        Login login = (Login) entity;
        List<Column> columnList = new ArrayList<>();
        columnList.add(new Column("email", login.getEmail()));
        String sqlString = "DELETE FROM \"Forgot\""
                .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "="));
        try (PreparedStatement deleteStatement = getConnection().prepareStatement(sqlString);) {
            return setPreparedStatementValues(deleteStatement, columnList).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.remove-forgot.sql-delete", e.getCause());
        }
    }

    @Override
    <T> String getEntitySetNames(T entity) {
        return "email, password, attemptLeft, status";
    }


    // newly dao implementation

    @Override
    <T> List<T> getEntityList(ResultSet resultSet, Argument argument) throws SQLException, DaoException {
        List<Login> loginList = new ArrayList<>();
        while (resultSet.next()) loginList.add(getEntity(resultSet, argument));
        return (List<T>) loginList;
    }

    @Override
    <T> T getEntity(ResultSet resultSet, Argument argument) throws SQLException, DaoException {
        Login login = new Login();
        for (String columnName : (List<String>) argument.get(ArgumentType.SELECT_COLUMN_LIST)) {
            if (columnName.startsWith(DEFAULT_FROM.concat("."))) {
                String shortColumnName = columnName.substring(DEFAULT_FROM.length() + 1);
                switch (shortColumnName) {
                    case "id":
                        login.setId(resultSet.getLong(shortColumnName));
                        break;
                    case "email":
                        login.setEmail((String) resultSet.getObject(shortColumnName));
                        break;
                    case "password":
                        login.setPassword((String) resultSet.getObject(shortColumnName));
                        break;
                    case "attempt_left":
                        login.setAttemptLeft((Integer) resultSet.getObject(shortColumnName));
                        break;
                    case "status":
                        login.setStatus((Integer) resultSet.getObject(shortColumnName));
                        break;
                }
            }
        }
        return (T) login;
    }

    @Override
    Argument.Field getJoinWhere(String joinTable) throws DaoException {
        throw new DaoException("exception.dao.jdbc.get-join-where");
    }

    @Override
    String getDefaultFrom() {
        return DEFAULT_FROM;
    }

    @Override
    List<String> getAllSelectColumnList() {
        List<String> columnList = new ArrayList<>();
        columnList.add(DEFAULT_FROM.concat(".id"));
        columnList.add(DEFAULT_FROM.concat(".email"));
        columnList.add(DEFAULT_FROM.concat(".password"));
        columnList.add(DEFAULT_FROM.concat(".attemptLeft"));
        columnList.add(DEFAULT_FROM.concat(".status"));
        return columnList;
    }


}
