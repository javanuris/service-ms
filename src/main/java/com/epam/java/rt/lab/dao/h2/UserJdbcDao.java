package com.epam.java.rt.lab.dao.h2;

import com.epam.java.rt.lab.dao.Argument;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.query.Column;
import com.epam.java.rt.lab.dao.query.Set;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.Role;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.util.TimestampCompare;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * service-ms
 */
public class UserJdbcDao extends JdbcDao {
    private static final Logger logger = LoggerFactory.getLogger(UserJdbcDao.class);
    private static final String DEFAULT_FROM = "\"User\"";

    public UserJdbcDao(Connection connection) throws DaoException {
        super(connection);
    }

    @Override
    String getEntityTableName() {
        return "User";
    }

    @Override
    <T> Column getEntityColumn(T entity, Field field) throws DaoException {
        try {
            switch (field.getName()) {
                case "id":
                    return new Column("id", fieldValue(field, entity));
                case "login":
                     return new Column("login_id", ((Login) fieldValue(field, entity)).getId());
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
                case "firstName":
                    return new Set("first_name", fieldValue(field, entity));
                case "middleName":
                    return new Set("middle_name", fieldValue(field, entity));
                case "lastName":
                    return new Set("last_name", fieldValue(field, entity));
                case "role":
                    Role role = (Role) fieldValue(field, entity);
                    return role == null ? new Set("role_id", null) : new Set("role_id", role.getId());
                case "login":
                    Login login = (Login) fieldValue(field, entity);
                    return login == null ? new Set("login_id", null) : new Set("login_id", login.getId());
                case "avatarId":
                    return new Set("avatar_id", fieldValue(field, entity));
                default:
                    throw new DaoException("exception.dao.jdbc.getTransfer-entity-set.field-name");
            }
        } catch (IllegalAccessException e) {
            throw new DaoException("exception.dao.jdbc.getTransfer-entity-set.add-column", e.getCause());
        }
    }

    @Override
    <T> T getEntityFromResultSet(T entity, ResultSet resultSet) throws DaoException {
        try {
            User user = (User) entity;
            if (user == null) user = new User();
            user.setId(resultSet.getLong("id"));
            user.setFirstName(resultSet.getString("first_name"));
            user.setMiddleName(resultSet.getString("middle_name"));
            user.setLastName(resultSet.getString("last_name"));
            logger.debug("resultSet.getObject('avatar_id') = {}", (Long) resultSet.getObject("avatar_id"));
            user.setAvatarId((Long) resultSet.getObject("avatar_id"));
            Role role = new Role();
            role.setId(resultSet.getLong("role_id"));
            user.setRole((new RoleJdbcDao(getConnection())).getFirst(role, "id", ""));
            Login login = new Login();
            login.setId(resultSet.getLong("login_id"));
            user.setLogin((new LoginJdbcDao(getConnection())).getFirst(login, "id", ""));
            return (T) user;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.jdbc.getTransfer-entity-from-result-set", e.getCause());
        }
    }

    @Override
    public <T> Object getRelEntity(T entity, String relEntityName) throws DaoException {
        switch(relEntityName) {
            case "Remember":
                return getRemember((String) entity);
            case "Avatar":
                return getAvatar((User) entity);
            default:
                return null;
        }
    }

    @Override
    public <T> int setRelEntity(T entity, String relEntityName, Object relEntity) throws DaoException {
        switch (relEntityName) {
            case "Remember":
                return setRemember(relEntity);
            case "Avatar":
                return setAvatar(entity, relEntity);
            default:
                return 0;
        }
    }

    @Override
    public <T> int removeRelEntity(T entity, String relEntityName) throws DaoException {
        switch (relEntityName) {
            case "Remember":
                return removeRemember(entity);
            case "Avatar":
                return removeAvatar(entity);
            default:
                return 0;
        }
    }

    private Map<String, Object> getRemember(String rememberName) throws DaoException {
        List<Column> columnList = new ArrayList<>();
        columnList.add(new Column("name", rememberName));
        String sqlString = "SELECT * FROM \"Remember\""
                .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "="));
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlString);
             ResultSet resultSet = setPreparedStatementValues(preparedStatement, columnList).executeQuery();) {
            if (resultSet == null || !resultSet.first()) return null;
            Map<String, Object> rememberMap = new HashMap<>();
            rememberMap.put("id", resultSet.getLong("id"));
            rememberMap.put("userId", resultSet.getLong("user_id"));
            rememberMap.put("name", resultSet.getString("name"));
            rememberMap.put("value", resultSet.getString("value"));
            rememberMap.put("valid", resultSet.getTimestamp("valid"));
            return rememberMap;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.getTransfer-remember", e.getCause());
        }
    }

    private int setRemember(Object relEntity) throws DaoException {
        Map<String, Object> rememberValueMap = (Map<String, Object>) relEntity;
        List<Column> columnList = new ArrayList<>();
        columnList.add(new Column("user_id", rememberValueMap.get("userId")));
        String sqlString = "SELECT id FROM \"Remember\""
                .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "="));
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlString);
             ResultSet resultSet = setPreparedStatementValues(preparedStatement, columnList).executeQuery();) {
            if (resultSet != null && resultSet.first())
                rememberValueMap.put("id", resultSet.getLong("id"));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.set-remember.sql-select", e.getCause());
        }
        List<Set> setList = new ArrayList<>();
        setList.add(new Set("user_id", rememberValueMap.get("userId")));
        setList.add(new Set("name", rememberValueMap.get("name")));
        setList.add(new Set("value", rememberValueMap.get("value")));
        setList.add(new Set("valid", rememberValueMap.get("valid")));
        if (rememberValueMap.get("id") == null) {
            logger.debug("INSERT REMEMBER");
            sqlString = "INSERT INTO \"Remember\" (user_id, name, value, valid) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement =
                         getConnection().prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);) {
                return setPreparedStatementValues(preparedStatement, setList).executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DaoException("exception.dao.set-remember.sql-insert", e.getCause());
            }
        } else {
            logger.debug("UPDATE REMEMBER");
            sqlString = "UPDATE \"Remember\" SET user_id = ?, name = ?, value = ?, valid = ?"
                    .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "="));
            try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlString);) {
                return setPreparedStatementValues(preparedStatement, setList, columnList).executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DaoException("exception.dao.set-remember.sql-update", e.getCause());
            }
        }
    }

    private <T> int removeRemember(T entity) throws DaoException {
        Long userId = (Long) entity;
        List<Column> columnList = new ArrayList<>();
        columnList.add(new Column("user_id", userId));
        String sqlString = "DELETE FROM \"Remember\""
                .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "="));
        try (PreparedStatement deleteStatement = getConnection().prepareStatement(sqlString);) {
            return setPreparedStatementValues(deleteStatement, columnList).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.remove-remember.sql-delete", e.getCause());
        }
    }

    private Map<String, Object> getAvatar(User user) throws DaoException {
        List<Column> columnList = new ArrayList<>();
        columnList.add(new Column("\"Avatar\".id", user.getAvatarId()));
        String sqlString = "SELECT name, type, file, modified FROM \"Avatar\""
                .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "="));
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlString);
             ResultSet resultSet = setPreparedStatementValues(preparedStatement, columnList).executeQuery();) {
            if (resultSet == null || !resultSet.first()) return null;
            Map<String, Object> avatarMap = new HashMap<>();
            avatarMap.put("name", resultSet.getString("name"));
            avatarMap.put("type", resultSet.getString("type"));
            avatarMap.put("file", resultSet.getBinaryStream("file"));
            avatarMap.put("modified", resultSet.getTimestamp("modified"));
            return avatarMap;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.getTransfer-avatar", e.getCause());
        }
    }

    private <T> int setAvatar(T entity, Object relEntity) throws DaoException {
        String outputFilePath = (String) relEntity;
        String outputFileName = outputFilePath.substring(outputFilePath.lastIndexOf("\\") + 1);
        int avatarInfoIndex = outputFileName.lastIndexOf(".avatar.");
        String fileName = outputFileName.substring(0, avatarInfoIndex);
        logger.debug("fileName = {}", fileName);
        avatarInfoIndex = avatarInfoIndex + 8;
        String contentType = outputFileName.substring(avatarInfoIndex,
                outputFileName.indexOf(".", avatarInfoIndex)).replaceAll("_", "/");
        logger.debug("contentType = {}", contentType);
        try (InputStream inputStream = new FileInputStream(new File(outputFilePath));) {
            User user = (User) entity;
            List<Set> setList = new ArrayList<>();
            setList.add(new Set("\"Avatar\".name", fileName));
            setList.add(new Set("\"Avatar\".type", contentType));
            setList.add(new Set("\"Avatar\".file", inputStream));
            setList.add(new Set("\"Avatar\".modified", TimestampCompare.getCurrentTimestamp()));
            logger.debug("user.getAvatarId() = {}", user.getAvatarId());
            if (user.getAvatarId() == null) {
                logger.debug("INSERT AVATAR");
                String sqlString = "INSERT INTO \"Avatar\" (name, type, file, modified) VALUES (?, ?, ?, ?)";
                try (PreparedStatement preparedStatement =
                             getConnection().prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);
                     ResultSet resultSet =
                             getGeneratedKeysAfterUpdate(setPreparedStatementValues(preparedStatement, setList));) {
                    if (resultSet.first()) user.setAvatarId(resultSet.getLong(1));
                    return 1;
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new DaoException("exception.dao.put-avatar.sql-insert", e.getCause());
                }
            } else {
                logger.debug("UPDATE AVATAR");
                List<Column> columnList = new ArrayList<>();
                columnList.add(new Column("\"Avatar\".id", user.getAvatarId()));
                String sqlString = "UPDATE \"Avatar\" SET name = ?, type = ?, file = ?, modified = ?"
                        .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "="));
                try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlString);) {
                    setPreparedStatementValues(preparedStatement, setList, columnList).executeUpdate();
                    return 1;
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new DaoException("exception.dao.put-avatar.sql-update", e.getCause());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.put-avatar.output-file-name", e.getCause());
        }
    }

    private <T> int removeAvatar(T entity) throws DaoException {
        User user = (User) entity;
        List<Set> setList = new ArrayList<>();
        setList.add(new Set("avatar_id", null));
        List<Column> columnList = new ArrayList<>();
        columnList.add(new Column("id", user.getId()));
        String sqlString = "UPDATE \"User\" SET avatar_id = ?"
                .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "="));
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlString);) {
            setPreparedStatementValues(preparedStatement, setList, columnList).executeUpdate();
            columnList.clear();
            columnList.add(new Column("\"Avatar\".id", user.getAvatarId()));
            sqlString = "DELETE FROM \"Avatar\""
                    .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "="));
            try (PreparedStatement deleteStatement = getConnection().prepareStatement(sqlString);) {
                setPreparedStatementValues(deleteStatement, columnList).executeUpdate();
                user.setAvatarId(null);
                return 1;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DaoException("exception.dao.remove-avatar.sql-delete", e.getCause());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.remove-avatar.sql-update", e.getCause());
        }
    }

    @Override
    <T> String getEntitySetNames(T entity) {
        return "firstName, middleName, lastName, role, login, avatarId";
    }


    // newly dao implementation

    @Override
    <T> List<T> getEntityList(ResultSet resultSet, Argument argument) throws SQLException, DaoException {
        List<User> userList = new ArrayList<>();
        while (resultSet.next()) userList.add(getEntity(resultSet, argument));
        return (List<T>) userList;
    }

    @Override
    <T> T getEntity(ResultSet resultSet, Argument argument) throws SQLException, DaoException {
        Map<String, List<String>> subEntityColumnListMap = new HashMap<>();
        User user = new User();
        for (String columnName : (List<String>) argument.get(ArgumentType.SELECT_COLUMN_LIST)) {
            if (columnName.startsWith(DEFAULT_FROM.concat("."))) {
                String shortColumnName = columnName.substring(DEFAULT_FROM.length() + 1);
                switch (shortColumnName) {
                    case "id":
                        user.setId(resultSet.getLong(shortColumnName));
                        break;
                    case "first_name":
                        user.setFirstName((String) resultSet.getObject(shortColumnName));
                        break;
                    case "middle_name":
                        user.setMiddleName((String) resultSet.getObject(shortColumnName));
                        break;
                    case "last_name":
                        user.setLastName((String) resultSet.getObject(shortColumnName));
                        break;
                    case "avatar_id":
                        user.setAvatarId((Long) resultSet.getObject(shortColumnName));
                        break;
                    case "role_id":
                        Long role_id = (Long) resultSet.getObject(columnName);
                        if (role_id != null)
                            user.setRole(new RoleJdbcDao(getConnection()).getFirst(
                                    new Argument().put(ArgumentType.WHERE_LIST, Argument.Field.set("id", role_id))
                            ));
                        break;
                    case "login_id":
                        Long login_id = (Long) resultSet.getObject(columnName);
                        if (login_id != null) {
                            user.setLogin(new LoginJdbcDao(getConnection()).getFirst(
                                    new Argument().put(ArgumentType.WHERE_LIST, Argument.Field.set("id", login_id))
                            ));
                        }
                        break;
                }
            } else {
                String subEntityName = columnName.split("\\.")[0];
                List<String> subEntityColumnList = subEntityColumnListMap.get(subEntityName);
                if (subEntityColumnList == null) {
                    subEntityColumnList = new ArrayList<>();
                    subEntityColumnListMap.put(subEntityName, subEntityColumnList);
                }
                subEntityColumnList.add(columnName);
            }
        }
        for (Map.Entry<String, List<String>> subEntityEntry : subEntityColumnListMap.entrySet()) {
            switch (subEntityEntry.getKey()) {
                case "\"Role\"":
                    user.setRole(new RoleJdbcDao(getConnection()).getEntity(
                            resultSet,
                            new Argument().put(ArgumentType.SELECT_COLUMN_LIST, subEntityEntry.getValue())
                    ));
                    break;
                case "\"Login\"":
                    user.setLogin(new LoginJdbcDao(getConnection()).getEntity(
                            resultSet,
                            new Argument().put(ArgumentType.SELECT_COLUMN_LIST, subEntityEntry.getValue())
                    ));
                    break;
            }
        }
        return (T) user;
    }

    @Override
    Argument.Field getJoinWhere(String joinTable) throws DaoException {
        logger.debug("getJoinTable {}", joinTable);
        switch (joinTable) {
            case "\"Role\"":
                return Argument.Field.set(DEFAULT_FROM.concat(".role_id"), null, joinTable.concat(".id"));
            case "\"Login\"":
                return Argument.Field.set(DEFAULT_FROM.concat(".login_id"), null, joinTable.concat(".id"));
        }
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
        columnList.add(DEFAULT_FROM.concat(".first_name"));
        columnList.add(DEFAULT_FROM.concat(".middle_name"));
        columnList.add(DEFAULT_FROM.concat(".last_name"));
        columnList.add(DEFAULT_FROM.concat(".role_id"));
        columnList.add(DEFAULT_FROM.concat(".login_id"));
        columnList.add(DEFAULT_FROM.concat(".avatar_id"));
        return columnList;
    }
}
