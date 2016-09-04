package com.epam.java.rt.lab.dao.factory;

import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.connection.ConnectionPool;
import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.h2.H2JdbcDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * service-ms
 */
public class H2DaoFactory extends DaoFactory {
    private static final Logger logger = LoggerFactory.getLogger(H2DaoFactory.class);
    private static ConnectionPool connectionPool = null;

    private H2DaoFactory() {
    }

    public static DaoFactory getInstance() throws DaoException {
        try {
            if (connectionPool == null) {
                H2DaoFactory instance = InstanceHolder.INSTANCE;
                connectionPool = ConnectionPool.getInstance();
                return instance;
            }
        } catch (ConnectionException e) {
            throw new DaoException(e.getMessage());
        }
        return null;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    @Override
    public void releaseConnection(Connection connection) throws ConnectionException {
        connectionPool.releaseConnection(connection);
    }

    @Override
    public Dao getJdbcDao(String entityName, Connection connection) throws DaoException {
        try {
            String jdbcClassName = H2JdbcDao.class.getPackage().getName().concat(".")
                    .concat("H2Jdbc").concat(entityName).concat("Dao");
            logger.debug("jdbcDao = {}", jdbcClassName);
            Dao jdbcDao = (Dao) Class.forName(jdbcClassName).newInstance();
            if (connection == null) {
                jdbcDao.setConnection(ConnectionPool.getInstance().getConnection());
            } else {
                jdbcDao.setConnection(connection);
            }
            return jdbcDao;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                ConnectionException | SQLException e) {
            logger.debug("Exception", e);
            throw new DaoException(e.getMessage());
        }
    }

    private static class InstanceHolder {
        private static final H2DaoFactory INSTANCE = new H2DaoFactory();
    }

}
