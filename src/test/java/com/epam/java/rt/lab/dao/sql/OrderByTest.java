package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.DaoStatement;
import com.epam.java.rt.lab.dao.factory.AbstractDaoFactory;
import com.epam.java.rt.lab.dao.h2.jdbc.JdbcDao;
import com.epam.java.rt.lab.entity.access.Login;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.PropertyManager;
import com.epam.java.rt.lab.util.TimestampManager;
import com.epam.java.rt.lab.web.validator.ValidatorFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrderByTest {

    private static final String ORDER_BY =
            " ORDER BY \"Login\".id ASC, \"Login\".email DESC";

    private OrderBy orderBy;

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
        OrderBy.Criteria[] criteriaArray =
                {OrderBy.Criteria.asc(Login.Property.ID),
                        OrderBy.Criteria.desc(Login.Property.EMAIL)};
        this.orderBy = new OrderBy(criteriaArray);
        assertNotNull("Instantiating failed", this.orderBy);
    }

    @After
    public void tearDown() throws Exception {
        this.orderBy = null;
    }

    @Test
    public void toStringTest() throws Exception {
        assertEquals("toString() failed", ORDER_BY,
                this.orderBy.appendClause(new StringBuilder()).toString());
    }

}