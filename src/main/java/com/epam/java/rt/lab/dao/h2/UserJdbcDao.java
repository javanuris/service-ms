package com.epam.java.rt.lab.dao.h2;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.query.Column;
import com.epam.java.rt.lab.dao.query.Set;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.Role;
import com.epam.java.rt.lab.entity.rbac.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * service-ms
 */
public class UserJdbcDao extends JdbcDao {

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
            Role role = new Role();
            role.setId(resultSet.getLong("role_id"));
            user.setRole((new RoleJdbcDao(getConnection())).getFirst(role, "id"));
            Login login = new Login();
            login.setId(resultSet.getLong("login_id"));
            user.setLogin((new LoginJdbcDao(getConnection())).getFirst(login, "id"));
            return (T) user;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.jdbc.get-entity-from-result-set", e.getCause());
        }
    }

    @Override
    public <T> Object getRelEntity(T entity, String relEntityName) throws DaoException {
        if (relEntityName.equals("Avatar")) return getAvatar(entity);
        return null;
    }

    @Override
    public <T> void putRelEntity(T entity, String relEntityName, Object relEntity) throws DaoException {
        if (relEntityName.equals("Avatar")) putAvatar(entity, relEntity);
    }

    private <T> Object getAvatar(T entity) throws DaoException {
        ResultSet relResultSet = null;
        try {
            User user = (User) entity;
            List<Column> columnList = new ArrayList<>();
            columnList.add(new Column("\"Avatar\".id", user.getAvatarId()));
            PreparedStatement preparedStatement = rawQuery("SELECT file FROM \"Avatar\""
                            .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "=")),
                            columnList);
            preparedStatement.execute();
            relResultSet = preparedStatement.getResultSet();
            if (relResultSet.first()) return relResultSet.getBinaryStream("file");
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.user.get-avatar", e.getCause());
        } finally {
            try {
                if (relResultSet != null) relResultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DaoException("exception.dao.get-avatar.resultset-close", e.getCause());
            }
        }
    }

    private <T> void putAvatar(T entity, Object relEntity) throws DaoException {
        ResultSet relResultSet = null;
        try {
            User user = (User) entity;
            String outputFileName = (String) relEntity;
            String fileName = outputFileName.substring(outputFileName.lastIndexOf("\\") + 1);
            fileName = fileName.substring(0, fileName.lastIndexOf("_upload"));
            System.out.println(fileName + " >> " + outputFileName);
            InputStream inputStream = new FileInputStream(new File(outputFileName));
            PreparedStatement preparedStatement = null;
            List<Column> columnList = new ArrayList<>();
            columnList.add(new Column("\"Avatar\".name", fileName));
            columnList.add(new Column("\"Avatar\".file", inputStream));
            if (user.getAvatarId() == null) {
                preparedStatement = rawQuery("INSERT INTO \"Avatar\" (name, file) VALUES (?, ?)",
                        columnList, PreparedStatement.RETURN_GENERATED_KEYS);
            } else {
                columnList.add(new Column("\"Avatar\".id", user.getAvatarId()));
                preparedStatement = rawQuery("INSERT INTO \"Avatar\" (name, file) VALUES (?, ?)"
                        .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "=")),
                        columnList, PreparedStatement.RETURN_GENERATED_KEYS);
            }
            preparedStatement.executeUpdate();
            relResultSet = preparedStatement.getGeneratedKeys();
            if (relResultSet.first()) user.setAvatarId(relResultSet.getLong(1));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.user.put-avatar.file", e.getCause());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.user.put-avatar.update", e.getCause());
        } finally {
            try {
                if (relResultSet != null) relResultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DaoException("exception.dao.put-avatar.resultset-close", e.getCause());
            }
        }

    }
}
