package com.epam.java.rt.lab.dao.factory;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.exception.AppException;

import java.sql.Connection;

public interface DaoFactory {

    Connection getConnection() throws AppException;

    void beginTransaction(int transactionIsolation) throws AppException;

    void commitTransaction() throws AppException;

    void rollbackTransaction() throws AppException;

    Dao createDao(String daoShortName) throws AppException;

    void close() throws AppException;
}
