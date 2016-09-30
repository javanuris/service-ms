package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.Column;
import com.epam.java.rt.lab.dao.sql.Select;
import com.epam.java.rt.lab.dao.sql.Sql;
import com.epam.java.rt.lab.dao.sql.Update;
import com.epam.java.rt.lab.entity.rbac.Login;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * service-ms
 */
public class LoginDao extends JdbcDao {

    public LoginDao(Connection connection) {
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
    Sql getSqlUpdate(DaoParameter daoParameter) throws DaoException {
        return Sql
                .update(Login.class)
                .set(daoParameter.getSetValueArray())
                .where(daoParameter.getWherePredicate());
    }

    @Override
    Sql getSqlDelete(DaoParameter daoParameter) {
        return null;
    }

    @Override
    <T> List<T> getEntity(ResultSet resultSet, Sql sql) throws DaoException {
        Select select = (Select) sql;
        String loginTableName = Sql.getProperty(Login.class.getName());
        List<Login> loginList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int columnIndex = 0;
                Login login = null;
                for (Column column : select) {
                    columnIndex++;
                    if (loginTableName.equals(column.getTableName())) {
                        if (login == null) login = new Login();
                        setEntityProperty(column.getTableName(), column.getColumnName(), login, resultSet.getObject(columnIndex));
                    } else {
                        // another entity
                    }
                }
                loginList.add(login);
            }
            return (List<T>) loginList;
        } catch (SQLException e) {
            throw new DaoException("exception.dao.jdbc.login.get-entity", e.getCause());
        }
    }


}
