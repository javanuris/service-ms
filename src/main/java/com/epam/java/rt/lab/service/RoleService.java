package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.Dao_;
import com.epam.java.rt.lab.dao.factory.AbstractDaoFactory;
import com.epam.java.rt.lab.entity.rbac.Login;
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

    public RoleService() throws ConnectionException, DaoException {
    }

    Role getRole(Long id) throws DaoException {
        Role role = new Role();
        role.setId(id);
        Dao_ dao = super.daoFactory.createDao("Role");
        role = dao.getFirst(role, "id");
        daoFactory.close();
        return role;
    }


//    Role getRole(Long id, Connection connection) throws DaoException, SQLException, ConnectionException {
//        logger.debug("getRole");
//        Dao roleDao = null;
//        try {
//            roleDao = super.getJdbcDao(connection);
//            logger.debug("roleDao = {}", roleDao.getClass().getSimpleName());
//            Role role = roleDao.query("*").filter("id", id).first();
//            if (role == null) return null;
//            PermissionService permissionService = new PermissionService();
//            role.setUriList(permissionService.getUriList(permissionService.getPermissionList(role.getId(), connection)));
//            return role;
//        } finally {
//            if (roleDao != null) roleDao.close();
//        }
//    }
//
//    public Role getRole(Long id) throws DaoException, SQLException, ConnectionException {
//        logger.debug("getRole({})", id);
//        Connection connection = null;
//        try {
//            connection = AbstractDaoFactory.createDaoFactory().getConnection();
//            return getRole(id, connection);
//        } finally {
//            if (connection != null) AbstractDaoFactory.createDaoFactory().releaseConnection(connection);
//        }
//    }

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
