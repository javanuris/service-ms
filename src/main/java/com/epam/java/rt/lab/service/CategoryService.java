package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.OrderBy;
import com.epam.java.rt.lab.dao.sql.Update;
import com.epam.java.rt.lab.dao.sql.Where;
import com.epam.java.rt.lab.entity.business.Category;
import com.epam.java.rt.lab.web.component.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * category-ms
 */
public class CategoryService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    public CategoryService()
            throws ServiceException {
    }

    public List<Category> getCategoryList(Page page) throws ServiceException {
        try {
            DaoParameter daoParameter = new DaoParameter();
            page.setCountItems(dao(Category.class.getSimpleName()).count(daoParameter));
            return dao(Category.class.getSimpleName()).read(new DaoParameter()
                    .setOrderByCriteriaArray(OrderBy.Criteria.asc(
                            Category.Property.NAME
                    ))
                    .setLimit(
                            (page.getCurrentPage() - 1) * page.getItemsOnPage(),
                            page.getItemsOnPage()
                    )
            );
        } catch (DaoException e) {
            throw new ServiceException("exception.service.category.get-category-list.dao", e.getCause());
        }
    }

    public List<Category> getCategoryList() throws ServiceException {
        try {
            DaoParameter daoParameter = new DaoParameter();
            return dao(Category.class.getSimpleName()).read(new DaoParameter()
                    .setOrderByCriteriaArray(OrderBy.Criteria.asc(
                            Category.Property.NAME
                    ))
            );
        } catch (DaoException e) {
            throw new ServiceException("exception.service.category.get-category-list.dao", e.getCause());
        }
    }

    public Category getCategory(Long id) throws ServiceException {
        try {
            List<Category> categoryList = dao(Category.class.getSimpleName()).read(new DaoParameter()
                    .setWherePredicate(Where.Predicate.get(
                            Category.Property.ID,
                            Where.Predicate.PredicateOperator.EQUAL,
                            id
                    ))
            );
            return categoryList != null && categoryList.size() > 0 ? categoryList.get(0) : null;
        } catch (DaoException e) {
            throw new ServiceException("exception.service.category.get-user.dao", e.getCause());
        }
    }

    public int updateCategory(Category category) throws ServiceException {
        try {
            return dao(Category.class.getSimpleName()).update(new DaoParameter()
                    .setSetValueArray(
                            new Update.SetValue(Category.Property.PARENT_ID,
                                    category.getParent() == null ? null : category.getParent().getId()),
                            new Update.SetValue(Category.Property.NAME, category.getName())
                    )
                    .setWherePredicate(Where.Predicate.get(
                            Category.Property.ID,
                            Where.Predicate.PredicateOperator.EQUAL,
                            category.getId()
                    ))
            );
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException("exception.service.category.update-category.dao", e.getCause());
        }
    }

    public Long addCategory(Category category) throws ServiceException {
        try {
            return dao(Category.class.getSimpleName()).create(new DaoParameter().setEntity(category));
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException("exception.service.category.update-category.dao", e.getCause());
        }
    }


}
