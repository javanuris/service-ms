package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.factory.AbstractDaoFactory;
import com.epam.java.rt.lab.dao.factory.DaoFactory;
import com.epam.java.rt.lab.dao.sql.Where;
import com.epam.java.rt.lab.entity.access.Login;
import com.epam.java.rt.lab.service.ServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * category-ms
 */
public class JdbcDaoTest {

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
    public void create() throws Exception {

    }

    @Test
    public void read() throws Exception {
        Dao dao = this.daoFactory.createDao("Login");
        List<Login> loginList = dao.read(new DaoParameter()
                .setWherePredicate(Where.Predicate.get(
                        Login.Property.EMAIL,
                        Where.Predicate.PredicateOperator.EQUAL,
                        "test@test.com"
                )
        ));
        assertNotNull("read() failed", loginList);
        Login login = loginList.get(0);
        assertEquals("read(login) failed", "test@test.com", login.getEmail());
    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void delete() throws Exception {

    }

}