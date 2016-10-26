package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.DaoStatement;
import com.epam.java.rt.lab.dao.factory.AbstractDaoFactory;
import com.epam.java.rt.lab.dao.factory.DaoFactory;
import com.epam.java.rt.lab.dao.sql.Sql;
import com.epam.java.rt.lab.dao.sql.Where.Predicate;
import com.epam.java.rt.lab.entity.access.Login;
import com.epam.java.rt.lab.entity.access.Login.Property;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.PropertyManager;
import com.epam.java.rt.lab.util.TimestampManager;
import com.epam.java.rt.lab.web.validator.ValidatorFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JdbcDaoTest {

    private DaoFactory daoFactory;
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
        if (this.daoFactory != null) this.daoFactory.close();
        this.daoFactory = null;
    }

    @Test
    public void read() throws Exception {
        Dao dao = this.daoFactory.createDao("Login");
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setWherePredicate(Predicate.
                get(Property.EMAIL, Predicate.PredicateOperator.EQUAL,
                        "admin@test.com"));
        List<Login> loginList = dao.read(daoParameter);
        assertNotNull("read() failed", loginList);
        Login login = loginList.get(0);
        assertEquals("read(login) failed", "admin@test.com", login.getEmail());
    }

}