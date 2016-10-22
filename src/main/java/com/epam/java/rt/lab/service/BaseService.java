package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.factory.AbstractDaoFactory;
import com.epam.java.rt.lab.dao.factory.DaoFactory;
import com.epam.java.rt.lab.exception.AppException;

import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;

abstract class BaseService implements AutoCloseable{

    DaoFactory daoFactory;

    Dao dao(String name) throws AppException {
        if (name == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        if (daoFactory == null) {
            daoFactory = AbstractDaoFactory.createDaoFactory();
        }
        return daoFactory.createDao(name);
    }

    public void close() throws AppException {
        if (daoFactory != null) daoFactory.close();
    }

}