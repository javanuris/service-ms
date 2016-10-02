package com.epam.java.rt.lab.dao.sql;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * service-ms
 */
public class LimitTest {

    private static final String LIMIT = " LIMIT 0, 10";

    private Select.Limit limit;

    @Before
    public void setUp() throws Exception {
        this.limit = new Select.Limit(0L, 10L);
        assertNotNull("Instantiating failed", this.limit);
    }

    @After
    public void tearDown() throws Exception {
        this.limit = null;
    }

    @Test
    public void appendClause() throws Exception {
        assertEquals("appendClause() failed", LIMIT, this.limit.appendClause(new StringBuilder()).toString());
    }

}