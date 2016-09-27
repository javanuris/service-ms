package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.h2.JdbcParameter;
import com.epam.java.rt.lab.dao.sql.Sql;
import com.epam.java.rt.lab.dao.sql.Where.Predicate;
import com.epam.java.rt.lab.entity.rbac.Login;

import java.sql.Connection;
import java.sql.ResultSet;

import static com.epam.java.rt.lab.dao.sql.Where.Predicate.PredicateOperator.MORE;

/**
 * service-ms
 */
public class LoginJdbcDao extends JdbcDao {

    public LoginJdbcDao(Connection connection) {
        super(connection);
    }

    @Override
    JdbcParameter getQueryCreate(DaoParameter daoParameter) {
        return null;
    }

    @Override
    JdbcParameter getQueryRead(DaoParameter daoParameter)
            throws DaoException {
        return new JdbcParameter(
                Sql.select(Login.class).where(Predicate.get(Login.Property.ID, MORE, 100)).toString(),
                null
        );
        daoParameter.getWhere()
    }

    @Override
    JdbcParameter getQueryUpdate(DaoParameter daoParameter) {
        return null;
    }

    @Override
    JdbcParameter getQueryDelete(DaoParameter daoParameter) {
        return null;
    }

    @Override
    <T> T getEntity(ResultSet resultSet, DaoParameter daoParameter) {
        return null;
    }

}
