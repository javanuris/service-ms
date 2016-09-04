package com.epam.java.rt.lab.dao.h2;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.rbac.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * service-ms
 */
public class H2JdbcRoleDao extends H2JdbcDao implements Dao {
    private static final Logger logger = LoggerFactory.getLogger(H2JdbcRoleDao.class);

    public H2JdbcRoleDao() throws DaoException {
        super.resetProperties();
    }

    @Override
    String getEntityTableName() {
        return "Role";
    }

    @Override
    Object getEntity(ResultSet resultSet) throws SQLException {
        Role role = new Role();
        role.setId(resultSet.getLong("id"));
        role.setName(resultSet.getString("name"));
        return role;
    }
}
