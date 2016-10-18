package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.factory.AbstractDaoFactory;
import com.epam.java.rt.lab.dao.factory.DaoFactory;
import com.epam.java.rt.lab.exception.AppException;

import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;
import static com.epam.java.rt.lab.service.ServiceExceptionCode.CREATE_DAO_FACTORY_OR_DAO_ERROR;
import static com.epam.java.rt.lab.service.ServiceExceptionCode.DAO_CLOSE_ERROR;

abstract class BaseService implements AutoCloseable{

    DaoFactory daoFactory;

    Dao dao(String name) throws AppException {
        if (name == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        try {
            if (daoFactory == null) {
                daoFactory = AbstractDaoFactory.createDaoFactory();
            }
            return daoFactory.createDao(name);
        } catch (DaoException e) {
            throw new AppException(CREATE_DAO_FACTORY_OR_DAO_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

    public void close() throws AppException {
        try {
            if (daoFactory != null) daoFactory.close();
        } catch (DaoException e) {
            throw new AppException(DAO_CLOSE_ERROR, e.getCause());
        }
    }

}
