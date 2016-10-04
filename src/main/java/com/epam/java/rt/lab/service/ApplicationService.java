package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.OrderBy;
import com.epam.java.rt.lab.dao.sql.Update;
import com.epam.java.rt.lab.dao.sql.Where;
import com.epam.java.rt.lab.entity.business.Application;
import com.epam.java.rt.lab.entity.rbac.Avatar;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.Remember;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.util.*;
import com.epam.java.rt.lab.util.validator.ValidatorException;
import com.epam.java.rt.lab.util.validator.ValidatorFactory;
import com.epam.java.rt.lab.web.access.RoleException;
import com.epam.java.rt.lab.web.access.RoleFactory;
import com.epam.java.rt.lab.web.component.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.List;
import java.util.UUID;

/**
 * category-ms
 */
public class ApplicationService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationService.class);

    public ApplicationService()
            throws ServiceException {
    }

    public List<Application> getApplicationList(Page page, User user) throws ServiceException {
        try {
            DaoParameter daoParameter = new DaoParameter();
            page.setCountItems(dao(Application.class.getSimpleName()).count(daoParameter));
            daoParameter = new DaoParameter()
                    .setOrderByCriteriaArray(OrderBy.Criteria.desc(
                            Application.Property.CREATED
                    ))
                    .setLimit(
                            (page.getCurrentPage() - 1) * page.getItemsOnPage(),
                            page.getItemsOnPage()
                    );
            if (user != null) {
                daoParameter.setWherePredicate(Where.Predicate.get(
                        User.Property.ID,
                        Where.Predicate.PredicateOperator.EQUAL,
                        user.getId()
                ));
            }
            return dao(Application.class.getSimpleName()).read(daoParameter);
        } catch (DaoException e) {
            throw new ServiceException("exception.service.application.get-application-list.dao", e.getCause());
        }
    }

    public Application getApplication(Long id) throws ServiceException {
        try {
            List<Application> applicationList = dao(Application.class.getSimpleName()).read(new DaoParameter()
                    .setWherePredicate(Where.Predicate.get(
                            Application.Property.ID,
                            Where.Predicate.PredicateOperator.EQUAL,
                            id
                    ))
            );
            return applicationList != null && applicationList.size() > 0 ? applicationList.get(0) : null;
        } catch (DaoException e) {
            throw new ServiceException("exception.service.application.get-application.dao", e.getCause());
        }
    }

    public Long addApplication(Application application) throws ServiceException {
        try {
            return dao(Application.class.getSimpleName()).create(new DaoParameter().setEntity(application));
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException("exception.service.application.add-application.dao", e.getCause());
        }
    }

}
