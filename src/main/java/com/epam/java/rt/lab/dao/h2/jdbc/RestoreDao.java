package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.Column;
import com.epam.java.rt.lab.dao.sql.Insert;
import com.epam.java.rt.lab.dao.sql.Select;
import com.epam.java.rt.lab.dao.sql.Sql;
import com.epam.java.rt.lab.entity.rbac.Restore;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * service-ms
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
    <T> List<T> getEntity(ResultSet resultSet, Sql sql) throws DaoException {
        Select select = (Select) sql;
        String restoreTableName = Sql.getProperty(Restore.class.getName());
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
                        // another entity
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
