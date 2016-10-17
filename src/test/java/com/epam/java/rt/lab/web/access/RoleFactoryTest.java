package com.epam.java.rt.lab.web.access;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.PropertyManager;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RoleFactoryTest {

    @Before
    public void setUp() throws Exception {
        PropertyManager.initGlobalProperties();
        AppException.initExceptionBundle();
        RoleFactory.getInstance().initRoleMap();
    }

    @Test
    public void getInstance() throws Exception {
        assertNotNull("getInstance() failed", RoleFactory.getInstance());
    }

    @Test
    public void create() throws Exception {
        assertNotNull("create() failed", RoleFactory.getInstance().
                create("manager"));
        assertEquals("create().getName() failed", "manager",
                RoleFactory.getInstance().create("manager").getName());
    }

    @Test
    public void createAnonymous() throws Exception {
        assertNotNull("createAnonymous() failed", RoleFactory.getInstance().
                createAnonymous());
        assertEquals("createAnonymous().getName() failed", "anonymous",
                RoleFactory.getInstance().createAnonymous().getName());
    }

    @Test
    public void createAthorized() throws Exception {
        assertNotNull("createAuthorized() failed", RoleFactory.getInstance().
                createAuthorized());
        assertEquals("createAuthorized().getName() failed", "authorized",
                RoleFactory.getInstance().createAuthorized().getName());
    }
}