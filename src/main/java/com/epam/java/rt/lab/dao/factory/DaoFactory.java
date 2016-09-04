package com.epam.java.rt.lab.dao.factory;

import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * service-ms
 */
public abstract class DaoFactory {
    private static final Logger logger = LoggerFactory.getLogger(DaoFactory.class);
    private static DaoFactory factory = null;

    public static DaoFactory getDaoFactory() throws DaoException {
        if (factory == null) DaoFactory.resetProperties("database.properties");
        return factory;
    }

    public static void resetProperties(String fileName) throws DaoException {
        try {
            Properties properties = new Properties();
            properties.load(DaoFactory.class.getClassLoader().getResourceAsStream(fileName));
            String factoryClassName = DaoFactory.class.getPackage().getName().concat(".")
                    .concat(properties.getProperty("database.management-system.name")).concat("DaoFactory");
            logger.debug("factoryClassName = {}", factoryClassName);
            if (factoryClassName == null) throw new DaoException("Database properties error");
            Method method = Class.forName(factoryClassName).getMethod("getInstance");
            logger.debug("method = {}", method.getName());
            DaoFactory.factory = (DaoFactory) method.invoke(null);
            logger.debug("factory = {}", factory.getClass().getName());
        } catch (IOException | ClassNotFoundException |
                NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            logger.debug("Reset Properties Exception", e);
            throw new DaoException(e.getMessage());
        }
    }

    public abstract Dao getJdbcDao(String entityName, Connection connection) throws DaoException;

    public abstract Connection getConnection() throws SQLException;

    public abstract void releaseConnection(Connection connection) throws ConnectionException;

}
