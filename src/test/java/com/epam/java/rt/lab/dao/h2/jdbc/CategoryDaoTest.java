package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.DaoStatement;
import com.epam.java.rt.lab.dao.factory.AbstractDaoFactory;
import com.epam.java.rt.lab.dao.factory.DaoFactory;
import com.epam.java.rt.lab.dao.sql.OrderBy;
import com.epam.java.rt.lab.dao.sql.Sql;
import com.epam.java.rt.lab.dao.sql.Where.Predicate;
import com.epam.java.rt.lab.dao.sql.WherePredicateOperator;
import com.epam.java.rt.lab.entity.business.Category;
import com.epam.java.rt.lab.entity.business.CategoryProperty;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.PropertyManager;
import com.epam.java.rt.lab.util.TimestampManager;
import com.epam.java.rt.lab.web.validator.ValidatorFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CategoryDaoTest {

    private static final String CATEGORY_READ_A =
            "SELECT \"Category\".id, \"Category\".created,"
                    + " \"Category\".parent_id, \"Category\".name"
                    + " FROM \"Category\" WHERE \"Category\".id = ?";
    private static final String CATEGORY_READ_B =
            "SELECT \"Category\".id, \"Category\".created," +
                    " \"Category\".parent_id, \"Category\".name"
                    + " FROM \"Category\" ORDER BY \"Category\".name ASC";
    private static final String CATEGORY_READ_C =
            "SELECT \"Category\".id, \"Category\".created,"
                    + " \"Category\".parent_id, \"Category\".name"
                    + " FROM \"Category\" LIMIT 0, 10";

    private DaoFactory daoFactory;
    private DaoParameter daoParameter;
    private Dao dao;

    @Before
    public void setUp() throws Exception {
        PropertyManager.initGlobalProperties();
        AppException.initExceptionBundle();
        TimestampManager.initDateList();
        TimestampManager.initTimeList();
        ValidatorFactory.getInstance().initValidatorMap();
        AbstractDaoFactory.initDatabaseProperties();
        JdbcDao.initDaoProperties();
        Sql.initSqlProperties();
        DaoStatement.initStatementMethodMap();
        this.daoFactory = AbstractDaoFactory.createDaoFactory();
    }

    @After
    public void tearDown() throws Exception {
        this.daoParameter = null;
        if (this.daoFactory != null) this.daoFactory.close();
        this.daoFactory = null;
    }

    @Test
    public void getSqlRead() throws Exception {
        dao = daoFactory.createDao("Category");
        this.daoParameter = new DaoParameter();
        this.daoParameter.setWherePredicate(Predicate.
                get(CategoryProperty.ID,
                        WherePredicateOperator.EQUAL, 1L));
        assertEquals("getSqlRead(WHERE) failed", CATEGORY_READ_A,
                ((CategoryDao) dao).getSqlRead(this.daoParameter).create());
        this.daoParameter = new DaoParameter();
        this.daoParameter.
                setOrderByCriteriaArray(OrderBy.Criteria.
                        asc(CategoryProperty.NAME));
        assertEquals("getSqlRead(ORDER) failed", CATEGORY_READ_B,
                ((CategoryDao) dao).getSqlRead(this.daoParameter).create());
        this.daoParameter = new DaoParameter().setLimit(0L, 10L);
        assertEquals("getSqlRead(LIMIT) failed", CATEGORY_READ_C,
                ((CategoryDao) dao).getSqlRead(this.daoParameter).create());
    }

    @Test
    public void getEntity() throws Exception {
        dao = daoFactory.createDao("Category");
        this.daoParameter = new DaoParameter();
        this.daoParameter.setWherePredicate(Predicate.
                get(CategoryProperty.ID,
                        WherePredicateOperator.EQUAL, 1L));
        Sql sql = ((CategoryDao) dao).getSqlRead(this.daoParameter);
        PreparedStatement statement =
                ((CategoryDao) dao).getConnection().
                        prepareStatement(sql.create());
        statement.setLong(1, (Long) sql.getWildValueList().get(0).getVal());
        ResultSet resultSet = statement.executeQuery();
        List<Category> categoryList =
                ((CategoryDao) dao).getEntity(resultSet, sql);
        Category category = categoryList.get(0);
        assertNotNull("getEntity() failed", category);
        assertNotNull("getEntity(created) failed", category.getCreated());
        assertNotNull("getEntity(name) failed", category.getName());
    }

}