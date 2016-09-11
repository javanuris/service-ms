package com.epam.java.rt.lab.dao;

import java.util.List;

/**
 * service-ms
 */
public interface Dao_ {

    <T> T getFirst(T entity, String fieldNames) throws DaoException;

    <T> T getFirst(T entity, String fieldNames, String columnNames) throws DaoException;

    <T> List<T> getAll(T entity, String fieldNames) throws DaoException;

    <T> List<T> getAll(T entity, String fieldNames, String columnNames) throws DaoException;



}
