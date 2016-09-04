package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.factory.DaoFactory;

import java.sql.Connection;

/**
 * service-ms
 */
public abstract class BaseService {
    private DaoFactory factory;

    public BaseService() throws DaoException {
        factory = DaoFactory.getDaoFactory();
    }

    public Dao getJdbcDao(Connection connection) throws DaoException {
        String serviceName = this.getClass().getSimpleName();
        return factory.getJdbcDao(serviceName.substring(0, serviceName.length() - 7), connection);
    }

    public DaoFactory getFactory() {
        return factory;
    }

}
