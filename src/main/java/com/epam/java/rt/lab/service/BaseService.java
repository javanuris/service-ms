package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.factory.AbstractDaoFactory;
import com.epam.java.rt.lab.dao.factory.DaoFactory;

/**
 * category-ms
 */
abstract class BaseService implements AutoCloseable{

    DaoFactory daoFactory;

    public void close() throws ServiceException {
        try {
            if (daoFactory != null) daoFactory.close();
        } catch (DaoException e) {
            throw new ServiceException("exception.service.base.close.dao", e.getCause());
        }
    }

    Dao dao(String name) throws ServiceException {
        try {
            if (daoFactory == null) daoFactory = AbstractDaoFactory.createDaoFactory();
            return daoFactory.createDao(name);
        } catch (DaoException e) {
            throw new ServiceException("exception.service.base.dao.dao", e.getCause());
        }
    }

}
