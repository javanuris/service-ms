package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.DaoStatement;
import com.epam.java.rt.lab.dao.factory.AbstractDaoFactory;
import com.epam.java.rt.lab.dao.h2.jdbc.JdbcDao;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.PropertyManager;
import com.epam.java.rt.lab.util.TimestampManager;
import com.epam.java.rt.lab.web.validator.ValidatorFactory;
import org.junit.*;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ColumnTest {

    private static final String TABLE_COLUMN_NAME = "\"Login\".id";
    private static final String COLUMN_NAME = "id";
    private static final String TABLE_NAME = "\"Login\"";

    private Column column;

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
        this.column = new Column(TABLE_NAME, COLUMN_NAME);
        assertNotNull("Instantiating failed", this.column);
        assertEquals("toString() failed", TABLE_COLUMN_NAME,
                this.column.appendClause(new StringBuilder()).toString());
    }

    @After
    public void tearDown() throws Exception {
        this.column = null;
    }

    @Test
    public void of() throws Exception {
        this.column = Column.from(TABLE_COLUMN_NAME);
        assertNotNull("Instantiating failed", this.column);
        assertEquals("toString() failed", TABLE_COLUMN_NAME,
                this.column.appendClause(new StringBuilder()).toString());
    }

    @Test
    public void getTableName() throws Exception {
        assertEquals("getTableName() failed", TABLE_NAME,
                this.column.getTableName());
    }

    @Test
    public void getColumnName() throws Exception {
        assertEquals("getColumnName() failed", COLUMN_NAME,
                this.column.getColumnName());
    }

}