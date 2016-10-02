package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.*;
import com.epam.java.rt.lab.entity.EntityProperty;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.Permission;
import com.epam.java.rt.lab.entity.rbac.Role;
import com.epam.java.rt.lab.entity.rbac.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * service-ms
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
                        new Insert.InsertValue(User.Property.ROLE_ID, user.getRole().getId()),
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
    <T> List<T> getEntity(ResultSet resultSet, Sql sql) throws DaoException {
        Select select = (Select) sql;
        String userTableName = Sql.getProperty(User.class.getName());
        String loginTableName = Sql.getProperty(Login.class.getName());
        String roleTableName = Sql.getProperty(Role.class.getName());
        List<User> userList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int columnIndex = 0;
                User user = null;
                for (Column column : select) {
                    columnIndex++;
                    if (userTableName.equals(column.getTableName())) {
                        if (user == null) user = new User();
                        setEntityProperty(column.getTableName(), column.getColumnName(), user, resultSet.getObject(columnIndex));
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
                        } else if (roleTableName.equals(column.getTableName())) {
                            Long roleId = (Long) resultSet.getObject(columnIndex);
                            if (roleId != null) {
                                Dao dao = new RoleDao(getConnection());
                                List<Role> roleList = dao.read(new DaoParameter()
                                        .setWherePredicate(Where.Predicate.get(
                                                Role.Property.ID,
                                                Where.Predicate.PredicateOperator.EQUAL,
                                                roleId
                                        ))
                                );
                                if (roleList != null && roleList.size() > 0)
                                    user.setRole(roleList.get(0));
                            }
                        }
                    }
                }
                userList.add(user);
            }
            return (List<T>) userList;
        } catch (SQLException e) {
            throw new DaoException("exception.dao.jdbc.user.get-entity", e.getCause());
        }
    }

}
