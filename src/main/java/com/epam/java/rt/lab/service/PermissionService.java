package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.entity.rbac.Permission;
import com.epam.java.rt.lab.entity.rbac.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * service-ms
 */
public class PermissionService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(PermissionService.class);

    public PermissionService() throws ServiceException {
    }

    public List<Permission> getPermissionList()
            throws ServiceException {
        try {
            return dao(Permission.class.getSimpleName()).read(new DaoParameter());
        } catch (DaoException e) {
            throw new ServiceException("exception.service.permission.get-permission-list.dao", e.getCause());
        }
    }

}
