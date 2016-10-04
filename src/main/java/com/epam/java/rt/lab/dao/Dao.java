package com.epam.java.rt.lab.dao;

import java.util.List;

/**
 * category-ms
 */
public interface Dao {

    Long create(DaoParameter daoParameter) throws DaoException;

    <T> List<T> read(DaoParameter daoParameter) throws DaoException;

    int update(DaoParameter daoParameter) throws DaoException;

    int delete(DaoParameter daoParameter) throws DaoException;

    Long count(DaoParameter daoParameter) throws DaoException;

}
