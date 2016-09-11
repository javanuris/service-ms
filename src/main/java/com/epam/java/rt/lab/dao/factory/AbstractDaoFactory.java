package com.epam.java.rt.lab.dao.factory;

import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.connection.ConnectionPool;
import com.epam.java.rt.lab.dao.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * service-ms
 */
public abstract class AbstractDaoFactory implements DaoFactory {
    private static final Logger logger = LoggerFactory.getLogger(AbstractDaoFactory.class);
    private static Properties databaseProperties = null;
    private static DataSource connectionPool = null;
    private Connection connection = null;

    public static DaoFactory createDaoFactory() throws DaoException, ConnectionException {
        logger.debug("createDaoFactory");
        try {
            if (AbstractDaoFactory.databaseProperties == null) {
                AbstractDaoFactory.databaseProperties = new Properties();
                AbstractDaoFactory.databaseProperties.load
                        (AbstractDaoFactory.class.getClassLoader().getResourceAsStream("database.properties"));
            }
            if (AbstractDaoFactory.connectionPool == null) {
                ConnectionPool.initProperties(databaseProperties);
                AbstractDaoFactory.connectionPool = ConnectionPool.getInstance();
                Class.forName(databaseProperties.getProperty("management-system.class"));
            }
            Class factoryClass = Class.forName(AbstractDaoFactory.databaseProperties.getProperty("management-system.factory"));
            Constructor<?> factoryClassConstructor = factoryClass.getConstructor(Connection.class);
            return (DaoFactory) factoryClassConstructor.newInstance(AbstractDaoFactory.connectionPool.getConnection());
        } catch (IOException e) {
            throw new DaoException("exception.dao.properties.load", e.getCause());
        } catch (ClassNotFoundException e) {
            throw new DaoException("exception.dao.factory.for-name", e.getCause());
        } catch (NoSuchMethodException e) {
            throw new DaoException("exception.dao.factory.get-constructor", e.getCause());
        } catch (SQLException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.factory.new-instance", e.getCause());
        }
    }

    static String getDatabaseProperty(String key) throws DaoException {
        String databaseProperty = AbstractDaoFactory.databaseProperties.getProperty(key);
        if (databaseProperty == null) throw new DaoException("exception.dao.properties.key");
        return databaseProperty;
    }

    public AbstractDaoFactory(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getConnection() throws DaoException {
        if (this.connection == null) throw new DaoException("exception.dao.connection.get");
        return this.connection;
    }

    @Override
    public void beginTransaction(int transactionIsolation) throws DaoException {
        try {
            if (this.connection == null) throw new DaoException("exception.dao.connection.get");
            this.connection.setAutoCommit(false);
            this.connection.setTransactionIsolation(transactionIsolation);
        } catch (SQLException e) {
            throw new DaoException("exception.dao.connection.transaction.begin", e.getCause());
        }
    }

    @Override
    public void commitTransaction() throws DaoException {
        try {
            if (this.connection == null) throw new DaoException("exception.dao.connection.get");
            this.connection.commit();
        } catch (SQLException e) {
            throw new DaoException("exception.dao.connection.transaction.commit", e.getCause());
        }
    }

    @Override
    public void rollbackTransaction() throws DaoException {
        try {
            if (this.connection == null) throw new DaoException("exception.dao.connection.get");
            this.connection.rollback();
        } catch (SQLException e) {
            throw new DaoException("exception.dao.connection.transaction.rollback", e.getCause());
        }
    }

    @Override
    public void close() throws DaoException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new DaoException("exception.dao.factory.close",e.getCause());
        }
    }

}