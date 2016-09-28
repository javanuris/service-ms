package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.entity.rbac.Login;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * service-ms
 */
public class OrderByTest {

    private static final String ORDER_BY = " ORDER BY \"Login\".id ASC, \"Login\".email DESC";

    private OrderBy orderBy;

    @Before
    public void setUp() throws Exception {
        OrderBy.Criteria[] criteriaArray = {OrderBy.Criteria.asc(Login.Property.ID), OrderBy.Criteria.desc(Login.Property.EMAIL)};
        this.orderBy = new OrderBy(criteriaArray);
        assertNotNull("Instantiating failed", this.orderBy);
    }

    @After
    public void tearDown() throws Exception {
        this.orderBy = null;
    }

    @Test
    public void toStringTest() throws Exception {
        assertEquals("toString() failed", ORDER_BY, this.orderBy.appendClause(new StringBuilder()).toString());
    }

}