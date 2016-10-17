package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.factory.AbstractDaoFactory;
import com.epam.java.rt.lab.dao.factory.DaoFactory;
import com.epam.java.rt.lab.dao.sql.OrderBy;
import com.epam.java.rt.lab.dao.sql.Sql;
import com.epam.java.rt.lab.dao.sql.Where;
import com.epam.java.rt.lab.entity.access.Login;
import com.epam.java.rt.lab.service.ServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * category-ms
 */
public class LoginDaoTest {

    private static final String LOGIN_READ_A = "SELECT \"Login\".id, \"Login\".email, \"Login\".salt, \"Login\".password, \"Login\".attempt_left, \"Login\".status FROM \"Login\" WHERE \"Login\".id = ?";
    private static final String LOGIN_READ_B = "SELECT \"Login\".id, \"Login\".email, \"Login\".salt, \"Login\".password, \"Login\".attempt_left, \"Login\".status FROM \"Login\" ORDER BY \"Login\".email ASC";
    private static final String LOGIN_READ_C = "SELECT \"Login\".id, \"Login\".email, \"Login\".salt, \"Login\".password, \"Login\".attempt_left, \"Login\".status FROM \"Login\" LIMIT 0, 10";

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
        dao = daoFactory.createDao("Login");
        this.daoParameter = new DaoParameter()
                .setWherePredicate(
                        Where.Predicate.get(
                                Login.Property.ID,
                                Where.Predicate.PredicateOperator.EQUAL,
                                1
                        )
                );
        assertEquals("getSqlRead(WHERE) failed", LOGIN_READ_A, ((LoginDao) dao).getSqlRead(this.daoParameter).create());
        this.daoParameter = new DaoParameter()
                .setOrderByCriteriaArray(
                        OrderBy.Criteria.asc(Login.Property.EMAIL)
                );
        assertEquals("getSqlRead(ORDER) failed", LOGIN_READ_B, ((LoginDao) dao).getSqlRead(this.daoParameter).create());
        this.daoParameter = new DaoParameter().setLimit(0L, 10L);
        assertEquals("getSqlRead(LIMIT) failed", LOGIN_READ_C, ((LoginDao) dao).getSqlRead(this.daoParameter).create());
    }

    @Test
    public void getSqlUpdate() throws Exception {

    }

    @Test
    public void getSqlDelete() throws Exception {

    }

    @Test
    public void getEntity() throws Exception {
        dao = daoFactory.createDao("Login");
        this.daoParameter = new DaoParameter()
                .setWherePredicate(
                        Where.Predicate.get(
                                Login.Property.ID,
                                Where.Predicate.PredicateOperator.EQUAL,
                                1L
                        )
                );
        Sql sql = ((LoginDao) dao).getSqlRead(this.daoParameter);
        PreparedStatement statement = ((LoginDao) dao).getConnection().prepareStatement(sql.create());
        statement.setLong(1, (Long) sql.getWildValueList().get(0).getVal());
        ResultSet resultSet = statement.executeQuery();
        List<Login> loginList = ((LoginDao) dao).getEntity(resultSet, sql);
        Login login = loginList.get(0);
        assertNotNull("getEntity() failed", login);
    }

}