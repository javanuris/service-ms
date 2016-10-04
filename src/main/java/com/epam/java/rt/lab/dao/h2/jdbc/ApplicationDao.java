package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.*;
import com.epam.java.rt.lab.entity.business.Application;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.web.access.RoleException;
import com.epam.java.rt.lab.web.access.RoleFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * category-ms
 */
public class ApplicationDao extends JdbcDao {

    public ApplicationDao(Connection connection) throws DaoException {
        super(connection);
    }

    @Override
    Sql getSqlCreate(DaoParameter daoParameter) throws DaoException {
        Application application = (Application) daoParameter.getEntity();
        return Sql
                .insert(application)
                .values(
                        new Insert.InsertValue(Application.Property.CREATED, application.getCreated()),
                        new Insert.InsertValue(Application.Property.USER_ID, application.getUser().getId()),
                        new Insert.InsertValue(Application.Property.CATEGORY_ID, application.getCategory().getId()),
                        new Insert.InsertValue(Application.Property.MESSAGE, application.getMessage())
                );
    }

    @Override
    Sql getSqlRead(DaoParameter daoParameter) throws DaoException {
        return Sql
                .select(Application.class)
                .where(daoParameter.getWherePredicate())
                .orderBy(daoParameter.getOrderByCriteriaArray())
                .limit(daoParameter.getLimitOffset(), daoParameter.getLimitCount());
    }

    @Override
    Sql getSqlUpdate(DaoParameter daoParameter) throws DaoException {
        return Sql
                .update(Application.class)
                .set(daoParameter.getSetValueArray())
                .where(daoParameter.getWherePredicate());
    }

    @Override
    Sql getSqlDelete(DaoParameter daoParameter) {
        return null;
    }

    @Override
    Sql getSqlCount(DaoParameter daoParameter) throws DaoException {
        return Sql.count(Application.class);
    }

    @Override
    <T> List<T> getEntity(ResultSet resultSet, Sql sql) throws DaoException {
        Select select = (Select) sql;
        String userTableName = Sql.getProperty(User.class.getName());
        String loginTableName = Sql.getProperty(Login.class.getName());
        List<User> userList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int columnIndex = 0;
                User user = null;
                for (Column column : select) {
                    columnIndex++;
                    if (userTableName.equals(column.getTableName())) {
                        if (user == null) user = new User();
                        if (column.getColumnName().equals("role_name")) {
                            user.setRole(RoleFactory.getInstance()
                                    .create(resultSet.getString(columnIndex)));
                        } else {
                            setEntityProperty(column.getTableName(), column.getColumnName(), user, resultSet.getObject(columnIndex));
                        }
                    } else {
                        if (loginTableName.equals(column.getTableName())) {
                            Long loginId = (Long) resultSet.getObject(columnIndex);
                            if (loginId != null) {
                                Dao dao = new LoginDao(getConnection());
                                List<Login> loginList = dao.read(new DaoParameter()
                                        .setWherePredicate(Where.Predicate.get(
                                                Login.Property.ID,
                                                Where.Predicate.PredicateOperator.EQUAL,
                                                loginId
                                        ))
                                );
                                if (loginList != null && loginList.size() > 0)
                                    user.setLogin(loginList.get(0));
                            }
                        }
                    }
                }
                userList.add(user);
            }
            return (List<T>) userList;
        } catch (SQLException e) {
            throw new DaoException("exception.dao.jdbc.user.get-entity", e.getCause());
        } catch (RoleException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.jdbc.user.role-factory", e.getCause());
        }
    }

}
