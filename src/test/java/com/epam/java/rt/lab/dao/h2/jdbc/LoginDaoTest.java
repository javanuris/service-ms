package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.DaoStatement;
import com.epam.java.rt.lab.dao.factory.AbstractDaoFactory;
import com.epam.java.rt.lab.dao.factory.DaoFactory;
import com.epam.java.rt.lab.dao.sql.OrderBy;
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LoginDaoTest {

    private static final String LOGIN_READ_A =
            "SELECT \"Login\".id, \"Login\".email, \"Login\".salt,"
                    + " \"Login\".password, \"Login\".attempt_left,"
                    + " \"Login\".status FROM \"Login\" WHERE \"Login\".id = ?";
    private static final String LOGIN_READ_B =
            "SELECT \"Login\".id, \"Login\".email, \"Login\".salt,"
                    + " \"Login\".password, \"Login\".attempt_left,"
                    + " \"Login\".status FROM \"Login\" ORDER BY \"Login\".email"
                    + " ASC";
    private static final String LOGIN_READ_C =
            "SELECT \"Login\".id, \"Login\".email, \"Login\".salt,"
                    + " \"Login\".password, \"Login\".attempt_left,"
                    + " \"Login\".status FROM \"Login\" LIMIT 0, 10";

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
        dao = daoFactory.createDao("Login");
        this.daoParameter = new DaoParameter();
        this.daoParameter.setWherePredicate(Predicate.
                get(Property.ID, Predicate.PredicateOperator.EQUAL, 1));
        assertEquals("getSqlRead(WHERE) failed", LOGIN_READ_A,
                ((LoginDao) dao).getSqlRead(this.daoParameter).create());
        this.daoParameter = new DaoParameter();
        this.daoParameter.
                setOrderByCriteriaArray(OrderBy.Criteria.asc(Property.EMAIL));
        assertEquals("getSqlRead(ORDER) failed", LOGIN_READ_B,
                ((LoginDao) dao).getSqlRead(this.daoParameter).create());
        this.daoParameter = new DaoParameter().setLimit(0L, 10L);
        assertEquals("getSqlRead(LIMIT) failed", LOGIN_READ_C,
                ((LoginDao) dao).getSqlRead(this.daoParameter).create());
    }

    @Test
    public void getEntity() throws Exception {
        dao = daoFactory.createDao("Login");
        this.daoParameter = new DaoParameter();
        this.daoParameter.setWherePredicate(Predicate.
                get(Property.ID, Predicate.PredicateOperator.EQUAL, 1L));
        Sql sql = ((LoginDao) dao).getSqlRead(this.daoParameter);
        PreparedStatement statement =
                ((LoginDao) dao).getConnection().prepareStatement(sql.create());
        statement.setLong(1, (Long) sql.getWildValueList().get(0).getVal());
        ResultSet resultSet = statement.executeQuery();
        List<Login> loginList = ((LoginDao) dao).getEntity(resultSet, sql);
        Login login = loginList.get(0);
        assertNotNull("getEntity() failed", login);
    }

}