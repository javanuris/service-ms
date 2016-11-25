package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.Column;
import com.epam.java.rt.lab.dao.sql.Insert.InsertValue;
import com.epam.java.rt.lab.dao.sql.Select;
import com.epam.java.rt.lab.dao.sql.Sql;
import com.epam.java.rt.lab.entity.access.Login;
import com.epam.java.rt.lab.entity.access.Restore;
import com.epam.java.rt.lab.entity.access.RestoreProperty;
import com.epam.java.rt.lab.exception.AppException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.epam.java.rt.lab.dao.DaoExceptionCode.SQL_OPERATION_ERROR;
import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;

public class RestoreDao extends JdbcDao {

    public RestoreDao(Connection connection) throws AppException {
        super(connection);
    }

    @Override
    Sql getSqlCreate(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        Restore restore = (Restore) daoParameter.getEntity();
        return Sql.insert(restore).values(
                new InsertValue(RestoreProperty.LOGIN_ID,
                        restore.getLogin().getId()),
                new InsertValue(RestoreProperty.CODE, restore.getCode()),
                new InsertValue(RestoreProperty.COOKIE_NAME,
                        restore.getCookieName()),
                new InsertValue(RestoreProperty.COOKIE_VALUE,
                        restore.getCookieValue()),
                new InsertValue(RestoreProperty.VALID, restore.getValid()));
    }

    @Override
    Sql getSqlRead(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        return Sql.select(Restore.class).
                where(daoParameter.getWherePredicate()).
                orderBy(daoParameter.getOrderByCriteriaArray()).
                limit(daoParameter.getLimitOffset(),
                        daoParameter.getLimitCount());
    }

    @Override
    Sql getSqlUpdate(DaoParameter daoParameter) throws AppException {
        throw new UnsupportedOperationException();
    }

    @Override
    Sql getSqlDelete(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        return Sql.delete(Restore.class).
                where(daoParameter.getWherePredicate());
    }

    @Override
    Sql getSqlCount(DaoParameter daoParameter) throws AppException {
        throw new UnsupportedOperationException();
    }

    @Override
    <T> List<T> getEntity(ResultSet resultSet, Sql sql) throws AppException {
        if (resultSet == null || sql == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
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
                        setEntityProperty(column.getTableName(),
                                column.getColumnName(), restore,
                                resultSet.getObject(columnIndex));
                    } else {
                        if (loginTableName.equals(column.getTableName())) {
                            restore.setLogin((new LoginDao(getConnection())
                                    .getLogin((Long) resultSet.
                                            getObject(columnIndex))));
                        }
                    }
                }
                restoreList.add(restore);
            }
            return (List<T>) restoreList;
        } catch (SQLException e) {
            throw new AppException(SQL_OPERATION_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

}