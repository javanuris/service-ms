package com.epam.java.rt.lab.dao.h2;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
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

}
