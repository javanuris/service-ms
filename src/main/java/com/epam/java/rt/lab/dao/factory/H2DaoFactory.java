package com.epam.java.rt.lab.dao.factory;

import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.connection.ConnectionPool;
import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * service-ms
 */
public class H2DaoFactory extends DaoFactory {
    private static final Logger logger = LoggerFactory.getLogger(H2DaoFactory.class);
    private static ConnectionPool connectionPool = null;

    private H2DaoFactory() {
    }

    public static H2DaoFactory getInstance() throws DaoException {
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
    public Dao getJdbcDao(String entityName) throws DaoException {
        try {
            if (DaoFactory.getDbmsName() == null) throw new DaoException("DBMS not defined");
            Class<Dao> entityClass = (Class<Dao>) Class.forName
                    (DaoFactory.getDbmsName().concat("Jdbc").concat(entityName).concat("Dao"));
            return entityClass.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new DaoException(e.getMessage());
        }
    }

    private static class InstanceHolder {
        private static final H2DaoFactory INSTANCE = new H2DaoFactory();
    }

}
