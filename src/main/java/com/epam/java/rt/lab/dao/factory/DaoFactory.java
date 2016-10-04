package com.epam.java.rt.lab.dao.factory;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;

import java.sql.Connection;

/**
 * category-ms
 */
public interface DaoFactory {

    Connection getConnection() throws DaoException;

    void beginTransaction(int transactionIsolation) throws DaoException;

    void commitTransaction() throws DaoException;

    void rollbackTransaction() throws DaoException;

    Dao createDao(String daoShortName) throws DaoException;

    void close() throws DaoException;
}
