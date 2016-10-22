package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.entity.access.Login;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoginServiceTest {

    LoginService loginService;

    @Before
    public void setUp() throws Exception {
        this.loginService = new LoginService();
    }

    @After
    public void tearDown() throws Exception {
        this.loginService.close();
        this.loginService = null;
    }

    @Test
    public void getLogin() throws Exception {
        Login login = this.loginService.getLogin("test@test.com");
        assertNotNull("getLogin() failed", login);
        assertEquals("getLogin(email) failed", "test@test.com", login.getEmail());
    }

}