package com.epam.java.rt.lab.web.access;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * service-ms
 */
public class RoleFactoryTest {

    @Test
    public void getInstance() throws Exception {
        assertNotNull("getInstance() failed", RoleFactory.getInstance());
    }

    @Test
    public void create() throws Exception {
        assertNotNull("create() failed", RoleFactory.getInstance().create("anonymous"));
        assertEquals("create().getName() failed", "anonymous",
                RoleFactory.getInstance().create("anonymous").getName());
    }

    @Test
    public void getPermissionList() throws Exception {
        assertNotNull("getPermissionList() failed", RoleFactory.getInstance().getPermissionList());
        assertTrue("getPermissionList().get(0) failed",
                RoleFactory.getInstance().getPermissionList().get(0).getUri().equals("/"));
    }

}