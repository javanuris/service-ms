package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.Column;
import com.epam.java.rt.lab.dao.sql.Insert.InsertValue;
import com.epam.java.rt.lab.dao.sql.Select;
import com.epam.java.rt.lab.dao.sql.Sql;
import com.epam.java.rt.lab.dao.sql.Where.Predicate;
import com.epam.java.rt.lab.dao.sql.WherePredicateOperator;
import com.epam.java.rt.lab.entity.access.Login;
import com.epam.java.rt.lab.entity.access.LoginProperty;
import com.epam.java.rt.lab.exception.AppException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.epam.java.rt.lab.dao.DaoExceptionCode.SQL_OPERATION_ERROR;
import static com.epam.java.rt.lab.entity.access.Login.NULL_LOGIN;
import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;

public class LoginDao extends JdbcDao {

    public LoginDao(Connection connection) throws AppException {
        super(connection);
    }

    @Override
    Sql getSqlCreate(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        Login login = (Login) daoParameter.getEntity();
        return Sql.insert(login).values(
                new InsertValue(LoginProperty.EMAIL, login.getEmail()),
                new InsertValue(LoginProperty.SALT, login.getSalt()),
                new InsertValue(LoginProperty.PASSWORD, login.getPassword()),
                new InsertValue(LoginProperty.ATTEMPT_LEFT,
                        login.getAttemptLeft()),
                new InsertValue(LoginProperty.STATUS, login.getStatus()));
    }

    @Override
    Sql getSqlRead(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        return Sql.select(Login.class).
                where(daoParameter.getWherePredicate()).
                orderBy(daoParameter.getOrderByCriteriaArray()).
                limit(daoParameter.getLimitOffset(),
                        daoParameter.getLimitCount());
    }

    @Override
    Sql getSqlUpdate(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        return Sql.update(Login.class).
                set(daoParameter.getSetValueArray()).
                where(daoParameter.getWherePredicate());
    }

    @Override
    Sql getSqlDelete(DaoParameter daoParameter) throws AppException {
        throw new UnsupportedOperationException();
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
                        setEntityProperty(column.getTableName(),
                                column.getColumnName(), login,
                                resultSet.getObject(columnIndex));
                    }
                }
                loginList.add(login);
            }
            return (List<T>) loginList;
        } catch (SQLException e) {
            throw new AppException(SQL_OPERATION_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

    Login getLogin(Long id) throws AppException {
        if (id == null) throw new AppException(NULL_NOT_ALLOWED);
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setWherePredicate(Predicate.
                get(LoginProperty.ID, WherePredicateOperator.EQUAL, id));
        List<Login> loginList = read(daoParameter);
        if (loginList == null || loginList.size() == 0) return NULL_LOGIN;
        return loginList.get(0);
    }

}
