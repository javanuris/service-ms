package com.epam.java.rt.lab.dao.sql;

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
    private static final String JOIN_PREDICATE = "\"Login\".id = \"Restore\".login_id";
    private static final String JOIN = " JOIN \"Restore\"";

    private Select.Join join;

    @Before
    public void setUp() throws Exception {
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
        assertEquals("getPredicate() failed", JOIN_PREDICATE, this.join.getPredicate().appendClause(new StringBuilder()).toString());
    }

    @Test
    public void appendClause() throws Exception {
        this.join.setFrom(FROM_TABLE);
        this.join.addJoin(JOIN_TABLE);
        this.join.addJoin(JOIN_TABLE);
        this.join.addJoin(JOIN_TABLE);
        assertEquals("appendClause() failed", JOIN, this.join.appendClause(new StringBuilder()).toString());
    }

}