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

    public Login getLogin(String email, String password) throws DaoException, SQLException, ConnectionException {
        logger.debug("getLogin");
        Connection connection = null;
        try {
            Dao jdbcDao = super.getJdbcDao();
            logger.debug("jdbcDao = {}", jdbcDao.getClass().getSimpleName());
            connection = DaoFactory.getDaoFactory().getConnection();
            Login login = jdbcDao.find(connection, "email", email).first();
            logger.debug("login.email = {}", login.getEmail());
            DaoFactory.getDaoFactory().releaseConnection(connection);
            jdbcDao.close();
            return login;
        } finally {
            if (connection != null) DaoFactory.getDaoFactory().releaseConnection(connection);
        }
    }

}
