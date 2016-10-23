package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.Column;
import com.epam.java.rt.lab.dao.sql.Insert.InsertValue;
import com.epam.java.rt.lab.dao.sql.Select;
import com.epam.java.rt.lab.dao.sql.Sql;
import com.epam.java.rt.lab.dao.sql.Where.Predicate;
import com.epam.java.rt.lab.entity.access.User;
import com.epam.java.rt.lab.entity.business.Application;
import com.epam.java.rt.lab.entity.business.Application.Property;
import com.epam.java.rt.lab.entity.business.Category;
import com.epam.java.rt.lab.exception.AppException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.epam.java.rt.lab.dao.DaoExceptionCode.SQL_OPERATION_ERROR;
import static com.epam.java.rt.lab.entity.business.Application.NULL_APPLICATION;
import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;

public class ApplicationDao extends JdbcDao {

    public ApplicationDao(Connection connection) throws AppException {
        super(connection);
    }

    @Override
    Sql getSqlCreate(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        Application application = (Application) daoParameter.getEntity();
        return Sql.insert(application).values(
                new InsertValue(Property.CREATED, application.getCreated()),
                new InsertValue(Property.USER_ID,
                        application.getUser().getId()),
                new InsertValue(Property.CATEGORY_ID,
                        ((application.getCategory() == null) ? null
                                : application.getCategory().getId())),
                new InsertValue(Property.MESSAGE, application.getMessage()));
    }

    @Override
    Sql getSqlRead(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        return Sql.select(Application.class).
                where(daoParameter.getWherePredicate()).
                orderBy(daoParameter.getOrderByCriteriaArray()).
                limit(daoParameter.getLimitOffset(),
                        daoParameter.getLimitCount());
    }

    @Override
    Sql getSqlUpdate(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        return Sql.update(Application.class).
                set(daoParameter.getSetValueArray()).
                where(daoParameter.getWherePredicate());
    }

    @Override
    Sql getSqlDelete(DaoParameter daoParameter) {
        throw new UnsupportedOperationException();
    }

    @Override
    Sql getSqlCount(DaoParameter daoParameter) throws AppException {
        return Sql.count(Application.class);
    }

    @Override
    <T> List<T> getEntity(ResultSet resultSet, Sql sql) throws AppException {
        if (resultSet == null || sql == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        Select select = (Select) sql;
        String applicationTableName =
                Sql.getProperty(Application.class.getName());
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
                        if (application == null) {
                            application = new Application();
                        }
                        setEntityProperty(column.getTableName(),
                                column.getColumnName(), application,
                                resultSet.getObject(columnIndex));
                    } else {
                        if (userTableName.equals(column.getTableName())) {
                            UserDao userDao = new UserDao(getConnection());
                            Long userId = (Long) resultSet.
                                    getObject(columnIndex);
                            if (userId != null) {
                                application.setUser(userDao.getUser(userId));
                            }
                        } else if (categoryTableName.
                                equals(column.getTableName())) {
                            CategoryDao categoryDao =
                                    new CategoryDao(getConnection());
                            Long categoryId = (Long) resultSet.
                                    getObject(columnIndex);
                            if (categoryId != null) {
                                application.setCategory(categoryDao.
                                        getCategory(categoryId));
                            }
                        }
                    }
                }
                applicationList.add(application);
            }
            return (List<T>) applicationList;
        } catch (SQLException e) {
            throw new AppException(SQL_OPERATION_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

    Application getApplication(Long id) throws AppException {
        if (id == null) throw new AppException(NULL_NOT_ALLOWED);
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setWherePredicate(Predicate.
                get(Property.ID, Predicate.PredicateOperator.EQUAL, id));
        List<Application> applicationList = read(daoParameter);
        if (applicationList == null || applicationList.size() == 0) {
            return NULL_APPLICATION;
        }
        return applicationList.get(0);
    }

}