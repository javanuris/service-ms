package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.factory.DaoFactory;
import com.epam.java.rt.lab.entity.rbac.Login;

/**
 * service-ms
 */
public class LoginService extends BaseService {
    private DaoFactory factory;

    public LoginService() throws DaoException {
        super();
    }

    public Login getLogin(String email, String password) throws DaoException {
        Dao jdbcDao = super.getJdbcDao();
        



        if (email.equals("test@test.com") && password.equals("test")) {
            Login login = new Login();
            login.setId(1L);
            login.setEmail("test@test.com");
            login.setPassword("test");
            login.setAttemptLeft(5);
            login.setStatus(0);
            return login;
        }
        return null;
    }

}
