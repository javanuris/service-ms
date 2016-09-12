package com.epam.java.rt.lab.dao.factory;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.Dao;
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

    public JdbcDaoFactory(Connection connection) {
        super(connection);
    }

    @Override
    public Dao createDao(String daoShortName) throws DaoException {
        try {
            logger.debug("createDao: {}", daoShortName);
            Class daoClass = Class.forName
                    (JdbcDaoFactory.getDatabaseProperty("management-system.package")
                            .concat(".").concat(daoShortName).concat("JdbcDao"));
            logger.debug("daoClass: {}", daoClass.getName());
            Constructor<?> daoClassConstructor = daoClass.getConstructor(Connection.class);
            logger.debug("daoClassConstructor: {}", daoClassConstructor.getName());
            Dao dao = (Dao) daoClassConstructor.newInstance(getConnection());
            logger.debug("dao: {}", dao.getClass().getName());
            return dao;
        } catch (ClassNotFoundException e) {
            throw new DaoException("exception.dao.factory.jdbc.for-name", e.getCause());
        } catch (NoSuchMethodException e) {
            throw new DaoException("exception.dao.factory.jdbc.get-constructor", e.getCause());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new DaoException("exception.dao.factory.jdbc.new-instance", e.getCause());
        } catch (DaoException e) {
            e.printStackTrace();
            throw new DaoException();
        }
    }

}
