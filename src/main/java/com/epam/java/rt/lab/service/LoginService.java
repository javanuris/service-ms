package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.Dao_;
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

    LoginService() throws ConnectionException, DaoException {
    }

    Login getLogin(String email, String password) throws DaoException {
        Login login = new Login();
        login.setEmail(email);
        login.setPassword(password);
        Dao_ dao = super.daoFactory.createDao("Login");
        login = dao.getFirst(login, "email, password");
        daoFactory.close();
        return login;
    }

    public int updatePassword(Login login)
            throws DaoException, SQLException, ConnectionException {
        logger.debug("updateLogin");
        Connection connection = null;
        Dao jdbcDao = null;
//        try {
//            connection = AbstractDaoFactory.createDaoFactory().getConnection();
//            jdbcDao = super.getJdbcDao(connection);
//            logger.debug("jdbcDao = {}", jdbcDao.getClass().getSimpleName());
//            return jdbcDao.update("password").set(login).execute().getLastUpdateCount();
//        } finally {
//            if (jdbcDao != null) jdbcDao.close();
//            if (connection != null) AbstractDaoFactory.createDaoFactory().releaseConnection(connection);
//        }
        return 0;
    }

}
