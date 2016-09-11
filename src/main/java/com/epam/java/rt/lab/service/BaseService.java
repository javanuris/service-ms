package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.factory.AbstractDaoFactory;
import com.epam.java.rt.lab.dao.factory.DaoFactory;

/**
 * service-ms
 */
abstract class BaseService {
    DaoFactory daoFactory;

    BaseService() throws ConnectionException, DaoException {
        daoFactory = AbstractDaoFactory.createDaoFactory();
    }

}
