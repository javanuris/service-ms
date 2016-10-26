package com.epam.java.rt.lab.dao.factory;

import com.epam.java.rt.lab.connection.ConnectionPool;
import com.epam.java.rt.lab.exception.AppException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static com.epam.java.rt.lab.dao.DaoExceptionCode.*;
import static com.epam.java.rt.lab.exception.AppExceptionCode.*;

public abstract class AbstractDaoFactory implements DaoFactory {

    private static final String DB_MANAGEMENT_SYSTEM_CLASS =
            "management-system.class";
    private static final String DB_MANAGEMENT_SYSTEM_FACTORY =
            "management-system.factory";
    private static final String DB_PROPERTY_FILE = "db.properties";

    private static final Properties databaseProperties = new Properties();

    private Connection connection = null;

    public static void initDatabaseProperties() throws AppException {
        ClassLoader classLoader = AbstractDaoFactory.class.getClassLoader();
        InputStream inputStream =
                classLoader.getResourceAsStream(DB_PROPERTY_FILE);
        if (inputStream == null) {
            String[] detailArray = {DB_PROPERTY_FILE};
            throw new AppException(PROPERTY_READ_ERROR, detailArray);
        }
        try {
            AbstractDaoFactory.databaseProperties.load(inputStream);
            ConnectionPool.getInstance().initDatabaseProperties(
                    AbstractDaoFactory.databaseProperties);
            String databaseDriverClassName = AbstractDaoFactory.
                    databaseProperties.getProperty(DB_MANAGEMENT_SYSTEM_CLASS);
            Class.forName(databaseDriverClassName);
        } catch (IOException e) {
            String[] detailArray = {DB_PROPERTY_FILE};
            throw new AppException(PROPERTY_READ_ERROR,
                    e.getMessage(), e.getCause(), detailArray);
        } catch (ClassNotFoundException e) {
            throw new AppException(DB_DRIVER_CLASS_NOT_FOUND,
                    e.getMessage(), e.getCause());
        }
    }

    public static DaoFactory createDaoFactory() throws AppException {
        String daoFactoryClassName = AbstractDaoFactory.
                databaseProperties.getProperty(DB_MANAGEMENT_SYSTEM_FACTORY);
        if (daoFactoryClassName == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        try {
            Class factoryClass = Class.forName(daoFactoryClassName);
            Constructor<?> factoryClassConstructor =
                    factoryClass.getConstructor(Connection.class);
            return (DaoFactory) factoryClassConstructor.
                    newInstance(ConnectionPool.getInstance().getConnection());
        } catch (ClassNotFoundException e) {
            String[] detailArray = {daoFactoryClassName};
            throw new AppException(DB_FACTORY_CLASS_NOT_FOUND,
                    e.getMessage(), e.getCause(), detailArray);
        } catch (NoSuchMethodException e) {
            String[] detailArray = {daoFactoryClassName};
            throw new AppException(DB_FACTORY_CONSTRUCTOR_NOT_FOUND,
                    e.getMessage(), e.getCause(), detailArray);
        } catch (SQLException e) {
            String[] detailArray = {daoFactoryClassName};
            throw new AppException(GET_CONNECTION_ERROR,
                    e.getMessage(), e.getCause(), detailArray);
        } catch (IllegalAccessException | InstantiationException
                | InvocationTargetException e) {
            String[] detailArray = {daoFactoryClassName};
            throw new AppException(DB_FACTORY_INSTANTIATE_ERROR,
                    e.getMessage(), e.getCause(), detailArray);
        }
    }

    public static String getDatabaseProperty(String key) throws AppException {
        if (key == null) throw new AppException(NULL_NOT_ALLOWED);
        String databaseProperty =
                AbstractDaoFactory.databaseProperties.getProperty(key);
        if (databaseProperty == null) {
            throw new AppException(PROPERTY_EMPTY_OR_CONTENT_ERROR);
        }
        return databaseProperty;
    }

    AbstractDaoFactory(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getConnection() throws AppException {
        if (this.connection == null) {
            throw new AppException(GET_CONNECTION_ERROR);
        }
        return this.connection;
    }

    @Override
    public void beginTransaction(int transactionIsolation)
            throws AppException {
        if (this.connection == null) {
            throw new AppException(GET_CONNECTION_ERROR);
        }
        try {
            this.connection.setAutoCommit(false);
            this.connection.setTransactionIsolation(transactionIsolation);
        } catch (SQLException e) {
            throw new AppException(BEGIN_TRANSACTION_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

    @Override
    public void commitTransaction() throws AppException {
        if (this.connection == null) {
            throw new AppException(GET_CONNECTION_ERROR);
        }
        try {
            this.connection.commit();
        } catch (SQLException e) {
            throw new AppException(COMMIT_TRANSACTION_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

    @Override
    public void rollbackTransaction() throws AppException {
        if (this.connection == null) {
            throw new AppException(GET_CONNECTION_ERROR);
        }
        try {
            this.connection.rollback();
        } catch (SQLException e) {
            throw new AppException(ROLLBACK_TRANSACTION_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

    @Override
    public void close() throws AppException {
        try {
            if (this.connection != null) {
                this.connection.close();
                this.connection = null;
            }
        } catch (SQLException e) {
            throw new AppException(CLOSE_CONNECTION_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

}