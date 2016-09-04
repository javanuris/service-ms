package com.epam.java.rt.lab.dao.h2;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.rbac.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * service-ms
 */
public class H2JdbcPermissionDao extends H2JdbcDao implements Dao {
    private static final Logger logger = LoggerFactory.getLogger(H2JdbcPermissionDao.class);

    public H2JdbcPermissionDao() throws DaoException {
        super.resetProperties();
    }

    @Override
    String getEntityTableName() {
        return "Permission";
    }

    @Override
    Object getEntity(ResultSet resultSet) throws SQLException {
        Permission permission = new Permission();
        permission.setId(resultSet.getLong("id"));
        permission.setUri(resultSet.getString("uri"));
        return permission;
    }
}
