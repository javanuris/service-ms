package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.factory.AbstractDaoFactory;
import com.epam.java.rt.lab.dao.factory.DaoFactory;
import com.epam.java.rt.lab.dao.sql.OrderBy;
import com.epam.java.rt.lab.dao.sql.Sql;
import com.epam.java.rt.lab.dao.sql.Where;
import com.epam.java.rt.lab.entity.rbac.Role;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.ServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.Assert.*;

/**
 * service-ms
 */
public class UserDaoTest {

    private static final String USER_READ_A = "SELECT \"User\".id, \"User\".first_name, \"User\".middle_name, \"User\".last_name, \"User\".avatar_id, \"Login\".id, \"Role\".id FROM \"User\" JOIN \"Login\", \"Role\" WHERE \"User\".login_id = \"Login\".id AND \"User\".role_id = \"Role\".id AND \"User\".id = ?";
    private static final String USER_READ_B = "SELECT \"User\".id, \"User\".first_name, \"User\".middle_name, \"User\".last_name, \"User\".avatar_id, \"Login\".id, \"Role\".id FROM \"User\" JOIN \"Login\", \"Role\" WHERE \"User\".login_id = \"Login\".id AND \"User\".role_id = \"Role\".id ORDER BY \"User\".first_name ASC";
    private static final String USER_READ_C = "SELECT \"User\".id, \"User\".first_name, \"User\".middle_name, \"User\".last_name, \"User\".avatar_id, \"Login\".id, \"Role\".id FROM \"User\" JOIN \"Login\", \"Role\" WHERE \"User\".login_id = \"Login\".id AND \"User\".role_id = \"Role\".id LIMIT 0, 10";

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
        dao = daoFactory.createDao("User");
        this.daoParameter = new DaoParameter()
                .setWherePredicate(
                        Where.Predicate.get(
                                User.Property.ID,
                                Where.Predicate.PredicateOperator.EQUAL,
                                1L
                        )
                );
        assertEquals("getSqlRead(WHERE) failed", USER_READ_A, ((UserDao) dao).getSqlRead(this.daoParameter).create());
        this.daoParameter = new DaoParameter()
                .setOrderByCriteriaArray(
                        OrderBy.Criteria.asc(User.Property.FIRST_NAME)
                );
        assertEquals("getSqlRead(ORDER) failed", USER_READ_B, ((UserDao) dao).getSqlRead(this.daoParameter).create());
        this.daoParameter = new DaoParameter().setLimit(0, 10);
        assertEquals("getSqlRead(LIMIT) failed", USER_READ_C, ((UserDao) dao).getSqlRead(this.daoParameter).create());
    }

    @Test
    public void getSqlUpdate() throws Exception {

    }

    @Test
    public void getSqlDelete() throws Exception {

    }

    @Test
    public void getEntity() throws Exception {
        dao = daoFactory.createDao("User");
        this.daoParameter = new DaoParameter()
                .setWherePredicate(
                        Where.Predicate.get(
                                User.Property.ID,
                                Where.Predicate.PredicateOperator.EQUAL,
                                2L
                        )
                );
        Sql sql = ((UserDao) dao).getSqlRead(this.daoParameter);
        PreparedStatement statement = ((UserDao) dao).getConnection().prepareStatement(sql.create());
        statement.setLong(1, (Long) sql.getWildValueList().get(0).getVal());
        ResultSet resultSet = statement.executeQuery();
        List<User> userList = ((UserDao) dao).getEntity(resultSet, sql);
        User user = userList.get(0);
        assertNotNull("getEntity() failed", user);
        assertNotNull("getEntity(login) failed", user.getLogin());
        assertNotNull("getEntity(role) failed", user.getRole());
    }

}