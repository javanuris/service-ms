package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.*;
import com.epam.java.rt.lab.entity.access.Remember;
import com.epam.java.rt.lab.entity.access.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * category-ms
 */
public class RememberDao extends JdbcDao {

    public RememberDao(Connection connection) {
        super(connection);
    }

    @Override
    Sql getSqlCreate(DaoParameter daoParameter) throws DaoException {
        Remember remember = (Remember) daoParameter.getEntity();
        return Sql
                .insert(remember)
                .values(
                        new Insert.InsertValue(Remember.Property.USER_ID, remember.getUser().getId()),
                        new Insert.InsertValue(Remember.Property.COOKIE_NAME, remember.getCookieName()),
                        new Insert.InsertValue(Remember.Property.COOKIE_VALUE, remember.getCookieValue()),
                        new Insert.InsertValue(Remember.Property.VALID, remember.getValid())
                );
    }

    @Override
    Sql getSqlRead(DaoParameter daoParameter) throws DaoException {
        return Sql
                .select(Remember.class)
                .where(daoParameter.getWherePredicate())
                .orderBy(daoParameter.getOrderByCriteriaArray())
                .limit(daoParameter.getLimitOffset(), daoParameter.getLimitCount());
    }

    @Override
    Sql getSqlUpdate(DaoParameter daoParameter) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    Sql getSqlDelete(DaoParameter daoParameter) throws DaoException {
        return Sql
                .delete(Remember.class)
                .where(daoParameter.getWherePredicate());
    }

    @Override
    Sql getSqlCount(DaoParameter daoParameter) throws DaoException {
        return null;
    }

    @Override
    <T> List<T> getEntity(ResultSet resultSet, Sql sql) throws DaoException {
        Select select = (Select) sql;
        String rememberTableName = Sql.getProperty(Remember.class.getName());
        String userTableName = Sql.getProperty(User.class.getName());
        List<Remember> rememberList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int columnIndex = 0;
                Remember remember = null;
                for (Column column : select) {
                    columnIndex++;
                    if (rememberTableName.equals(column.getTableName())) {
                        if (remember == null) remember = new Remember();
                        setEntityProperty(column.getTableName(), column.getColumnName(), remember, resultSet.getObject(columnIndex));
                    } else {
                        if (userTableName.equals(column.getTableName())) {
                            remember.setUser(new UserDao(getConnection())
                                    .getUser((Long) resultSet.getObject(columnIndex)));
                        }
                    }
                }
                rememberList.add(remember);
            }
            return (List<T>) rememberList;
        } catch (SQLException e) {
            throw new DaoException("exception.dao.jdbc.remember.get-entity", e.getCause());
        }
    }

}
