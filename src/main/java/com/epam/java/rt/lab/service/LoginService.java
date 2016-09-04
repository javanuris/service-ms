package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.factory.DaoFactory;
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

    public LoginService() throws DaoException {
        super();
    }

    Login getLogin(String email, String password, Connection connection)
            throws DaoException, SQLException, ConnectionException {
        logger.debug("getLogin");
        Dao jdbcDao = null;
        try {
            jdbcDao = super.getJdbcDao(connection);
            logger.debug("jdbcDao = {}", jdbcDao.getClass().getSimpleName());
            Login login = jdbcDao.query("*").filter("email", email).first();
            if (login == null || !login.getPassword().equals(password)) return null;
            logger.debug("login.email = {}", login.getEmail());
            return login;
        } finally {
            if (jdbcDao != null) jdbcDao.close();
        }
    }

    public Login getLogin(String email, String password) throws DaoException, SQLException, ConnectionException {
        logger.debug("getLogin");
        Connection connection = null;
        try {
            connection = DaoFactory.getDaoFactory().getConnection();
            return getLogin(email, password, connection);
        } finally {
            if (connection != null) DaoFactory.getDaoFactory().releaseConnection(connection);
        }
    }

}
