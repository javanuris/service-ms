package com.epam.java.rt.lab.dao;

import java.util.List;

/**
 * service-ms
 */
public interface Dao {

    int create(DaoParameter daoParameter);

    <T> List<T> read(DaoParameter daoParameter) throws DaoException;

    int update(DaoParameter daoParameter);

    int delete(DaoParameter daoParameter);

}
