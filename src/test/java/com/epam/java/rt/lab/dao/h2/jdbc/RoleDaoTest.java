package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.factory.AbstractDaoFactory;
import com.epam.java.rt.lab.dao.factory.DaoFactory;
import com.epam.java.rt.lab.dao.sql.OrderBy;
import com.epam.java.rt.lab.dao.sql.Where;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.Role;
import com.epam.java.rt.lab.service.ServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * service-ms
 */
public class RoleDaoTest {

    private static final String ROLE_READ_A = "SELECT \"Role\".id, \"Role\".name FROM \"Role\" WHERE \"Role\".id = ?";
    private static final String ROLE_READ_B = "SELECT \"Role\".id, \"Role\".name FROM \"Role\" ORDER BY \"Role\".name ASC";
    private static final String ROLE_READ_C = "SELECT \"Role\".id, \"Role\".name FROM \"Role\" LIMIT 0, 10";

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
        dao = daoFactory.createDao("Role");
        this.daoParameter = new DaoParameter()
                .setWherePredicate(
                        Where.Predicate.get(
                                Role.Property.ID,
                                Where.Predicate.PredicateOperator.EQUAL,
                                1
                        )
                );
        assertEquals("getSqlRead(WHERE) failed", ROLE_READ_A, ((RoleDao) dao).getSqlRead(this.daoParameter).create());
        this.daoParameter = new DaoParameter()
                .setOrderByCriteriaArray(
                        OrderBy.Criteria.asc(Role.Property.NAME)
                );
        assertEquals("getSqlRead(ORDER) failed", ROLE_READ_B, ((RoleDao) dao).getSqlRead(this.daoParameter).create());
        this.daoParameter = new DaoParameter().setLimit(0, 10);
        assertEquals("getSqlRead(LIMIT) failed", ROLE_READ_C, ((RoleDao) dao).getSqlRead(this.daoParameter).create());
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