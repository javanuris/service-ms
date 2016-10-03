package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.Where;
import com.epam.java.rt.lab.entity.rbac.Role;
import com.epam.java.rt.lab.util.GlobalProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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
            List<Role> roleList = dao(Role.class.getSimpleName()).read(new DaoParameter()
                    .setWherePredicate(Where.Predicate.get(
                            Role.Property.ID,
                            Where.Predicate.PredicateOperator.EQUAL,
                            id
                    ))
            );
            return roleList != null && roleList.size() > 0 ? roleList.get(0) : null;
        } catch (DaoException e) {
            throw new ServiceException("exception.service.role.get-role.dao", e.getCause());
        }
    }

    public Role getRoleAuthorized()
            throws ServiceException {
        try {
            List<Role> roleList = dao(Role.class.getSimpleName()).read(new DaoParameter()
                    .setWherePredicate(Where.Predicate.get(
                            Role.Property.NAME,
                            Where.Predicate.PredicateOperator.EQUAL,
                            GlobalProperties.getProperty("role.authorized")
                    ))
            );
            return roleList != null && roleList.size() > 0 ? roleList.get(0) : null;
        } catch (DaoException e) {
            throw new ServiceException("exception.service.role.get-role-authorized.dao", e.getCause());
        }
    }

    public List<Role> getRoleList()
            throws ServiceException {
        try {
            return dao(Role.class.getSimpleName()).read(new DaoParameter());
        } catch (DaoException e) {
            throw new ServiceException("exception.service.role.get-role-list.dao", e.getCause());
        }
    }

}
