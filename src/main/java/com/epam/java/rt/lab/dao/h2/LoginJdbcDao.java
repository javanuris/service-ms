package com.epam.java.rt.lab.dao.h2;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.query.Column;
import com.epam.java.rt.lab.dao.query.Set;
import com.epam.java.rt.lab.entity.rbac.Login;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * service-ms
 */
public class LoginJdbcDao extends JdbcDao {
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
                case "email":
                    return new Set("email", fieldValue(field, entity));
                case "password":
                    return new Set("password", fieldValue(field, entity));
                case "attemptLeft":
                    return new Set("attempt_left", fieldValue(field, entity));
                case "status":
                    return new Set("status", fieldValue(field, entity));
                default:
                    throw new DaoException("exception.dao.jdbc.get-entity-column.field-name");
            }
        } catch (IllegalAccessException e) {
            throw new DaoException("exception.dao.jdbc.get-entity-set.add-column", e.getCause());
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
