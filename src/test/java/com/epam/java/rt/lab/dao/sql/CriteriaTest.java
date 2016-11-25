package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.DaoStatement;
import com.epam.java.rt.lab.dao.factory.AbstractDaoFactory;
import com.epam.java.rt.lab.dao.h2.jdbc.JdbcDao;
import com.epam.java.rt.lab.entity.access.LoginProperty;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.PropertyManager;
import com.epam.java.rt.lab.util.TimestampManager;
import com.epam.java.rt.lab.web.validator.ValidatorFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CriteriaTest {

    private static final String LOGIN_ID = "\"Login\".id";

    private OrderBy.Criteria criteria;

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
        this.criteria = new OrderBy.Criteria(Column.from(LOGIN_ID), false);
        assertNotNull("Instantiating failed", this.criteria);
        assertEquals("toString() failed", LOGIN_ID.concat(" ASC"),
                this.criteria.appendClause(new StringBuilder()).toString());
    }

    @After
    public void tearDown() throws Exception {
        this.criteria = null;
    }

    @Test
    public void asc() throws Exception {
        this.criteria = OrderBy.Criteria.asc(LoginProperty.ID);
        assertNotNull("Instantiating failed", this.criteria);
        assertEquals("asc() failed", LOGIN_ID.concat(" ASC"),
                this.criteria.appendClause(new StringBuilder()).toString());
    }

    @Test
    public void desc() throws Exception {
        this.criteria = OrderBy.Criteria.desc(LoginProperty.ID);
        assertNotNull("Instantiating failed", this.criteria);
        assertEquals("desc() failed", LOGIN_ID.concat(" DESC"),
                this.criteria.appendClause(new StringBuilder()).toString());
    }

}