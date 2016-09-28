package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.factory.AbstractDaoFactory;
import com.epam.java.rt.lab.dao.factory.DaoFactory;
import com.epam.java.rt.lab.dao.sql.Where;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.service.ServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * service-ms
 */
public class LoginJdbcDaoTest {

    private static final String LOGIN_READ = "SELECT \"Login\".id, \"Login\".email, \"Login\".password, \"Login\".attempt_left, \"Login\".status FROM \"Login\" WHERE \"Login\".id = ?";

    private DaoFactory daoFactory;
    private DaoParameter daoParameter;

    @Before
    public void setUp() throws Exception {
        try {
            this.daoFactory = AbstractDaoFactory.createDaoFactory();
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException("exception.service.base.dao", e.getCause());
        }
        this.daoParameter = new DaoParameter()
                .setWherePredicate(
                        Where.Predicate.get(
                                Login.Property.ID,
                                Where.Predicate.PredicateOperator.EQUAL,
                                1
                        )
                );
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
        LoginDao loginDao = (LoginDao) daoFactory.createDao("Login");
        assertEquals("getSqlRead() failed", LOGIN_READ, loginDao.getSqlRead(this.daoParameter).create());
    }

    @Test
    public void getSqlUpdate() throws Exception {

    }

    @Test
    public void getSqlDelete() throws Exception {

    }

    @Test
    public void getEntity() throws Exception {

    }

}