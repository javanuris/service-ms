package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.DaoStatement;
import com.epam.java.rt.lab.dao.sql.Sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * service-ms
 */
public abstract class JdbcDao implements Dao {

    private Connection connection = null;

    public JdbcDao(Connection connection) {
        this.connection = connection;
    }

    Connection getConnection() {
        return this.connection;
    }

    @Override
    public int create(DaoParameter daoParameter) {
        return 0;
    }

    @Override
    public <T> T read(DaoParameter daoParameter) throws DaoException {
        Sql sql = getSqlRead(daoParameter);
        try (DaoStatement statement = new DaoStatement(this.connection, sql, Statement.NO_GENERATED_KEYS)) {
            return getEntity(statement.executeQuery(), sql);
        } catch (NoSuchMethodException e) {
            throw new DaoException("exception.dao.jdbc.read.statement-method", e.getCause());
        } catch (SQLException e) {
            throw new DaoException("exception.dao.jdbc.read.sql", e.getCause());
        } catch (Exception e) {
            throw new DaoException("exception.dao.jdbc.read.close", e.getCause());
        }
    }

    @Override
    public int update(DaoParameter daoParameter) {
        return 0;
    }

    @Override
    public int delete(DaoParameter daoParameter) {
        return 0;
    }

    abstract Sql getSqlCreate(DaoParameter daoParameter);

    abstract Sql getSqlRead(DaoParameter daoParameter) throws DaoException;

    abstract Sql getSqlUpdate(DaoParameter daoParameter);

    abstract Sql getSqlDelete(DaoParameter daoParameter);

    abstract <T> T getEntity(ResultSet resultSet, Sql sql) throws DaoException;

}
