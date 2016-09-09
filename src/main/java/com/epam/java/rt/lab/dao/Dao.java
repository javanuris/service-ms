package com.epam.java.rt.lab.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

/**
 * service-ms
 */
public interface Dao {

    void setConnection(Connection connection);

    Connection getConnection();

    void close() throws DaoException;

    Dao query(String... fieldNameArray) throws DaoException;

    <T> Dao filter(String fieldName, T fieldValue) throws DaoException;

    Dao join(String tableName) throws DaoException;

    Dao join(String tableName, String alias) throws DaoException;

    Dao on(String fieldName, String compareFieldName) throws DaoException;

    Dao execute() throws DaoException;

    <T> T first() throws DaoException;

    <T> List<T> all() throws DaoException;

    ResultSet getResultSet() throws DaoException;

    Dao update(String... fieldNameArray) throws DaoException;

    <T> Dao set(T entity) throws DaoException;

    int getLastUpdateCount() throws DaoException;
}
