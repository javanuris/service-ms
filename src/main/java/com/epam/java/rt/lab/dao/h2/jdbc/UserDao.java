package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.Column;
import com.epam.java.rt.lab.dao.sql.Insert.InsertValue;
import com.epam.java.rt.lab.dao.sql.Select;
import com.epam.java.rt.lab.dao.sql.Sql;
import com.epam.java.rt.lab.dao.sql.Where.Predicate;
import com.epam.java.rt.lab.entity.access.Login;
import com.epam.java.rt.lab.entity.access.User;
import com.epam.java.rt.lab.entity.access.User.Property;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.web.access.RoleFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.epam.java.rt.lab.dao.DaoExceptionCode.SQL_OPERATION_ERROR;
import static com.epam.java.rt.lab.entity.access.User.NULL_USER;
import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;

public class UserDao extends JdbcDao {

    public UserDao(Connection connection) throws AppException {
        super(connection);
    }

    @Override
    Sql getSqlCreate(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        User user = (User) daoParameter.getEntity();
        return Sql.insert(user).values(
                new InsertValue(Property.FIRST_NAME, user.getFirstName()),
                new InsertValue(Property.MIDDLE_NAME, user.getMiddleName()),
                new InsertValue(Property.LAST_NAME, user.getLastName()),
                new InsertValue(Property.LOGIN_ID, user.getLogin().getId()),
                new InsertValue(Property.ROLE_NAME, user.getRole().getName()),
                new InsertValue(Property.AVATAR_ID, user.getAvatarId()));
    }

    @Override
    Sql getSqlRead(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        return Sql.select(User.class).
                where(daoParameter.getWherePredicate()).
                orderBy(daoParameter.getOrderByCriteriaArray()).
                limit(daoParameter.getLimitOffset(),
                        daoParameter.getLimitCount());
    }

    @Override
    Sql getSqlUpdate(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        return Sql.update(User.class).
                set(daoParameter.getSetValueArray()).
                where(daoParameter.getWherePredicate());
    }

    @Override
    Sql getSqlDelete(DaoParameter daoParameter) throws AppException {
        throw new UnsupportedOperationException();
    }

    @Override
    Sql getSqlCount(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        return Sql.count(User.class);
    }

    @Override
    <T> List<T> getEntity(ResultSet resultSet, Sql sql) throws AppException {
        if (resultSet == null || sql == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
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
                            setEntityProperty(column.getTableName(),
                                    column.getColumnName(), user,
                                    resultSet.getObject(columnIndex));
                        }
                    } else {
                        if (loginTableName.equals(column.getTableName())) {
                            user.setLogin((new LoginDao(getConnection())
                                    .getLogin((Long) resultSet.
                                            getObject(columnIndex))));
                        }
                    }
                }
                userList.add(user);
            }
            return (List<T>) userList;
        } catch (SQLException e) {
            throw new AppException(SQL_OPERATION_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

    User getUser(Long id) throws AppException {
        if (id == null) throw new AppException(NULL_NOT_ALLOWED);
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setWherePredicate(Predicate.
                get(Property.ID, Predicate.PredicateOperator.EQUAL, id));
        List<User> userList = read(daoParameter);
        if (userList == null || userList.size() == 0) return NULL_USER;
        return userList.get(0);
    }

}
