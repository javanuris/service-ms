package com.epam.java.rt.lab.web.component.form;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * service-ms
 */
public class FormFactoryTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void create() throws Exception {
        Form form = FormFactory.getInstance().create("login-profile");
        System.out.println(form);
    }

}