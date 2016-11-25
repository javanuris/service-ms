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

import static org.junit.Assert.assertNotNull;

public class SelectTest {

    private Select select;

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
        List<Column> columnList = new ArrayList<>();
        columnList.add(Column.from("\"Restore\".id"));
        columnList.add(Column.from("\"Restore\".code"));
        columnList.add(Column.from("\"Login\".id"));
        columnList.add(Column.from("\"Login\".email"));
        this.select = new Select(columnList);
        assertNotNull("Instantiating failed", this.select);
    }

    @After
    public void tearDown() throws Exception {
        this.select = null;
    }

    @Test
    public void where() throws Exception {
        this.select.where(
                Where.Predicate.get(
                        LoginProperty.ID,
                        WherePredicateOperator.EQUAL,
                        100
                )
        );
        // no result to assert
    }

}