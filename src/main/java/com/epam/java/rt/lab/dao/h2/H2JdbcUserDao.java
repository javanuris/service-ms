package com.epam.java.rt.lab.dao.h2;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * service-ms
 */
public class H2JdbcUserDao extends H2JdbcDao implements Dao {
    private static final Logger logger = LoggerFactory.getLogger(H2JdbcUserDao.class);

    public H2JdbcUserDao() throws DaoException {
        super.resetProperties();
    }

    @Override
    String getEntityTableName() {
        return "User";
    }

    @Override
    Object getEntity(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setMiddleName(resultSet.getString("middle_name"));
        user.setLastName(resultSet.getString("last_name"));
        return user;
    }

    @Override
    <T> T getValue(Object entity, String columnName) throws DaoException {
        User user = (User) entity;
        if (columnName.equals("id")) {
            return (T) user.getId();
        } else if (columnName.equals("first_name")) {
            return (T) user.getFirstName();
        } else if (columnName.equals("middle_name")) {
            return (T) user.getMiddleName();
        } else if (columnName.equals("last_name")) {
            return (T) user.getLastName();
        } else if (columnName.equals("login_id")) {
            if (user.getLogin() == null) return null;
            return (T) user.getLogin().getId();
        } else if (columnName.equals("role_id")) {
            if (user.getRole() == null) return null;
            return (T) user.getRole().getId();
        }
        throw new DaoException("Field not assigned to column");
    }
}
