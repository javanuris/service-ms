package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.OrderBy.Criteria;
import com.epam.java.rt.lab.dao.sql.Where.Predicate;
import com.epam.java.rt.lab.entity.access.User;
import com.epam.java.rt.lab.entity.business.Application;
import com.epam.java.rt.lab.entity.business.Application.Property;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.web.component.Page;

import java.util.List;

import static com.epam.java.rt.lab.entity.business.Application.NULL_APPLICATION;

public class ApplicationService extends BaseService {

    public List<Application> getApplicationList(Page page, User user)
            throws AppException {
        DaoParameter daoParameter = new DaoParameter();
        page.setCountItems(dao(Application.class.getSimpleName()).
                count(daoParameter));
        daoParameter = new DaoParameter();
        daoParameter.setOrderByCriteriaArray(Criteria.desc(Property.CREATED));
        daoParameter.setLimit((page.getCurrentPage() - 1)
                * page.getItemsOnPage(), page.getItemsOnPage());
        if (user != null) {
            daoParameter.setWherePredicate(Predicate.
                    get(Property.ID, Predicate.PredicateOperator.EQUAL,
                            user.getId()));
        }
        return dao(Application.class.getSimpleName()).read(daoParameter);
    }

    public Application getApplication(Long id) throws AppException {
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setWherePredicate(Predicate.
                get(Property.ID, Predicate.PredicateOperator.EQUAL, id));
        List<Application> applicationList =
                dao(Application.class.getSimpleName()).read(daoParameter);
        return ((applicationList != null) && (applicationList.size() > 0))
                ? applicationList.get(0) : NULL_APPLICATION;
    }

    public Long addApplication(Application application) throws AppException {
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setEntity(application);
        return dao(Application.class.getSimpleName()).create(daoParameter);
    }

}
