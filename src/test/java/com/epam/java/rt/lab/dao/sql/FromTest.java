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

import java.util.ArrayList;
import java.util.List;

public class FromTest {

    private static final String FROM = " FROM \"Login\"";

    private Select.From from;
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
        List<Column> columnList = new ArrayList<>();
        columnList.add(Column.from("\"Login\".id"));
        columnList.add(Column.from("\"Login\".email"));
        columnList.add(Column.from("\"Restore\".id"));
        columnList.add(Column.from("\"Restore\".code"));
        this.join = new Select.Join();
        this.from = new Select.From(columnList, this.join);
        assertNotNull("Instantiating failed", this.from);
    }

    @After
    public void tearDown() throws Exception {
        this.from = null;
    }

    @Test
    public void appendClause() throws Exception {
        assertEquals("appendClause() failed", FROM,
                this.from.appendClause(new StringBuilder()).toString());
    }

}