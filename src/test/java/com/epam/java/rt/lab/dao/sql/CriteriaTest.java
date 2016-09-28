package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.entity.EntityProperty;
import com.epam.java.rt.lab.entity.rbac.Login;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * service-ms
 */
public class CriteriaTest {

    private static final String LOGIN_ID = "\"Login\".id";

    private OrderBy.Criteria criteria;

    @Before
    public void setUp() throws Exception {
        this.criteria = new OrderBy.Criteria(Column.of(LOGIN_ID), false);
        assertNotNull("Instantiating failed", this.criteria);
        assertEquals("toString() failed", LOGIN_ID.concat(" ASC"), this.criteria.appendClause(new StringBuilder()).toString());
    }

    @After
    public void tearDown() throws Exception {
        this.criteria = null;
    }

    @Test
    public void asc() throws Exception {
        this.criteria = OrderBy.Criteria.asc(Login.Property.ID);
        assertNotNull("Instantiating failed", this.criteria);
        assertEquals("asc() failed", LOGIN_ID.concat(" ASC"), this.criteria.appendClause(new StringBuilder()).toString());
    }

    @Test
    public void desc() throws Exception {
        this.criteria = OrderBy.Criteria.desc(Login.Property.ID);
        assertNotNull("Instantiating failed", this.criteria);
        assertEquals("desc() failed", LOGIN_ID.concat(" DESC"), this.criteria.appendClause(new StringBuilder()).toString());
    }

}