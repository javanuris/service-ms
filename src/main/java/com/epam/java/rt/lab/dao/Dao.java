package com.epam.java.rt.lab.dao;

import java.sql.Connection;

/**
 * service-ms
 */
public interface Dao<T> {
    //CRUD
    <E> T getBy(Connection connection, String columnName, E value) throws DaoException;

}
