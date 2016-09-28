package com.epam.java.rt.lab.dao.sql;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * service-ms
 */
public class FromTest {

    private static final String FROM = " FROM \"Login\"";

    private Select.From from;
    private Select.Join join;

    @Before
    public void setUp() throws Exception {
        List<Column> columnList = new ArrayList<>();
        columnList.add(Column.of("\"Login\".id"));
        columnList.add(Column.of("\"Login\".email"));
        columnList.add(Column.of("\"Restore\".id"));
        columnList.add(Column.of("\"Restore\".code"));
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
        assertEquals("appendClause() failed", FROM, this.from.appendClause(new StringBuilder()).toString());
    }

}