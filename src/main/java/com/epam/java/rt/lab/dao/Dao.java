package com.epam.java.rt.lab.dao;

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

    public <T> Long count(T entity, String columnNames) throws DaoException;

    <T> int update(T entity, String fieldNames, String setNames) throws DaoException;

    <T> Object getRelEntity(T entity, String relEntityName) throws DaoException;

    <T> int setRelEntity(T entity, String relEntityName, Object relEntity) throws DaoException;

    <T> int removeRelEntity(T entity, String relEntityName) throws DaoException;

    <T> int create(T entity) throws DaoException;

    Long count(Parameter parameter) throws DaoException;

    <T> List<T> getAll(Parameter parameter) throws DaoException;

    <T> T getFirst(Parameter parameter) throws DaoException;

}
