package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.OrderBy.Criteria;
import com.epam.java.rt.lab.dao.sql.Update.SetValue;
import com.epam.java.rt.lab.dao.sql.Where.Predicate;
import com.epam.java.rt.lab.entity.business.Category;
import com.epam.java.rt.lab.entity.business.Category.Property;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.web.component.Page;

import java.util.List;

import static com.epam.java.rt.lab.entity.business.Category.NULL_CATEGORY;

public class CategoryService extends BaseService {

    public List<Category> getCategoryList(Page page) throws AppException {
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
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setWherePredicate(Predicate.
                get(Property.ID, Predicate.PredicateOperator.EQUAL, id));
        List<Category> categoryList = dao(Category.class.getSimpleName()).
                read(daoParameter);
        return ((categoryList != null) && (categoryList.size() > 0))
                ? categoryList.get(0) : NULL_CATEGORY;
    }

    public int updateCategory(Category category) throws AppException {
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setSetValueArray(
                new SetValue(Property.PARENT_ID, category.getParentId()),
                new SetValue(Property.NAME, category.getName()));
        daoParameter.setWherePredicate(Predicate.
                get(Property.ID, Predicate.PredicateOperator.EQUAL,
                        category.getId()));
        return dao(Category.class.getSimpleName()).update(daoParameter);
    }

    public Long addCategory(Category category) throws AppException {
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setEntity(category);
        return dao(Category.class.getSimpleName()).create(daoParameter);
    }

}
