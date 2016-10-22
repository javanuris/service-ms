package com.epam.java.rt.lab.dao;

import com.epam.java.rt.lab.exception.AppException;

import java.util.List;

public interface Dao {

    Long create(DaoParameter daoParameter) throws AppException;

    <T> List<T> read(DaoParameter daoParameter) throws AppException;

    int update(DaoParameter daoParameter) throws AppException;

    int delete(DaoParameter daoParameter) throws AppException;

    Long count(DaoParameter daoParameter) throws AppException;

}
