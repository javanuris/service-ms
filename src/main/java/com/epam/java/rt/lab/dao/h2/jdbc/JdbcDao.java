package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.DaoStatement;
import com.epam.java.rt.lab.dao.factory.AbstractDaoFactory;
import com.epam.java.rt.lab.dao.h2.JdbcParameter;
import com.epam.java.rt.lab.entity.EntityProperty;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * service-ms
 */
public abstract class JdbcDao implements Dao {

    static final String SELECT = "SELECT ";
    static final String INSERT = "INSERT INTO ";
    static final String UPDATE = "UPDATE ";
    static final String DELETE = "DELETE ";
    static final String FROM = "FROM ";
    static final String JOIN = "JOIN ";
    static final String DELIMITER = " ";
    static final String END_SIGN = ";";

    static final String PROPERTY_COLUMNS = ".columns";

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
        try (
                DaoStatement statement = new DaoStatement(
                        this.connection,
                        getQueryRead(daoParameter),
                        Statement.NO_GENERATED_KEYS
                );
        ) {
            return getEntity(statement.executeQuery(), daoParameter);
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

    abstract JdbcParameter getQueryCreate(DaoParameter daoParameter);

    abstract JdbcParameter getQueryRead(DaoParameter daoParameter) throws DaoException;

    abstract JdbcParameter getQueryUpdate(DaoParameter daoParameter);

    abstract JdbcParameter getQueryDelete(DaoParameter daoParameter);

    abstract <T> T getEntity(ResultSet resultSet, DaoParameter daoParameter);

    void fitParameterToDatabase(DaoParameter daoParameter)
            throws DaoException {
        if (daoParameter.getValue() != null) {
            for (JdbcParameter.QueryValue queryValue : daoParameter.getValue())
                fitQueryValueToDatabase(queryValue);
        }
        if (daoParameter.getWhere() != null) {
            for (JdbcParameter.QueryValue queryValue : daoParameter.getWhere())
                fitQueryValueToDatabase(queryValue);
        }
        if (daoParameter.getOrder() != null) {
            daoParameter.getOrder().clearColumnList();
            for (EntityProperty entityProperty : daoParameter.getOrder())
                daoParameter.getOrder().addColumn(getColumnFullName(entityProperty));
        }
    }

    private void fitQueryValueToDatabase(JdbcParameter.QueryValue queryValue)
            throws DaoException {
        String[] split =
                getColumnFullName(queryValue.getEntityProperty()).split("\\.");
        queryValue.setTableName(split[0]);
        queryValue.setColumnName(split[1]);
    }

    private String getColumnFullName(EntityProperty entityProperty)
            throws DaoException {
        String columnFullName = AbstractDaoFactory.getDatabaseProperty(
                entityProperty.getClass().getSuperclass().getName()
                .concat(entityProperty.toString())
        );
        if (columnFullName == null)
            throw new DaoException("exception.dao.jdbc.fit-query-value-to-database");
        return columnFullName;
    }

}
