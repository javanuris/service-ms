package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.factory.AbstractDaoFactory;
import com.epam.java.rt.lab.dao.factory.DaoFactory;
import com.epam.java.rt.lab.dao.sql.OrderBy;
import com.epam.java.rt.lab.dao.sql.Sql;
import com.epam.java.rt.lab.dao.sql.Where;
import com.epam.java.rt.lab.entity.business.Category;
import com.epam.java.rt.lab.service.ServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.Assert.*;

/**
 * category-ms
 */
public class CategoryDaoTest {

    private static final String CATEGORY_READ_A = "SELECT \"Category\".id, \"Category\".created, \"Category\".parent_id, \"Category\".name FROM \"Category\" WHERE \"Category\".id = ?";
    private static final String CATEGORY_READ_B = "SELECT \"Category\".id, \"Category\".created, \"Category\".parent_id, \"Category\".name FROM \"Category\" ORDER BY \"Category\".name ASC";
    private static final String CATEGORY_READ_C = "SELECT \"Category\".id, \"Category\".created, \"Category\".parent_id, \"Category\".name FROM \"Category\" LIMIT 0, 10";

    private DaoFactory daoFactory;
    private DaoParameter daoParameter;
    private Dao dao;

    @Before
    public void setUp() throws Exception {
        try {
            this.daoFactory = AbstractDaoFactory.createDaoFactory();
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException("exception.service.base.dao", e.getCause());
        }
    }

    @After
    public void tearDown() throws Exception {
        this.daoParameter = null;
        this.daoFactory.close();
        this.daoFactory = null;
    }

    @Test
    public void getSqlCreate() throws Exception {

    }

    @Test
    public void getSqlRead() throws Exception {
        dao = daoFactory.createDao("Category");
        this.daoParameter = new DaoParameter()
                .setWherePredicate(
                        Where.Predicate.get(
                                Category.Property.ID,
                                Where.Predicate.PredicateOperator.EQUAL,
                                1L
                        )
                );
        assertEquals("getSqlRead(WHERE) failed", CATEGORY_READ_A, ((CategoryDao) dao).getSqlRead(this.daoParameter).create());
        this.daoParameter = new DaoParameter()
                .setOrderByCriteriaArray(
                        OrderBy.Criteria.asc(Category.Property.NAME)
                );
        assertEquals("getSqlRead(ORDER) failed", CATEGORY_READ_B, ((CategoryDao) dao).getSqlRead(this.daoParameter).create());
        this.daoParameter = new DaoParameter().setLimit(0L, 10L);
        assertEquals("getSqlRead(LIMIT) failed", CATEGORY_READ_C, ((CategoryDao) dao).getSqlRead(this.daoParameter).create());
    }

    @Test
    public void getSqlUpdate() throws Exception {

    }

    @Test
    public void getSqlDelete() throws Exception {

    }

    @Test
    public void getSqlCount() throws Exception {

    }

    @Test
    public void getEntity() throws Exception {
        dao = daoFactory.createDao("Category");
        this.daoParameter = new DaoParameter()
                .setWherePredicate(
                        Where.Predicate.get(
                                Category.Property.ID,
                                Where.Predicate.PredicateOperator.EQUAL,
                                1L
                        )
                );
        Sql sql = ((CategoryDao) dao).getSqlRead(this.daoParameter);
        PreparedStatement statement = ((CategoryDao) dao).getConnection().prepareStatement(sql.create());
        statement.setLong(1, (Long) sql.getWildValueList().get(0).getVal());
        ResultSet resultSet = statement.executeQuery();
        List<Category> categoryList = ((CategoryDao) dao).getEntity(resultSet, sql);
        Category category = categoryList.get(0);
        assertNotNull("getEntity() failed", category);
        assertNotNull("getEntity(created) failed", category.getCreated());
        assertNotNull("getEntity(name) failed", category.getName());
    }

}