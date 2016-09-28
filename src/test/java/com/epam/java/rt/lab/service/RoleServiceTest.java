package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.entity.rbac.Role;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * service-ms
 */
public class RoleServiceTest {

    RoleService roleService;

    @Before
    public void setUp() throws Exception {
        this.roleService = new RoleService();
    }

    @After
    public void tearDown() throws Exception {
        this.roleService.close();
        this.roleService = null;
    }

    @Test
    public void getRole() throws Exception {
        Role role = this.roleService.getRole(1L);
        assertNotNull("getRole() failed", role);
        assertEquals("getRole(1L) failed", (Long) 1L, (Long) role.getId());
    }

    @Test
    public void getRoleAuthorized() throws Exception {
        Role role = this.roleService.getRoleAuthorized();
        assertNotNull("getRoleAuthorized() failed", role);
        assertEquals("getRoleAuthorized() failed", "authorized", role.getName());
    }

    @Test
    public void getRoleList() throws Exception {
        List<Role> roleList = this.roleService.getRoleList();
        assertNotNull("getRoleList() failed", roleList);
        assertTrue("getRoleList() failed", roleList.size() > 0);
    }

}