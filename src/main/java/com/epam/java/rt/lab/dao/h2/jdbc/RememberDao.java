package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.Column;
import com.epam.java.rt.lab.dao.sql.Insert.InsertValue;
import com.epam.java.rt.lab.dao.sql.Select;
import com.epam.java.rt.lab.dao.sql.Sql;
import com.epam.java.rt.lab.entity.access.Remember;
import com.epam.java.rt.lab.entity.access.RememberProperty;
import com.epam.java.rt.lab.entity.access.User;
import com.epam.java.rt.lab.exception.AppException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.epam.java.rt.lab.dao.DaoExceptionCode.SQL_OPERATION_ERROR;
import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;

public class RememberDao extends JdbcDao {

    public RememberDao(Connection connection) throws AppException {
        super(connection);
    }

    @Override
    Sql getSqlCreate(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        Remember remember = (Remember) daoParameter.getEntity();
        return Sql.insert(remember).values(
                new InsertValue(RememberProperty.USER_ID,
                        remember.getUser().getId()),
                new InsertValue(RememberProperty.COOKIE_NAME,
                        remember.getCookieName()),
                new InsertValue(RememberProperty.COOKIE_VALUE,
                        remember.getCookieValue()),
                new InsertValue(RememberProperty.VALID, remember.getValid()));
    }

    @Override
    Sql getSqlRead(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        return Sql.select(Remember.class).
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
        return Sql.delete(Remember.class).
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
                        setEntityProperty(column.getTableName(),
                                column.getColumnName(), remember,
                                resultSet.getObject(columnIndex));
                    } else {
                        if (userTableName.equals(column.getTableName())) {
                            remember.setUser(new UserDao(getConnection())
                                    .getUser((Long) resultSet.
                                            getObject(columnIndex)));
                        }
                    }
                }
                rememberList.add(remember);
            }
            return (List<T>) rememberList;
        } catch (SQLException e) {
            throw new AppException(SQL_OPERATION_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

}