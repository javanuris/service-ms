package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.factory.DaoFactory;
import com.epam.java.rt.lab.entity.rbac.Role;
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

    Role getRole(Long id, Connection connection) throws DaoException, SQLException, ConnectionException {
        logger.debug("getRole");
        Dao roleDao = null;
        try {
            roleDao = super.getJdbcDao(connection);
            logger.debug("roleDao = {}", roleDao.getClass().getSimpleName());
            Role role = roleDao.query("*").filter("id", id).first();
            if (role == null) return null;
            PermissionService permissionService = new PermissionService();
            role.setUriList(permissionService.getUriList(permissionService.getPermissionList(role.getId(), connection)));
            return role;
        } finally {
            if (roleDao != null) roleDao.close();
        }
    }

    public Role getRole(Long id) throws DaoException, SQLException, ConnectionException {
        logger.debug("getRole({})", id);
        Connection connection = null;
        try {
            connection = DaoFactory.getDaoFactory().getConnection();
            return getRole(id, connection);
        } finally {
            if (connection != null) DaoFactory.getDaoFactory().releaseConnection(connection);
        }
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
