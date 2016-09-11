package com.epam.java.rt.lab.dao;

import com.epam.java.rt.lab.entity.rbac.User;

import java.util.List;

/**
 * service-ms
 */
public interface Dao {

    <T> T getFirst(T entity, String fieldNames) throws DaoException;

    <T> T getFirst(T entity, String fieldNames, String columnNames) throws DaoException;

    <T> List<T> getAll(T entity, String fieldNames) throws DaoException;

    <T> List<T> getAll(T entity, String fieldNames, String columnNames) throws DaoException;

    <T> int update(T entity, String fieldNames, String setNames) throws DaoException;

}
