package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.Column;
import com.epam.java.rt.lab.dao.sql.Select;
import com.epam.java.rt.lab.dao.sql.Sql;
import com.epam.java.rt.lab.entity.rbac.Login;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * service-ms
 */
public class LoginJdbcDao extends JdbcDao {

    public LoginJdbcDao(Connection connection) {
        super(connection);
    }

    @Override
    Sql getSqlCreate(DaoParameter daoParameter) {
        return null;
    }

    @Override
    Sql getSqlRead(DaoParameter daoParameter) throws DaoException {
        return Sql
                .select(Login.class)
                .where(daoParameter.getWherePredicate())
                .orderBy(daoParameter.getOrderByCriteriaArray())
                .limit(daoParameter.getLimitOffset(), daoParameter.getLimitCount());
    }

    @Override
    Sql getSqlUpdate(DaoParameter daoParameter) {
        return null;
    }

    @Override
    Sql getSqlDelete(DaoParameter daoParameter) {
        return null;
    }

    @Override
    <T> T getEntity(ResultSet resultSet, Sql sql) throws DaoException {
        String entityTable = Sql.getProperty("Login");
        Select select = (Select) sql;
        List<Login> loginList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int columnIndex = 0;
                Login login = new Login();
                for (Column column : select) {
                    columnIndex++;
                    if (column.getTableName().equals(entityTable)) {
                        switch (column.getColumnName()) {
                            case "id":
                                login.setId(resultSet.getLong(columnIndex));
                                break;
                            case "email":
                                login.setEmail((String) resultSet.getObject(columnIndex));
                                break;
                            case "password":
                                login.setPassword((String) resultSet.getObject(columnIndex));
                                break;
                            case "attempt_left":
                                login.setAttemptLeft((Integer) resultSet.getObject(columnIndex));
                                break;
                            case "status":
                                login.setStatus((Integer) resultSet.getObject(columnIndex));
                                break;
                        }
                    } else {
                        // there are no referenced entities for login
                    }
                }
            }
            return null;
        } catch (SQLException e) {
            throw new DaoException("exception.dao.jdbc.login.get-entity", e.getCause());
        }
    }

}
