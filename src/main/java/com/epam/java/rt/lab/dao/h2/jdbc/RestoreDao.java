package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.*;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.Restore;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * category-ms
 */
public class RestoreDao extends JdbcDao {

    public RestoreDao(Connection connection) {
        super(connection);
    }

    @Override
    Sql getSqlCreate(DaoParameter daoParameter) throws DaoException {
        Restore restore = (Restore) daoParameter.getEntity();
        return Sql
                .insert(restore)
                .values(
                        new Insert.InsertValue(Restore.Property.LOGIN_ID, restore.getLogin().getId()),
                        new Insert.InsertValue(Restore.Property.CODE, restore.getCode()),
                        new Insert.InsertValue(Restore.Property.COOKIE_NAME, restore.getCookieName()),
                        new Insert.InsertValue(Restore.Property.COOKIE_VALUE, restore.getCookieValue()),
                        new Insert.InsertValue(Restore.Property.VALID, restore.getValid())
                );
    }

    @Override
    Sql getSqlRead(DaoParameter daoParameter) throws DaoException {
        return Sql
                .select(Restore.class)
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
                .delete(Restore.class)
                .where(daoParameter.getWherePredicate());
    }

    @Override
    Sql getSqlCount(DaoParameter daoParameter) throws DaoException {
        return null;
    }

    @Override
    <T> List<T> getEntity(ResultSet resultSet, Sql sql) throws DaoException {
        Select select = (Select) sql;
        String restoreTableName = Sql.getProperty(Restore.class.getName());
        String loginTableName = Sql.getProperty(Login.class.getName());
        List<Restore> restoreList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int columnIndex = 0;
                Restore restore = null;
                for (Column column : select) {
                    columnIndex++;
                    if (restoreTableName.equals(column.getTableName())) {
                        if (restore == null) restore = new Restore();
                        setEntityProperty(column.getTableName(), column.getColumnName(), restore, resultSet.getObject(columnIndex));
                    } else {
                        if (loginTableName.equals(column.getTableName())) {
                            restore.setLogin((new LoginDao(getConnection())
                                    .getLogin((Long) resultSet.getObject(columnIndex))));
                        }
                    }
                }
                restoreList.add(restore);
            }
            return (List<T>) restoreList;
        } catch (SQLException e) {
            throw new DaoException("exception.dao.jdbc.restore.get-entity", e.getCause());
        }
    }


}
