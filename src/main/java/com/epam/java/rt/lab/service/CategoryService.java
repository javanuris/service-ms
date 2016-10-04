package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.OrderBy;
import com.epam.java.rt.lab.entity.business.Category;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.web.component.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * service-ms
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
            return dao(User.class.getSimpleName()).read(new DaoParameter()
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

}
