package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.OrderBy.Criteria;
import com.epam.java.rt.lab.dao.sql.Where.Predicate;
import com.epam.java.rt.lab.dao.sql.WherePredicateOperator;
import com.epam.java.rt.lab.entity.access.User;
import com.epam.java.rt.lab.entity.access.UserProperty;
import com.epam.java.rt.lab.entity.business.Application;
import com.epam.java.rt.lab.entity.business.ApplicationProperty;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.TimestampManager;
import com.epam.java.rt.lab.web.component.FormControlValue;
import com.epam.java.rt.lab.web.component.Page;
import com.epam.java.rt.lab.web.component.SelectValue;
import com.epam.java.rt.lab.web.validator.Validator;
import com.epam.java.rt.lab.web.validator.ValidatorFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.epam.java.rt.lab.entity.business.Application.NULL_APPLICATION;
import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;
import static com.epam.java.rt.lab.util.PropertyManager.AUTHORIZED;
import static com.epam.java.rt.lab.web.validator.ValidatorFactory.DIGITS;

public class ApplicationService extends BaseService {

    public List<Application> getApplicationList(Page page, User user)
            throws AppException {
        if (page == null || user == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        DaoParameter daoParameter = new DaoParameter();
        page.setCountItems(dao(Application.class.getSimpleName()).
                count(daoParameter));
        daoParameter = new DaoParameter();
        daoParameter.setOrderByCriteriaArray(Criteria.
                desc(ApplicationProperty.CREATED));
        daoParameter.setLimit((page.getCurrentPage() - 1)
                * page.getItemsOnPage(), page.getItemsOnPage());
        if (AUTHORIZED.equals(user.getRole().getName())) {
            daoParameter.setWherePredicate(Predicate.
                    get(UserProperty.ID, WherePredicateOperator.EQUAL,
                            user.getId()));
        }
        return dao(Application.class.getSimpleName()).read(daoParameter);
    }

    public Application getApplication(Long id, User user) throws AppException {
        if (id == null || user == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        DaoParameter daoParameter = new DaoParameter();
        Predicate applicationPredicate = Predicate.
                get(ApplicationProperty.ID,
                        WherePredicateOperator.EQUAL, id);
        if (!AUTHORIZED.equals(user.getRole().getName())) {
            daoParameter.setWherePredicate(applicationPredicate );
        } else {
            daoParameter.setWherePredicate(Predicate.
                    get(applicationPredicate,
                            WherePredicateOperator.AND, Predicate.
                                    get(UserProperty.ID,
                                            WherePredicateOperator.EQUAL,
                                            user.getId())));
        }
        List<Application> applicationList =
                dao(Application.class.getSimpleName()).read(daoParameter);
        return ((applicationList != null) && (applicationList.size() > 0))
                ? applicationList.get(0) : NULL_APPLICATION;
    }

    public Long addApplication(User user,
                               FormControlValue parentCategoryValue,
                               FormControlValue messageValue)
            throws AppException {
        if (user == null || parentCategoryValue == null
                || messageValue == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        if (!AUTHORIZED.equals(user.getRole().getName())) return null;
        boolean formValid = true;
        String[] validationMessageArray;
        if (messageValue.getValue().length() == 0) {
            validationMessageArray = new String[]
                    {"message.application.empty-message"};
            messageValue.setValidationMessageList(new ArrayList<>
                    (Arrays.asList(validationMessageArray)));
            formValid = false;
        }
        if (parentCategoryValue.getValue().length() > 0) {
            Validator digitValidator =
                    ValidatorFactory.getInstance().create(DIGITS);
            validationMessageArray = digitValidator.
                    validate(parentCategoryValue.getValue());
            if (validationMessageArray.length > 0) {
                parentCategoryValue.setValidationMessageList(new ArrayList<>
                        (Arrays.asList(validationMessageArray)));
                formValid = false;
            } else if (formValid) {
                formValid = false;
                for (SelectValue selectValue : parentCategoryValue.
                        getAvailableValueList()) {
                    if (parentCategoryValue.getValue().
                            equals(selectValue.getValue())) {
                        formValid = true;
                        break;
                    }
                }
                if (!formValid) {
                    validationMessageArray = new String[]
                            {"message.category.parent-not-exist"};
                    parentCategoryValue.setValidationMessageList(new ArrayList<>
                            (Arrays.asList(validationMessageArray)));
                }
            }
        } else {
            formValid = false;
        }
        if (!formValid) return null;
        Application application = new Application();
        application.setCreated(TimestampManager.getCurrentTimestamp());
        application.setUser(user);
        application.setCategory(new CategoryService().
                getCategory(Long.valueOf(parentCategoryValue.getValue())));
        application.setMessage(messageValue.getValue());
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setEntity(application);
        return dao(Application.class.getSimpleName()).create(daoParameter);
    }

}
