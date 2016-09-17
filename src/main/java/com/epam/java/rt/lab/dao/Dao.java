package com.epam.java.rt.lab.dao;

import com.epam.java.rt.lab.dao.h2.JdbcDao;
import com.epam.java.rt.lab.entity.rbac.User;

import java.util.List;

/**
 * service-ms
 */
public interface Dao {

    <T> T getFirst(T entity, String fieldNames, String order) throws DaoException;

    <T> T getFirst(T entity, String fieldNames, String columnNames, String order) throws DaoException;

    <T> List<T> getAll(String order) throws DaoException;

    <T> List<T> getAll(T entity, String fieldNames, String order) throws DaoException;

    <T> List<T> getAll(T entity, String fieldNames, String columnNames, String order) throws DaoException;

    <T> List<T> getAll(T entity, String fieldNames, String columnNames, String order, Long offset, Long count) throws DaoException;

    Long getSelectCount();

    <T> int update(T entity, String fieldNames, String setNames) throws DaoException;

    <T> Object getRelEntity(T entity, String relEntityName) throws DaoException;

    <T> int setRelEntity(T entity, String relEntityName, Object relEntity) throws DaoException;

    <T> int removeRelEntity(T entity, String relEntityName) throws DaoException;
}
