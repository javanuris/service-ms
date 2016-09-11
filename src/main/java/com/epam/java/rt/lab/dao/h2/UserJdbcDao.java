package com.epam.java.rt.lab.dao.h2;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.Dao_;
import com.epam.java.rt.lab.dao.query.Column;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.Role;
import com.epam.java.rt.lab.entity.rbac.User;

import java.lang.reflect.Field;
import java.sql.Connection;
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
                    System.out.println("!!! = " + ((Login) fieldValue(field, entity)).getId());
                    return new Column("login_id", ((Login) fieldValue(field, entity)).getId());
                default:
                    throw new DaoException("exception.dao.jdbc.entity-column.field-name");
            }
        } catch (IllegalAccessException e) {
            throw new DaoException("exception.dao.jdbc.entity-column.column", e.getCause());
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
    public <T> List<T> getAll(T entity, String fieldNames) throws DaoException {
        throw new DaoException("exception.dao.jdbc.unsupported");
    }

    @Override
    public <T> List<T> getAll(T entity, String fieldNames, String columnNames) throws DaoException {
        throw new DaoException("exception.dao.jdbc.unsupported");
    }

}
