package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.entity.rbac.Login;

/**
 * service-ms
 */
public class LoginService {

    public static Login getLogin(String email, String password) {
        if (email.equals("test@test.com") && password.equals("test")) {
            Login login = new Login();
            login.setId(1L);
            login.setEmail("test@test.com");
            login.setPassword("test");
            login.setAttemptLeft(5);
            login.setActive(true);
            return login;
        }
        return null;
    }

}
