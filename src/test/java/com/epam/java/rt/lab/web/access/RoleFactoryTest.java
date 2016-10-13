package com.epam.java.rt.lab.web.access;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * category-ms
 */
public class RoleFactoryTest {

    @Test
    public void getInstance() throws Exception {
        assertNotNull("getInstance() failed", RoleFactory.getInstance());
    }

    @Test
    public void create() throws Exception {
        assertNotNull("create() failed", RoleFactory.getInstance().create("dictionary.role.anonymous"));
        assertEquals("create().getName() failed", "dictionary.role.anonymous",
                RoleFactory.getInstance().create("dictionary.role.anonymous").getName());
    }

    @Test
    public void getPermissionList() throws Exception {
        assertNotNull("getPermissionList() failed", RoleFactory.getInstance().getPermissionList());
        assertTrue("getPermissionList().valueOf(0) failed",
                RoleFactory.getInstance().getPermissionList().get(0).getUri().equals("/"));
    }

}