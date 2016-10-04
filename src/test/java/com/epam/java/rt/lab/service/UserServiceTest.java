package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * category-ms
 */
public class UserServiceTest {

    UserService userService;

    @Before
    public void setUp() throws Exception {
        this.userService = new UserService();
    }

    @After
    public void tearDown() throws Exception {
        this.userService.close();
        this.userService = null;
    }

    @Test
    public void getUserByLogin() throws Exception {
        Login login = new LoginService().getLogin("test@test.com");
        User user = this.userService.getUser(login);
        assertNotNull("getUserByLogin() failed", user);
        assertNotNull("getUserByLogin(Login) failed", user.getLogin());
    }

    @Test
    public void getUserById() throws Exception {
        User user = this.userService.getUser(3L);
        assertNotNull("getUserById() failed", user);
        assertNotNull("getUserById(3L) failed", user.getRole());
    }

    @Test
    public void getAnonymous() throws Exception {
        User user = this.userService.getAnonymous();
        assertNotNull("getAnonymous() failed", user);
        assertNotNull("getAnonymous(Role) failed", user.getRole());
    }

    @Test
    public void getRemember() throws Exception {

    }

    @Test
    public void setRemember() throws Exception {

    }

}