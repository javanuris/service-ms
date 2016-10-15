package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.entity.access.Login;
import com.epam.java.rt.lab.entity.access.Restore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * category-ms
 */
public class WhereTest {

    private static final String WHERE = " WHERE \"Login\".id = \"Restore\".login_id AND \"Login\".id = \"Login\".email AND \"Restore\".id = ?";
    private static final String FROM_TABLE = "\"Login\"";

    private Where where;
    private List<WildValue> wildValueList;

    @Before
    public void setUp() throws Exception {
        this.wildValueList = new ArrayList<>();
        List<Where.Predicate> predicateList = new ArrayList<>();
        predicateList.add(
                Where.Predicate.get(
                        Login.Property.ID,
                        Where.Predicate.PredicateOperator.EQUAL,
                        Login.Property.EMAIL
                )
        );
        predicateList.add(
                Where.Predicate.get(
                        Restore.Property.ID,
                        Where.Predicate.PredicateOperator.EQUAL,
                        100
                )
        );
        predicateList.get(1).wildValueArray[0].link(this.wildValueList);
        Select.Join join = new Select.Join();
        join.setFrom(FROM_TABLE);
        this.where = new Where(
                join,
                Where.Predicate.get(predicateList)
        );
        assertNotNull("Instantiating failed", this.where);
        assertEquals("toString() failed", WHERE, this.where.appendClause(new StringBuilder()).toString());
    }

    @After
    public void tearDown() throws Exception {
        this.where = null;
    }

    @Test
    public void linkWildValue() throws Exception {
        List<WildValue> wildValueList = new ArrayList<>();
        this.where.linkWildValue(wildValueList);
        this.where.appendClause(new StringBuilder()); // wildValueList fills during executing appendClause() method
        assertEquals("linkWildValue() failed", 1, wildValueList.size());
        assertEquals("getVal() failed", 100, wildValueList.get(0).getVal());
    }

}