package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.Parameter;
import com.epam.java.rt.lab.dao.types.OrderType;
import com.epam.java.rt.lab.entity.EntityProperty;
import com.epam.java.rt.lab.entity.rbac.Role;
import com.epam.java.rt.lab.util.GlobalProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * service-ms
 */
public class RoleService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    public RoleService()
            throws ServiceException {
    }

    public Role getRole(Long id)
            throws ServiceException {
        try {
            return dao("Role").getFirst(new Parameter()
                    .filter(Parameter.Field.set(
                            Role.Property.ID, id
                    ))
            );
        } catch (DaoException e) {
            throw new ServiceException("exception.service.role.get-role.dao", e.getCause());
        }
    }

    public Role getRoleAuthorized()
            throws ServiceException {
        try {
            return dao("Role").getFirst(new Parameter()
                    .filter(Parameter.Field.set(
                            Role.Property.NAME,
                            GlobalProperties.getProperty("role.authorized")
                    ))
            );
        } catch (DaoException e) {
            throw new ServiceException("exception.service.role.get-role-authorized.dao", e.getCause());
        }
    }

    public List<Role> getRoleList(OrderType orderType, EntityProperty... orderBy)
            throws ServiceException {
        try {
            if (orderType == null || orderBy.length == 0) {
                return dao("Role").getAll(new Parameter());
            } else {
                return dao("Role").getAll(new Parameter()
                        .order(orderType, orderBy)
                );
            }
        } catch (DaoException e) {
            throw new ServiceException("exception.service.role.get-role-list.dao", e.getCause());
        }
    }

}
