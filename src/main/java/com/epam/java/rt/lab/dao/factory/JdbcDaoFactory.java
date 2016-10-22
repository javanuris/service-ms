package com.epam.java.rt.lab.dao.factory;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.exception.AppException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

import static com.epam.java.rt.lab.dao.DaoExceptionCode.*;
import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;
import static com.epam.java.rt.lab.util.PropertyManager.POINT;

public class JdbcDaoFactory extends AbstractDaoFactory {

    private static final String DB_MANAGEMENT_SYSTEM_PACKAGE =
            "management-system.package";
    private static final String DB_DAO_SUFFIX = "Dao";

    public JdbcDaoFactory(Connection connection) {
        super(connection);
    }

    @Override
    public Dao createDao(String daoShortName) throws AppException {
        if (daoShortName == null) throw new AppException(NULL_NOT_ALLOWED);
        try {
            Class daoClass = Class.forName(JdbcDaoFactory.
                    getDatabaseProperty(DB_MANAGEMENT_SYSTEM_PACKAGE)
                    + POINT + daoShortName + DB_DAO_SUFFIX);
            Constructor<?> daoClassConstructor =
                    daoClass.getConstructor(Connection.class);
            return (Dao) daoClassConstructor.newInstance(getConnection());
        } catch (InstantiationException | IllegalAccessException
                | InvocationTargetException e) {
            throw new AppException(DAO_CLASS_INSTANTIATE_ERROR,
                    e.getMessage(), e.getCause());
        } catch (ClassNotFoundException e) {
            throw new AppException(DAO_CLASS_NOT_FOUND,
                    e.getMessage(), e.getCause());
        } catch (NoSuchMethodException e) {
            throw new AppException(DAO_CONSTRUCTOR_NOT_FOUND,
                    e.getMessage(), e.getCause());
        }
    }

}
