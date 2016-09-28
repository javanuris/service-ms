package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.factory.AbstractDaoFactory;
import com.epam.java.rt.lab.dao.factory.DaoFactory;

/**
 * service-ms
 */
abstract class BaseService implements AutoCloseable{
    DaoFactory daoFactory;

    BaseService() throws ServiceException {
        try {
            daoFactory = AbstractDaoFactory.createDaoFactory();
        } catch (DaoException e) {
            throw new ServiceException("exception.service.base.dao", e.getCause());
        }
    }

    public void close() throws ServiceException {
        try {
            daoFactory.close();
        } catch (DaoException e) {
            throw new ServiceException("exception.service.base.close.dao", e.getCause());
        }
    }

    Dao dao(String name) throws ServiceException {
        try {
            return daoFactory.createDao(name);
        } catch (DaoException e) {
            throw new ServiceException("exception.service.base.dao.dao", e.getCause());
        }
    }

}
