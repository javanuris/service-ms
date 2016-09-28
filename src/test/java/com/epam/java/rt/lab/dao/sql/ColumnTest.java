package com.epam.java.rt.lab.dao.sql;

import org.junit.*;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

/**
 * service-ms
 */
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ColumnTest {

    private static final String TABLE_COLUMN_NAME = "\"Login\".id";
    private static final String COLUMN_NAME = "id";
    private static final String TABLE_NAME = "\"Login\"";

    private Column column;

    @Before
    public void setUp() throws Exception {
        this.column = new Column(TABLE_NAME, COLUMN_NAME);
        assertNotNull("Instantiating failed", this.column);
        assertEquals("toString() failed", TABLE_COLUMN_NAME, this.column.appendClause(new StringBuilder()).toString());
    }

    @After
    public void tearDown() throws Exception {
        this.column = null;
    }

    @Test
    public void of() throws Exception {
        this.column = Column.of(TABLE_COLUMN_NAME);
        assertNotNull("Instantiating failed", this.column);
        assertEquals("toString() failed", TABLE_COLUMN_NAME, this.column.appendClause(new StringBuilder()).toString());
    }

    @Test
    public void getTableName() throws Exception {
        assertEquals("getTableName() failed", TABLE_NAME, this.column.getTableName());
    }

    @Test
    public void getColumnName() throws Exception {
        assertEquals("getColumnName() failed", COLUMN_NAME, this.column.getColumnName());
    }

}