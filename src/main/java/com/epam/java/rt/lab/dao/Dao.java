package com.epam.java.rt.lab.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * service-ms
 */
public interface Dao {

    void close() throws DaoException;

    <T> T first() throws SQLException;

    ResultSet getResultSet() throws SQLException;

    //CRUD
    <T> Dao find(Connection connection, String columnName, T value) throws DaoException;

}
