package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.rbac.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        Dao dao = daoFactory.createDao("Role");
        role = dao.getFirst(role, "id", "");
        return role;
    }

    public List<Role> getRoleList() throws DaoException {
        Dao dao = daoFactory.createDao("Role");
        List<Role> roleList = dao.getAll("");
        return roleList;
    }

}
