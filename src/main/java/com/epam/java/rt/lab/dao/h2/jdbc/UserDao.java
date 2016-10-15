package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.*;
import com.epam.java.rt.lab.entity.access.Login;
import com.epam.java.rt.lab.entity.access.User;
import com.epam.java.rt.lab.web.access.AccessException;
import com.epam.java.rt.lab.web.access.RoleFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * category-ms
 */
public class UserDao extends JdbcDao {

    public UserDao(Connection connection) throws DaoException {
        super(connection);
    }

    @Override
    Sql getSqlCreate(DaoParameter daoParameter) throws DaoException {
        User user = (User) daoParameter.getEntity();
        return Sql
                .insert(user)
                .values(
                        new Insert.InsertValue(User.Property.FIRST_NAME, user.getFirstName()),
                        new Insert.InsertValue(User.Property.MIDDLE_NAME, user.getMiddleName()),
                        new Insert.InsertValue(User.Property.LAST_NAME, user.getLastName()),
                        new Insert.InsertValue(User.Property.LOGIN_ID, user.getLogin().getId()),
                        new Insert.InsertValue(User.Property.ROLE_NAME, user.getRole().getName()),
                        new Insert.InsertValue(User.Property.AVATAR_ID, user.getAvatarId())
                );
    }

    @Override
    Sql getSqlRead(DaoParameter daoParameter) throws DaoException {
        return Sql
                .select(User.class)
                .where(daoParameter.getWherePredicate())
                .orderBy(daoParameter.getOrderByCriteriaArray())
                .limit(daoParameter.getLimitOffset(), daoParameter.getLimitCount());
    }

    @Override
    Sql getSqlUpdate(DaoParameter daoParameter) throws DaoException {
        return Sql
                .update(User.class)
                .set(daoParameter.getSetValueArray())
                .where(daoParameter.getWherePredicate());
    }

    @Override
    Sql getSqlDelete(DaoParameter daoParameter) {
        return null;
    }

    @Override
    Sql getSqlCount(DaoParameter daoParameter) throws DaoException {
        return Sql.count(User.class);
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
                            user.setLogin((new LoginDao(getConnection())
                                    .getLogin((Long) resultSet.getObject(columnIndex))));
                        }
                    }
                }
                userList.add(user);
            }
            return (List<T>) userList;
        } catch (SQLException e) {
            throw new DaoException("exception.dao.jdbc.user.get-entity", e.getCause());
        } catch (AccessException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.jdbc.user.role-factory", e.getCause());
        }
    }

    User getUser(Long id) throws DaoException {
        if (id == null) return null;
        List<User> userList = read(new DaoParameter()
                .setWherePredicate(Where.Predicate.get(
                        User.Property.ID,
                        Where.Predicate.PredicateOperator.EQUAL,
                        id
                ))
        );
        if (userList == null || userList.size() == 0) return null;
        return userList.get(0);
    }

}
