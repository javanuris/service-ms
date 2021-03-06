package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.DaoStatement;
import com.epam.java.rt.lab.dao.factory.AbstractDaoFactory;
import com.epam.java.rt.lab.dao.h2.jdbc.JdbcDao;
import com.epam.java.rt.lab.entity.access.LoginProperty;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.PropertyManager;
import com.epam.java.rt.lab.util.TimestampManager;
import com.epam.java.rt.lab.web.validator.ValidatorFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PredicateTest {

    private static final String LOGIN_ID_EMAIL_PREDICATE_AND_LOGIN_ID_EMAIL_PREDICATE =
            "\"Login\".id = \"Login\".email AND \"Login\".id = \"Login\".email";
    private static final String LOGIN_ID_EMAIL_PREDICATE_AND_LOGIN_ID_WILDCARD_PREDICATE =
            "\"Login\".id = \"Login\".email AND \"Login\".id = ?";
    private static final String LOGIN_ID_EQUAL_LOGIN_EMAIL = "\"Login\".id = \"Login\".email";
    private static final String LOGIN_ID_EQUAL_WILDCARD = "\"Login\".id = ?";
    private static final String LOGIN_ID_IS_NULL = "\"Login\".id IS NULL";
    private static final String LOGIN_ID_IS_NOT_NULL = "\"Login\".id IS NOT NULL";

    private Where.Predicate predicate;
    private List<WildValue> wildValueList;

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
        this.predicate = new Where.Predicate(
                Column.from("\"Login\".id"),
                WherePredicateOperator.EQUAL,
                Column.from("\"Login\".email")
        );
        assertNotNull("Instantiating failed", this.predicate);
        assertEquals("toString() failed", LOGIN_ID_EQUAL_LOGIN_EMAIL,
                this.predicate.appendClause(new StringBuilder()).toString());
        this.wildValueList = new ArrayList<>();
    }

    @After
    public void tearDown() throws Exception {
        this.predicate = null;
    }

    @Test
    public void getEntityToEntity() throws Exception {
        this.predicate = Where.Predicate.get(
                LoginProperty.ID,
                WherePredicateOperator.EQUAL,
                LoginProperty.EMAIL
        );
        assertNotNull("Instantiating failed", this.predicate);
        assertEquals("get(EntityProperty) failed", LOGIN_ID_EQUAL_LOGIN_EMAIL,
                this.predicate.appendClause(new StringBuilder()).toString());
    }

    @Test
    public void getEntityToValue() throws Exception {
        this.predicate = Where.Predicate.get(
                LoginProperty.ID,
                WherePredicateOperator.EQUAL,
                100
        );
        assertNotNull("Instantiating failed", this.predicate);
        for (WildValue wildValue : this.predicate.wildValueArray) {
            wildValue.link(wildValueList);
        }
        assertEquals("get(EntityValue) failed", LOGIN_ID_EQUAL_WILDCARD,
                this.predicate.appendClause(new StringBuilder()).toString());
    }

    @Test
    public void getPredicateToPredicate() throws Exception {
        this.predicate = Where.Predicate.get(
                Where.Predicate.get(
                        LoginProperty.ID,
                        WherePredicateOperator.EQUAL,
                        LoginProperty.EMAIL
                ),
                WherePredicateOperator.AND,
                Where.Predicate.get(
                        LoginProperty.ID,
                        WherePredicateOperator.EQUAL,
                        LoginProperty.EMAIL
                )
        );
        assertNotNull("Instantiating failed", this.predicate);
        assertEquals("get(Predicate) failed",
                LOGIN_ID_EMAIL_PREDICATE_AND_LOGIN_ID_EMAIL_PREDICATE,
                this.predicate.appendClause(new StringBuilder()).toString());
    }

    @Test(expected = AppException.class)
    public void getPredicateToPredicateException() throws Exception {
        this.predicate = Where.Predicate.get(
                Where.Predicate.get(
                        LoginProperty.ID,
                        WherePredicateOperator.EQUAL,
                        LoginProperty.EMAIL
                ),
                WherePredicateOperator.EQUAL,
                Where.Predicate.get(
                        LoginProperty.ID,
                        WherePredicateOperator.EQUAL,
                        LoginProperty.EMAIL
                )
        );
        assertNotNull("Instantiating failed", this.predicate);
        assertEquals("get(Predicate) failed",
                LOGIN_ID_EMAIL_PREDICATE_AND_LOGIN_ID_EMAIL_PREDICATE,
                this.predicate.appendClause(new StringBuilder()).toString());
    }

    @Test
    public void isNull() throws Exception {
        this.predicate = Where.Predicate.isNull(LoginProperty.ID);
        assertNotNull("Instantiating failed", this.predicate);
        assertEquals("isNull() failed", LOGIN_ID_IS_NULL,
                this.predicate.appendClause(new StringBuilder()).toString());
    }

    @Test
    public void isNotNull() throws Exception {
        this.predicate = Where.Predicate.isNotNull(LoginProperty.ID);
        assertNotNull("Instantiating failed", this.predicate);
        assertEquals("isNotNull() failed", LOGIN_ID_IS_NOT_NULL,
                this.predicate.appendClause(new StringBuilder()).toString());
    }

    @Test
    public void getPredicateList() throws Exception {
        List<Where.Predicate> joinPredicateList = new ArrayList<>();
        joinPredicateList.add(
                Where.Predicate.get(
                        LoginProperty.ID,
                        WherePredicateOperator.EQUAL,
                        LoginProperty.EMAIL
                )
        );
        joinPredicateList.add(
                Where.Predicate.get(
                        LoginProperty.ID,
                        WherePredicateOperator.EQUAL,
                        100
                )
        );
        for (WildValue wildValue : joinPredicateList.get(1).wildValueArray) {
            wildValue.link(wildValueList);
        }
        this.predicate = Where.Predicate.get(joinPredicateList);
        assertNotNull("Instantiating failed", this.predicate);
        assertEquals("get(PredicateList) failed",
                LOGIN_ID_EMAIL_PREDICATE_AND_LOGIN_ID_WILDCARD_PREDICATE,
                this.predicate.appendClause(new StringBuilder()).toString());
    }

}