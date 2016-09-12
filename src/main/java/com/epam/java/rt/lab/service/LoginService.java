package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.rbac.Login;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * service-ms
 */
public class LoginService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    public LoginService() throws ConnectionException, DaoException {
    }

    public Login getLogin(String email, String password) throws DaoException {
        Login login = new Login();
        login.setEmail(email);
        login.setPassword(password);
        Dao dao = daoFactory.createDao("Login");
        login = dao.getFirst(login, "email, password");
        daoFactory.close();
        return login;
    }

    public int updatePassword(Login login)
            throws DaoException, SQLException, ConnectionException {
        logger.debug("updateLogin");
        Dao dao = daoFactory.createDao("Login");
        int updateCount = dao.update(login, "id", "password");
        daoFactory.close();
        return updateCount;
    }

}
