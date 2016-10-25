package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.OrderBy.Criteria;
import com.epam.java.rt.lab.dao.sql.Update.SetValue;
import com.epam.java.rt.lab.dao.sql.Where.Predicate;
import com.epam.java.rt.lab.entity.business.Category;
import com.epam.java.rt.lab.entity.business.Category.Property;
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

import static com.epam.java.rt.lab.entity.business.Category.NULL_CATEGORY;
import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;
import static com.epam.java.rt.lab.web.validator.ValidatorFactory.DIGITS;
import static com.epam.java.rt.lab.web.validator.ValidatorFactory.NAME;

public class CategoryService extends BaseService {

    public List<Category> getCategoryList(Page page) throws AppException {
        if (page == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        DaoParameter daoParameter = new DaoParameter();
        page.setCountItems(dao(Category.class.getSimpleName()).
                count(daoParameter));
        daoParameter = new DaoParameter();
        daoParameter.setOrderByCriteriaArray(Criteria.asc(Property.NAME));
        daoParameter.setLimit((page.getCurrentPage() - 1)
                * page.getItemsOnPage(), page.getItemsOnPage());
        return dao(Category.class.getSimpleName()).read(daoParameter);
    }

    public List<Category> getCategoryList() throws AppException {
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setOrderByCriteriaArray(Criteria.asc(Property.NAME));
        return dao(Category.class.getSimpleName()).read(daoParameter);
    }

    public Category getCategory(Long id) throws AppException {
        if (id == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setWherePredicate(Predicate.
                get(Property.ID, Predicate.PredicateOperator.EQUAL, id));
        List<Category> categoryList = dao(Category.class.getSimpleName()).
                read(daoParameter);
        return ((categoryList != null) && (categoryList.size() > 0))
                ? categoryList.get(0) : NULL_CATEGORY;
    }

    public boolean updateCategory(Long id,
                                  FormControlValue parentCategoryValue,
                                  FormControlValue nameValue)
            throws AppException {
        if (id == null || parentCategoryValue == null || nameValue == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        boolean formValid = true;
        String[] validationMessageArray;
        if (nameValue.getValue().length() == 0) {
            validationMessageArray = new String[]
                    {"message.category.empty-name"};
            nameValue.setValidationMessageList(new ArrayList<>
                    (Arrays.asList(validationMessageArray)));
            formValid = false;
        }
        Long parentId = null;
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
                        parentId = Long.
                                valueOf(parentCategoryValue.getValue());
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
        }
        if (!formValid) return false;
        Category category = getCategory(id);
        if (parentId != null) category.setParentId(parentId);
        category.setName(nameValue.getValue());
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setSetValueArray(
                new SetValue(Property.PARENT_ID, category.getParentId()),
                new SetValue(Property.NAME, category.getName()));
        daoParameter.setWherePredicate(Predicate.
                get(Property.ID, Predicate.PredicateOperator.EQUAL,
                        category.getId()));
        return (dao(Category.class.getSimpleName()).
                update(daoParameter) > 0);
    }

    public Long addCategory(FormControlValue parentCategoryValue,
                            FormControlValue nameValue)
            throws AppException {
        if (parentCategoryValue == null || nameValue == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        boolean formValid = true;
        String[] validationMessageArray;
        if (nameValue.getValue().length() == 0) {
            validationMessageArray = new String[]
                    {"message.category.empty-name"};
            nameValue.setValidationMessageList(new ArrayList<>
                    (Arrays.asList(validationMessageArray)));
            formValid = false;
        }
        Long parentId = null;
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
                        parentId = Long.
                                valueOf(parentCategoryValue.getValue());
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
        }
        if (!formValid) return null;
        Category category = new Category();
        category.setCreated(TimestampManager.getCurrentTimestamp());
        if (parentId != null) category.setParentId(parentId);
        category.setName(nameValue.getValue());
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setEntity(category);
        return dao(Category.class.getSimpleName()).create(daoParameter);
    }

}