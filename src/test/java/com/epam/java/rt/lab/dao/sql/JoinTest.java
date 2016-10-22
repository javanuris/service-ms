package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.DaoStatement;
import com.epam.java.rt.lab.dao.factory.AbstractDaoFactory;
import com.epam.java.rt.lab.dao.h2.jdbc.JdbcDao;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.PropertyManager;
import com.epam.java.rt.lab.util.TimestampManager;
import com.epam.java.rt.lab.web.validator.ValidatorFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * category-ms
 */
public class JoinTest {

    private static final String FROM_TABLE = "\"Login\"";
    private static final String JOIN_TABLE = "\"Restore\"";
    private static final String JOIN_PREDICATE =
            "\"Login\".id = \"Restore\".login_id";
    private static final String JOIN = " JOIN \"Restore\"";

    private Select.Join join;

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
        this.join = new Select.Join();
        assertNotNull("Instantiating failed", this.join);
    }

    @After
    public void tearDown() throws Exception {
        this.join = null;
    }

    @Test
    public void addJoin() throws Exception {
        this.join.setFrom(FROM_TABLE);
        this.join.addJoin(JOIN_TABLE);
        this.join.addJoin(JOIN_TABLE);
        this.join.addJoin(JOIN_TABLE);
        // nothing to assert
    }

    @Test
    public void getPredicate() throws Exception {
        this.join.setFrom(FROM_TABLE);
        this.join.addJoin(JOIN_TABLE);
        this.join.addJoin(JOIN_TABLE);
        this.join.addJoin(JOIN_TABLE);
        assertEquals("getPredicate() failed", JOIN_PREDICATE,
                this.join.getPredicate().
                        appendClause(new StringBuilder()).toString());
    }

    @Test
    public void appendClause() throws Exception {
        this.join.setFrom(FROM_TABLE);
        this.join.addJoin(JOIN_TABLE);
        this.join.addJoin(JOIN_TABLE);
        this.join.addJoin(JOIN_TABLE);
        assertEquals("appendClause() failed", JOIN,
                this.join.appendClause(new StringBuilder()).toString());
    }

}