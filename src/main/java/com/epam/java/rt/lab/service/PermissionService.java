package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.Dao_;
import com.epam.java.rt.lab.dao.factory.AbstractDaoFactory;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.Permission;
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
public class PermissionService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(PermissionService.class);

    public PermissionService() throws ConnectionException, DaoException {
    }

    List<Permission> getPermissionList(Role role) throws DaoException {
        List<Permission> permissionList = new ArrayList<>();

        permission

        User user = new User();
        user.setLogin(login);
        Dao_ dao = super.daoFactory.createDao("User");
        user = dao.getFirst(user, "login_id");
        daoFactory.close();
        return user;
    }


    List<Permission> getPermissionList(Long roleId, Connection connection) throws DaoException, SQLException, ConnectionException {
        logger.debug("getPermissionList");
        Dao permissionDao = null;
        try {
            permissionDao = super.getJdbcDao(connection);
            logger.debug("permissionDao = {}", permissionDao.getClass().getSimpleName());
            return permissionDao.query("\"Permission\".id", "\"Permission\".uri")
                    .join("RolePermission").on("\"RolePermission\".permission_id", "\"Permission\".id")
                    .join("Role").on("\"Role\".id", "\"RolePermission\".role_id")
                    .filter("\"Role\".id", roleId).all();
        } finally {
            if (permissionDao != null) permissionDao.close();
        }
    }

    public List<Permission> getPermissionList(Long roleId) throws DaoException, SQLException, ConnectionException {
        logger.debug("getPermissionList");
        Connection connection = null;
        try {
            connection = AbstractDaoFactory.createDaoFactory().getConnection();
            return getPermissionList(roleId, connection);
        } finally {
            if (connection != null) AbstractDaoFactory.createDaoFactory().releaseConnection(connection);
        }
    }

    public List<String> getUriList(List<Permission> permissionList) {
        List<String> uriList = new ArrayList<>();
        for (Permission permission : permissionList)
            uriList.add(permission.getUri());
        return uriList;
    }

    public static List<String> getPermissionUriList(Role role) {
        List<Permission> permissionList = new ArrayList<>();
        permissionList.add(new Permission(1L, "/"));
        permissionList.add(new Permission(2L, "/home"));
        permissionList.add(new Permission(3L, "/profile/view"));
        permissionList.add(new Permission(4L, "/profile/logout"));
        permissionList.add(new Permission(5L, "/application/list"));
        permissionList.add(new Permission(6L, "/execution/list"));
        permissionList.add(new Permission(7L, "/employee/list"));
        permissionList.add(new Permission(8L, "/service/list"));
        List<String> uriList = new ArrayList<>();
        for (Permission permission : permissionList)
            uriList.add(permission.getUri());
        return uriList;
    }

    @Deprecated
    public static List<String> getAnonymous() {
        List<Permission> permissionList = new ArrayList<>();
        permissionList.add(new Permission(1L, "/"));
        permissionList.add(new Permission(2L, "/home"));
        permissionList.add(new Permission(3L, "/profile/login"));
        List<String> uriList = new ArrayList<>();
        for (Permission permission : permissionList)
            uriList.add(permission.getUri());
        return uriList;
    }

}
