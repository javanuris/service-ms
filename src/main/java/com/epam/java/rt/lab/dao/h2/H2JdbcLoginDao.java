package com.epam.java.rt.lab.dao.h2;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.rbac.Login;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * service-ms
 */
public class H2JdbcLoginDao extends H2JdbcDao implements Dao {
    private static final Logger logger = LoggerFactory.getLogger(H2JdbcLoginDao.class);

    public H2JdbcLoginDao() throws DaoException {
        super.resetProperties();
    }

    @Override
    String getEntityTableName() {
        return "Login";
    }

    @Override
    Object getEntity(ResultSet resultSet) throws SQLException {
        Login login = new Login();
        login.setId(resultSet.getLong("id"));
        login.setEmail(resultSet.getString("email"));
        login.setPassword(resultSet.getString("password"));
        login.setAttemptLeft(resultSet.getInt("attempt_left"));
        login.setStatus(resultSet.getInt("status"));
        return login;
    }

    @Override
    <T> T getValue(Object entity, String columnName) throws DaoException {
        Login login = (Login) entity;
        if (columnName.equals("id")) {
            return (T) login.getId();
        } else if (columnName.equals("email")) {
            return (T) login.getEmail();
        } else if (columnName.equals("password")) {
            return (T) login.getPassword();
        } else if (columnName.equals("attempt_left")) {
            return (T) Integer.valueOf(login.getAttemptLeft());
        } else if (columnName.equals("status")) {
            return (T) Integer.valueOf(login.getStatus());
        }
        throw new DaoException("Field assigned to column");
    }
}
