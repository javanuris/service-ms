package com.epam.java.rt.lab.dao.h2;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.query.Column;
import com.epam.java.rt.lab.dao.query.Set;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.Role;
import com.epam.java.rt.lab.entity.rbac.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
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
                    throw new DaoException("exception.dao.jdbc.get-entity-column.field-name");
            }
        } catch (IllegalAccessException e) {
            throw new DaoException("exception.dao.jdbc.get-entity-column.add-column", e.getCause());
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
                    return new Set("role_id", ((Role) fieldValue(field, entity)).getId());
                case "login":
                    return new Set("login_id", ((Login) fieldValue(field, entity)).getId());
                case "avatarId":
                    return new Set("avatar_id", fieldValue(field, entity));
                default:
                    throw new DaoException("exception.dao.jdbc.get-entity-set.field-name");
            }
        } catch (IllegalAccessException e) {
            throw new DaoException("exception.dao.jdbc.get-entity-set.add-column", e.getCause());
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
            throw new DaoException("exception.dao.jdbc.get-entity-from-result-set", e.getCause());
        }
    }

    @Override
    public <T> Object getRelEntity(T entity, String relEntityName) throws DaoException {
        switch(relEntityName) {
            case "Avatar":
                return getAvatar((User) entity);
            default:
                return null;
        }
    }

    @Override
    public <T> void putRelEntity(T entity, String relEntityName, Object relEntity) throws DaoException {
        if (relEntityName.equals("Avatar")) putAvatar(entity, relEntity);
    }

    private Map<String, Object> getAvatar(User user) throws DaoException {
        List<Column> columnList = new ArrayList<>();
        columnList.add(new Column("\"Avatar\".id", user.getAvatarId()));
        String sqlString = "SELECT name, type, file FROM \"Avatar\""
                .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "="));
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlString);
             ResultSet resultSet = setPreparedStatementValues(preparedStatement, columnList).executeQuery();) {
            if (resultSet == null || !resultSet.first()) return null;
            Map<String, Object> avatarMap = new HashMap<>();
            avatarMap.put("name", resultSet.getString("name"));
            avatarMap.put("type", resultSet.getString("type"));
            avatarMap.put("file", resultSet.getBinaryStream("file"));
            return avatarMap;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.get-avatar", e.getCause());
        }
    }

    private <T> void putAvatar(T entity, Object relEntity) throws DaoException {
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
            List<Column> columnList = new ArrayList<>();
            columnList.add(new Column("\"Avatar\".name", fileName));
            columnList.add(new Column("\"Avatar\".type", contentType));
            columnList.add(new Column("\"Avatar\".file", inputStream));
            logger.debug("user.getAvatarId() = {}", user.getAvatarId());
            if (user.getAvatarId() == null) {
                logger.debug("INSERT AVATAR");
                String sqlString = "INSERT INTO \"Avatar\" (name, type, file) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatement =
                             getConnection().prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);
                     ResultSet resultSet =
                             getGeneratedKeysAfterUpdate(setPreparedStatementValues(preparedStatement, columnList));) {
                    if (resultSet.first()) user.setAvatarId(resultSet.getLong(1));
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new DaoException("exception.dao.put-avatar.sql-insert", e.getCause());
                }
            } else {
                logger.debug("UPDATE AVATAR");
                columnList.add(new Column("\"Avatar\".id", user.getAvatarId()));
                List<Column> whereList = new ArrayList<>();
                whereList.add(new Column("\"Avatar\".id", user.getAvatarId()));
                String sqlString = "UPDATE \"Avatar\" SET name = ?, type = ?, file = ?"
                        .concat(" WHERE ").concat(Column.columnListToString(whereList, "AND", "="));
                try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlString);) {
                    setPreparedStatementValues(preparedStatement, columnList).executeUpdate();
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
}
