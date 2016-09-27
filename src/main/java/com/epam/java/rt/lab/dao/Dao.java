package com.epam.java.rt.lab.dao;

/**
 * service-ms
 */
public interface Dao {

    int create(DaoParameter daoParameter);

    <T> T read(DaoParameter daoParameter) throws DaoException;

    int update(DaoParameter daoParameter);

    int delete(DaoParameter daoParameter);

}
