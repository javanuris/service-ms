package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.DaoStatement;
import com.epam.java.rt.lab.dao.factory.AbstractDaoFactory;
import com.epam.java.rt.lab.dao.factory.DaoFactory;
import com.epam.java.rt.lab.dao.sql.OrderBy.Criteria;
import com.epam.java.rt.lab.dao.sql.Sql;
import com.epam.java.rt.lab.dao.sql.Where.Predicate;
import com.epam.java.rt.lab.dao.sql.WherePredicateOperator;
import com.epam.java.rt.lab.entity.access.User;
import com.epam.java.rt.lab.entity.access.UserProperty;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.PropertyManager;
import com.epam.java.rt.lab.util.TimestampManager;
import com.epam.java.rt.lab.web.access.RoleFactory;
import com.epam.java.rt.lab.web.validator.ValidatorFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserDaoTest {

    private static final String USER_READ_A =
            "SELECT \"User\".id, \"User\".first_name, \"User\".middle_name,"
                    + " \"User\".last_name, \"User\".role_name,"
                    + " \"User\".avatar_id, \"Login\".id FROM \"User\" JOIN"
                    + " \"Login\" WHERE \"User\".login_id ="
                    + " \"Login\".id AND \"User\".id = ?";
    private static final String USER_READ_B =
            "SELECT \"User\".id, \"User\".first_name, \"User\".middle_name,"
                    + " \"User\".last_name, \"User\".role_name,"
                    + " \"User\".avatar_id, \"Login\".id FROM \"User\" JOIN"
                    + " \"Login\" WHERE \"User\".login_id = \"Login\".id"
                    + " ORDER BY \"User\".first_name ASC";
    private static final String USER_READ_C =
            "SELECT \"User\".id, \"User\".first_name, \"User\".middle_name,"
                    + " \"User\".last_name, \"User\".role_name,"
                    + " \"User\".avatar_id, \"Login\".id FROM \"User\" JOIN"
                    + " \"Login\" WHERE \"User\".login_id = \"Login\".id"
                    + " LIMIT 0, 10";

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
        RoleFactory.getInstance().initRoleMap();
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
        dao = daoFactory.createDao("User");
        this.daoParameter = new DaoParameter();
        this.daoParameter.setWherePredicate(Predicate.
                get(UserProperty.ID, WherePredicateOperator.EQUAL, 1L));
        assertEquals("getSqlRead(WHERE) failed", USER_READ_A,
                ((UserDao) dao).getSqlRead(this.daoParameter).create());
        this.daoParameter = new DaoParameter();
        this.daoParameter.
                setOrderByCriteriaArray(Criteria.
                        asc(UserProperty.FIRST_NAME));
        assertEquals("getSqlRead(ORDER) failed", USER_READ_B,
                ((UserDao) dao).getSqlRead(this.daoParameter).create());
        this.daoParameter = new DaoParameter().setLimit(0L, 10L);
        assertEquals("getSqlRead(LIMIT) failed", USER_READ_C,
                ((UserDao) dao).getSqlRead(this.daoParameter).create());
    }

    @Test
    public void getEntity() throws Exception {
        dao = daoFactory.createDao("User");
        this.daoParameter = new DaoParameter();
        this.daoParameter.setWherePredicate(Predicate.
                get(UserProperty.ID, WherePredicateOperator.EQUAL, 2L));
        Sql sql = ((UserDao) dao).getSqlRead(this.daoParameter);
        PreparedStatement statement =
                ((UserDao) dao).getConnection().prepareStatement(sql.create());
        statement.setLong(1, (Long) sql.getWildValueList().get(0).getVal());
        ResultSet resultSet = statement.executeQuery();
        List<User> userList = ((UserDao) dao).getEntity(resultSet, sql);
        User user = userList.get(0);
        assertNotNull("getEntity() failed", user);
        assertNotNull("getEntity(login) failed", user.getLogin());
        assertNotNull("getEntity(role) failed", user.getRole());
    }

}