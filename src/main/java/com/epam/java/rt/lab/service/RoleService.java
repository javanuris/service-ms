package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.factory.DaoFactory;
import com.epam.java.rt.lab.entity.rbac.Role;
import com.epam.java.rt.lab.entity.rbac.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * service-ms
 */
public class RoleService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    public RoleService() throws DaoException {
        super();
    }

    public Role getRole(Long id) throws DaoException, SQLException, ConnectionException {
        logger.debug("getRole");
        Dao jdbcDao = null;
        Connection connection = null;
        try {
            jdbcDao = super.getJdbcDao();
            logger.debug("jdbcDao = {}", jdbcDao.getClass().getSimpleName());
            connection = DaoFactory.getDaoFactory().getConnection();
            Role role = jdbcDao.find(connection, "id", id).first();
            if (role == null) return null;
            return role;
        } finally {
            if (connection != null) DaoFactory.getDaoFactory().releaseConnection(connection);
            if (jdbcDao != null) jdbcDao.close();
        }
    }

    public Role getRole(User user) {


        Role role = new Role();
        role.setId(1L);
        role.setName("admin");
        role.setUriList(PermissionService.getPermissionUriList(role));
        return role;
    }

    @Deprecated
    public static Role getAnonymous() {
        return new Role(2L, "anonymous", PermissionService.getAnonymous());
    }

    public static List<Role> getRoleList() {
        List<Role> roleList = new ArrayList<>();
        Role role = new Role();
        role.setId(1L);
        role.setName("admin");
        role.setUriList(PermissionService.getPermissionUriList(role));
        roleList.add(role);
        return roleList;
    }

}
