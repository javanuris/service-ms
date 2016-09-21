package com.epam.java.rt.lab.dao;

import java.util.List;
import java.util.Map;

/**
 * service-ms
 */
public interface Dao {

    enum ArgumentType {
        QUERY_TYPE,
        FUNC_NAME,
        LIMIT_OFFSET,
        LIMIT_COUNT,
        ORDER_COLUMNS,
        ORDER_TYPE,
        FROM_TABLE,
        JOIN_TABLES,
        RESULT_FIELDS,
        SELECT_COLUMN_LIST,
        SET_FIELD_LIST,
        WHERE_LIST,
        CUSTOM
    }

    enum QueryType {
        CREATE,
        READ,
        UPDATE,
        DELETE,
        FUNC
    }

    enum OrderType {
        ASC,
        DESC
    }

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

    Long count(Argument argument) throws DaoException;

    <T> List<T> getAll(Argument argument) throws DaoException;

    <T> T getFirst(Argument argument) throws DaoException;

}
