package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.types.JdbcParameterType;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.Restore;
import com.epam.java.rt.lab.util.CastManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * service-ms
 */
public class LoginJdbcDao extends JdbcDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginJdbcDao.class);

    private static final String DEFAULT_TABLE = "\"Login\"";
    private static final String DEFAULT_JOIN_COLUMN = "id";
    private static final String TABLE_RESTORE = "\"Restore\"";
    private static final String RESTORE_JOIN_COLUMN = "login_id";

    public LoginJdbcDao(Connection connection) throws DaoException {
        super(connection);
    }

//    @Override
//    String getEntityTableName() {
//        return "Login";
//    }
//
//    @Override
//    <T> Column getEntityColumn(T entity, Property field) throws DaoException {
//        try {
//            switch (field.getName()) {
//                case "id":
//                    return new Column("id", fieldValue(field, entity));
//                case "email.regex":
//                    return new Column("email.regex", fieldValue(field, entity));
//                case "password.regex":
//                    return new Column("password.regex", fieldValue(field, entity));
//                default:
//                    throw new DaoException("exception.dao.jdbc.getTransfer-entity-column.field-name");
//            }
//        } catch (IllegalAccessException e) {
//            throw new DaoException("exception.dao.jdbc.getTransfer-entity-column.add-column", e.getCause());
//        }
//    }
//
//    @Override
//    <T> Set getEntitySet(T entity, Property field) throws DaoException {
//        try {
//            switch (field.getName()) {
//                case "id":
//                    return new Set("id", fieldValue(field, entity));
//                case "email.regex":
//                    return new Set("email.regex", fieldValue(field, entity));
//                case "password.regex":
//                    return new Set("password.regex", fieldValue(field, entity));
//                case "attemptLeft":
//                    return new Set("attempt_left", fieldValue(field, entity));
//                case "status":
//                    return new Set("status", fieldValue(field, entity));
//                default:
//                    throw new DaoException("exception.dao.jdbc.getTransfer-entity-column.field-name");
//            }
//        } catch (IllegalAccessException e) {
//            throw new DaoException("exception.dao.jdbc.getTransfer-entity-set.add-column", e.getCause());
//        }
//    }
//
//    @Override
//    <T> T getEntityFromResultSet(T entity, ResultSet resultSet) throws DaoException {
//        try {
//            Login login = (Login) entity;
//            if (login == null) login = new Login();
//            login.setId(resultSet.getLong("id"));
//            login.setEmail(resultSet.getString("email.regex"));
//            login.setPassword(resultSet.getString("password.regex"));
//            login.setAttemptLeft(resultSet.getInt("attempt_left"));
//            login.setStatus(resultSet.getInt("status"));
//            return (T) login;
//        } catch (SQLException e) {
//            throw new DaoException("exception.dao.jdbc.getTransfer-entity-from-result-set", e.getCause());
//        }
//    }
//
//    @Override
//    public <T> Object getRelEntity(T entity, String relEntityName) throws DaoException {
//        switch(relEntityName) {
//            case "Activation":
//                return getActivation(entity);
//            case "Forgot":
//                return getForgot(entity);
//            default:
//                return null;
//        }
//    }
//
//    @Override
//    public <T> int setRelEntity(T entity, String relEntityName, Object relEntity) throws DaoException {
//        switch (relEntityName) {
//            case "Activation":
//                return setActivation(entity, relEntity);
//            case "Forgot":
//                return setForgot(entity, relEntity);
//            default:
//                return 0;
//        }
//    }
//
//    @Override
//    public <T> int removeRelEntity(T entity, String relEntityName) throws DaoException {
//        switch (relEntityName) {
//            case "Activation":
//                return removeActivation(entity);
//            case "Forgot":
//                return removeForgot(entity);
//            default:
//                return 0;
//        }
//    }
//
//    private <T> Map<String, Object> getActivation(T entity) throws DaoException {
//        Login login = (Login) entity;
//        List<Column> columnList = new ArrayList<>();
//        columnList.add(new Column("email.regex", login.getEmail()));
//        String sqlString = "SELECT * FROM \"Activation\""
//                .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "="));
//        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlString);
//             ResultSet resultSet = setPreparedStatementValues(preparedStatement, columnList).executeQuery();) {
//            if (resultSet == null || !resultSet.first()) return null;
//            Map<String, Object> activationMap = new HashMap<>();
//            activationMap.put("id", resultSet.getLong("id"));
//            activationMap.put("email.regex", resultSet.getString("email.regex"));
//            activationMap.put("password.regex", resultSet.getString("password.regex"));
//            activationMap.put("code", resultSet.getString("code"));
//            activationMap.put("valid", resultSet.getTimestamp("valid"));
//            return activationMap;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new DaoException("exception.dao.getTransfer-activation", e.getCause());
//        }
//    }
//
//    private <T> int setActivation(T entity, Object relEntity) throws DaoException {
//        Login login = (Login) entity;
//        String activationCode = (String) relEntity;
//        Long id = null;
//        List<Column> columnList = new ArrayList<>();
//        columnList.add(new Column("email.regex", login.getEmail()));
//        String sqlString = "SELECT id FROM \"Activation\""
//                .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "="));
//        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlString);
//             ResultSet resultSet = setPreparedStatementValues(preparedStatement, columnList).executeQuery();) {
//            if (resultSet != null && resultSet.first())
//                id = resultSet.getLong("id");
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new DaoException("exception.dao.set-activation.sql-select", e.getCause());
//        }
//        List<Set> setList = new ArrayList<>();
//        setList.add(new Set("email.regex", login.getEmail()));
//        setList.add(new Set("password.regex", login.getPassword()));
//        setList.add(new Set("code", activationCode));
//        setList.add(new Set("valid", TimestampCompare.daysToTimestamp(TimestampCompare.getCurrentTimestamp(),
//                Integer.valueOf(GlobalProperties.getProperty("activation.days.valid")))));
//        if (id == null) {
//            LOGGER.debug("INSERT ACTIVATION");
//            sqlString = "INSERT INTO \"Activation\" (email, password, code, valid) VALUES (?, ?, ?, ?)";
//            try (PreparedStatement preparedStatement =
//                         getConnection().prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);) {
//                return setPreparedStatementValues(preparedStatement, setList).executeUpdate();
//            } catch (SQLException e) {
//                e.printStackTrace();
//                throw new DaoException("exception.dao.set-activation.sql-insert", e.getCause());
//            }
//        } else {
//            LOGGER.debug("UPDATE ACTIVATION");
//            sqlString = "UPDATE \"Activation\" SET email = ?, password = ?, code = ?, valid = ?"
//                    .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "="));
//            try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlString);) {
//                return setPreparedStatementValues(preparedStatement, setList, columnList).executeUpdate();
//            } catch (SQLException e) {
//                e.printStackTrace();
//                throw new DaoException("exception.dao.set-activation.sql-update", e.getCause());
//            }
//        }
//    }
//
//    private <T> int removeActivation(T entity) throws DaoException {
//        Login login = (Login) entity;
//        List<Column> columnList = new ArrayList<>();
//        columnList.add(new Column("email.regex", login.getEmail()));
//        String sqlString = "DELETE FROM \"Activation\""
//                .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "="));
//        try (PreparedStatement deleteStatement = getConnection().prepareStatement(sqlString);) {
//            return setPreparedStatementValues(deleteStatement, columnList).executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new DaoException("exception.dao.remove-activation.sql-delete", e.getCause());
//        }
//    }
//
//    private <T> Map<String, Object> getForgot(T entity) throws DaoException {
//        Login login = (Login) entity;
//        List<Column> columnList = new ArrayList<>();
//        columnList.add(new Column("email.regex", login.getEmail()));
//        String sqlString = "SELECT * FROM \"Forgot\""
//                .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "="));
//        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlString);
//             ResultSet resultSet = setPreparedStatementValues(preparedStatement, columnList).executeQuery();) {
//            if (resultSet == null || !resultSet.first()) return null;
//            Map<String, Object> forgotMap = new HashMap<>();
//            forgotMap.put("id", resultSet.getLong("id"));
//            forgotMap.put("email.regex", resultSet.getString("email.regex"));
//            forgotMap.put("code", resultSet.getString("code"));
//            forgotMap.put("valid", resultSet.getTimestamp("valid"));
//            return forgotMap;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new DaoException("exception.dao.getTransfer-activation", e.getCause());
//        }
//    }
//
//    private <T> int setForgot(T entity, Object relEntity) throws DaoException {
//        Login login = (Login) entity;
//        String forgotCode = (String) relEntity;
//        Long id = null;
//        List<Column> columnList = new ArrayList<>();
//        columnList.add(new Column("email.regex", login.getEmail()));
//        String sqlString = "SELECT id FROM \"Forgot\""
//                .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "="));
//        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlString);
//             ResultSet resultSet = setPreparedStatementValues(preparedStatement, columnList).executeQuery();) {
//            if (resultSet != null && resultSet.first())
//                id = resultSet.getLong("id");
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new DaoException("exception.dao.set-forgot.sql-select", e.getCause());
//        }
//        List<Set> setList = new ArrayList<>();
//        setList.add(new Set("email.regex", login.getEmail()));
//        setList.add(new Set("code", forgotCode));
//        setList.add(new Set("valid", TimestampCompare.daysToTimestamp(TimestampCompare.getCurrentTimestamp(),
//                Integer.valueOf(GlobalProperties.getProperty("forgot.seconds.valid")))));
//        if (id == null) {
//            LOGGER.debug("INSERT FORGOT");
//            sqlString = "INSERT INTO \"Forgot\" (email, code, valid) VALUES (?, ?, ?)";
//            try (PreparedStatement preparedStatement =
//                         getConnection().prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);) {
//                return setPreparedStatementValues(preparedStatement, setList).executeUpdate();
//            } catch (SQLException e) {
//                e.printStackTrace();
//                throw new DaoException("exception.dao.set-forgot.sql-insert", e.getCause());
//            }
//        } else {
//            LOGGER.debug("UPDATE FORGOT");
//            sqlString = "UPDATE \"Forgot\" SET email = ?, code = ?, valid = ?"
//                    .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "="));
//            try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlString);) {
//                return setPreparedStatementValues(preparedStatement, setList, columnList).executeUpdate();
//            } catch (SQLException e) {
//                e.printStackTrace();
//                throw new DaoException("exception.dao.set-forgot.sql-update", e.getCause());
//            }
//        }
//    }
//
//    private <T> int removeForgot(T entity) throws DaoException {
//        Login login = (Login) entity;
//        List<Column> columnList = new ArrayList<>();
//        columnList.add(new Column("email.regex", login.getEmail()));
//        String sqlString = "DELETE FROM \"Forgot\""
//                .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "="));
//        try (PreparedStatement deleteStatement = getConnection().prepareStatement(sqlString);) {
//            return setPreparedStatementValues(deleteStatement, columnList).executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new DaoException("exception.dao.remove-forgot.sql-delete", e.getCause());
//        }
//    }
//
//    @Override
//    <T> String getEntitySetNames(T entity) {
//        return "email, password, attemptLeft, status";
//    }
//

    // newly dao implementation

    @Override
    <T> List<T> getEntityList(ResultSet resultSet, JdbcParameter jdbcParameter) throws SQLException, DaoException {
        List<Login> loginList = new ArrayList<>();
        while (resultSet.next()) loginList.add(getEntity(resultSet, jdbcParameter));
        return (List<T>) loginList;
    }

    @Override
    <T> T getEntity(ResultSet resultSet, JdbcParameter jdbcParameter) throws SQLException, DaoException {
        LOGGER.debug("loginJdbcDao.getEntity");
        Login login = new Login();
        List<JdbcParameter.Column> columnList =
                CastManager.getList(jdbcParameter.get(JdbcParameterType.SELECT_COLUMN_LIST), JdbcParameter.Column.class);
        for (JdbcParameter.Column column : columnList) {
            if (column.getTableName().equals(DEFAULT_TABLE)) {
                switch (column.getColumnName()) {
                    case "id":
                        login.setId(resultSet.getLong(column.getColumnName()));
                        break;
                    case "email":
                        login.setEmail((String) resultSet.getObject(column.getColumnName()));
                        break;
                    case "password":
                        login.setPassword((String) resultSet.getObject(column.getColumnName()));
                        break;
                    case "attempt_left":
                        login.setAttemptLeft((Integer) resultSet.getObject(column.getColumnName()));
                        break;
                    case "status":
                        login.setStatus((Integer) resultSet.getObject(column.getColumnName()));
                        break;
                }
            }
        }
        return (T) login;
    }

    @Override
    String getTableName(String entityClassName) {
        if (entityClassName.equals(Login.class.getSimpleName())) {
            return DEFAULT_TABLE;
        } else if (entityClassName.equals(Restore.class.getSimpleName())) {
            return TABLE_RESTORE;
        }
        return "";
    }

    @Override
    JdbcParameter.Field getParameterJoinWhereField(String joinTableName) throws DaoException {
        if (joinTableName.equals(TABLE_RESTORE)) {
            return new JdbcParameter.Field(
                    null,
                    new JdbcParameter.Column(DEFAULT_TABLE, DEFAULT_JOIN_COLUMN),
                    new JdbcParameter.Column(TABLE_RESTORE, RESTORE_JOIN_COLUMN)
            );
        }
        throw new DaoException("exception.dao.jdbc.login.get.parameter-join-where-field");
    }

    @Override
    String getParameterDefaultFrom() {
        return DEFAULT_TABLE;
    }

    @Override
    List<String> getParameterAllSelectColumnList() {
        List<String> columnList = new ArrayList<>();
        columnList.add(DEFAULT_TABLE.concat(".id"));
        columnList.add(DEFAULT_TABLE.concat(".email"));
        columnList.add(DEFAULT_TABLE.concat(".password"));
        columnList.add(DEFAULT_TABLE.concat(".attempt_left"));
        columnList.add(DEFAULT_TABLE.concat(".status"));
        return columnList;
    }

}
