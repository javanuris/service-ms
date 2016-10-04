package com.epam.java.rt.lab.dao.factory;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

/**
 * category-ms
 */
public class JdbcDaoFactory extends AbstractDaoFactory {

    private static final Logger logger = LoggerFactory.getLogger(JdbcDaoFactory.class);

    public JdbcDaoFactory(Connection connection) {
        super(connection);
    }

    @Override
    public Dao createDao(String daoShortName) throws DaoException {
        try {
            Constructor<?> daoClassConstructor = getDaoConstructor(daoShortName);
            logger.debug("daoClassConstructor: {}", daoClassConstructor.getName());
            return (Dao) daoClassConstructor.newInstance(getConnection());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new DaoException("exception.dao.factory.jdbc.new-instance", e.getCause());
        } catch (DaoException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static Constructor<?> getDaoConstructor(String daoShortName) throws DaoException {
        try {
            Class daoClass = Class.forName
                    (JdbcDaoFactory.getDatabaseProperty("management-system.package")
                            .concat(".jdbc.").concat(daoShortName).concat("Dao"));
            return daoClass.getConstructor(Connection.class);
        } catch (NoSuchMethodException | DaoException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.jdbc.getSql-dao-constructor", e.getCause());
        }
    }

}
