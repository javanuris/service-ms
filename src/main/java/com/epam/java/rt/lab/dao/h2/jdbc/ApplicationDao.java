package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.*;
import com.epam.java.rt.lab.entity.business.Application;
import com.epam.java.rt.lab.entity.business.Category;
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
        String applicationTableName = Sql.getProperty(Application.class.getName());
        String userTableName = Sql.getProperty(User.class.getName());
        String categoryTableName = Sql.getProperty(Category.class.getName());
        List<Application> applicationList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int columnIndex = 0;
                Application application = null;
                for (Column column : select) {
                    columnIndex++;
                    if (applicationTableName.equals(column.getTableName())) {
                        if (application == null) application = new Application();
                        setEntityProperty(column.getTableName(), column.getColumnName(), application, resultSet.getObject(columnIndex));
                    } else {
                        if (userTableName.equals(column.getTableName())) {
                            application.setUser((new UserDao(getConnection())
                                    .getUser((Long) resultSet.getObject(columnIndex))));
                        } else if (categoryTableName.equals(column.getTableName())) {
                            application.setCategory((new CategoryDao(getConnection())
                                    .getCategory((Long) resultSet.getObject(columnIndex))));
                        }
                    }
                }
                applicationList.add(application);
            }
            return (List<T>) applicationList;
        } catch (SQLException e) {
            throw new DaoException("exception.dao.jdbc.application.get-entity", e.getCause());
        }
    }

}
