package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.factory.AbstractDaoFactory;
import com.epam.java.rt.lab.dao.factory.DaoFactory;

/**
 * service-ms
 */
abstract class BaseService implements AutoCloseable{
    DaoFactory daoFactory;

    BaseService() throws ConnectionException, DaoException {
        daoFactory = AbstractDaoFactory.createDaoFactory();
    }

    public void close() throws DaoException {
        daoFactory.close();
    }

}
