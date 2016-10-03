package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.h2.jdbc.PermissionDao;
import com.epam.java.rt.lab.dao.sql.Where;
import com.epam.java.rt.lab.entity.EntityProperty;
import com.epam.java.rt.lab.entity.rbac.Permission;
import com.epam.java.rt.lab.entity.rbac.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            return dao(Permission.class.getSimpleName()).read(new DaoParameter()
                    .setPropertyArray(
                            Permission.Property.ID,
                            Permission.Property.URI,
                            Permission.Property.ACTION
                    )
            );
        } catch (DaoException e) {
            throw new ServiceException("exception.service.permission.get-permission-list.dao", e.getCause());
        }
    }

    public int deleteRolePermission(Role role)
            throws ServiceException {
        try {
            return dao(Permission.class.getSimpleName()).delete(new DaoParameter()
                    .setWherePredicate(Where.Predicate.get(
                            PermissionDao.RolePermissionProperty.ROLE_ID,
                            Where.Predicate.PredicateOperator.EQUAL,
                            role.getId()
                    ))
                    .setPropertyArray(PermissionDao.RolePermissionProperty.ID)
            );
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException("exception.service.permission.get-permission-list.dao", e.getCause());
        }
    }

    public int addRolePermission(Role role)
            throws ServiceException {
        try {
            long result = 0;
            Map<EntityProperty, Object> valueMap = new HashMap<>();
            for (Permission permission : getPermissionList()) {
                if (role.getUriList().contains(permission.getUri())) {
                    valueMap.put(PermissionDao.RolePermissionProperty.ROLE_ID, role.getId());
                    valueMap.put(PermissionDao.RolePermissionProperty.PERMISSION_ID, permission.getId());
                    System.out.println(permission.getUri());
                    result = result + dao(Permission.class.getSimpleName()).create(new DaoParameter()
                            .setValueMap(valueMap)
                            .setPropertyArray(PermissionDao.RolePermissionProperty.ID)
                    );
                }
            }
            return (int) result;
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException("exception.service.permission.get-permission-list.dao", e.getCause());
        }
    }

}
