package com.epam.java.rt.lab.dao.factory;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.Dao_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

/**
 * service-ms
 */
public class JdbcDaoFactory extends AbstractDaoFactory {
    private static final Logger logger = LoggerFactory.getLogger(JdbcDaoFactory.class);

    @Override
    public Dao_ createDao(String daoShortName) throws DaoException {
        try {
            Class daoClass = Class.forName(daoShortName.concat("JdbcDao"));
            Constructor<Dao_> daoClassConstructor = daoClass.getConstructor(Connection.class);
            return daoClassConstructor.newInstance(getConnection());
        } catch (ClassNotFoundException e) {
            throw new DaoException("exception.dao.factory.jdbc.for-name", e.getCause());
        } catch (NoSuchMethodException e) {
            throw new DaoException("exception.dao.factory.jdbc.get-constructor", e.getCause());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new DaoException("exception.dao.factory.jdbc.new-instance", e.getCause());
        }
    }

}
