package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.entity.rbac.Login;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * category-ms
 */
public class SelectTest {

    private Select select;

    @Before
    public void setUp() throws Exception {
        List<Column> columnList = new ArrayList<>();
        columnList.add(Column.of("\"Restore\".id"));
        columnList.add(Column.of("\"Restore\".code"));
        columnList.add(Column.of("\"Login\".id"));
        columnList.add(Column.of("\"Login\".email"));
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
                        Login.Property.ID,
                        Where.Predicate.PredicateOperator.EQUAL,
                        100
                )
        );
        // no result to assert
//        System.out.println(select.create());
    }

    @Test
    public void orderBy() throws Exception {

    }

    @Test
    public void limit() throws Exception {

    }

    @Test
    public void create() throws Exception {

    }

    @Test
    public void iterator() throws Exception {

    }

}