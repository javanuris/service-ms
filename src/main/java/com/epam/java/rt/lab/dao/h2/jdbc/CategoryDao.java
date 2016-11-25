package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.*;
import com.epam.java.rt.lab.dao.sql.Insert.InsertValue;
import com.epam.java.rt.lab.entity.business.Category;
import com.epam.java.rt.lab.entity.business.CategoryProperty;
import com.epam.java.rt.lab.exception.AppException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.epam.java.rt.lab.dao.DaoExceptionCode.SQL_OPERATION_ERROR;
import static com.epam.java.rt.lab.entity.business.Category.NULL_CATEGORY;
import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;

public class CategoryDao extends JdbcDao {

    public CategoryDao(Connection connection) throws AppException {
        super(connection);
    }

    @Override
    Sql getSqlCreate(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        Category category = (Category) daoParameter.getEntity();
        return Sql.insert(category).values(
                new InsertValue(CategoryProperty.CREATED,
                        category.getCreated()),
                new InsertValue(CategoryProperty.PARENT_ID,
                        category.getParentId()),
                new InsertValue(CategoryProperty.NAME,
                        category.getName()));
    }

    @Override
    Sql getSqlRead(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        return Sql.select(Category.class).
                where(daoParameter.getWherePredicate()).
                orderBy(daoParameter.getOrderByCriteriaArray()).
                limit(daoParameter.getLimitOffset(),
                        daoParameter.getLimitCount());
    }

    @Override
    Sql getSqlUpdate(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        return Sql.update(Category.class).
                set(daoParameter.getSetValueArray()).
                where(daoParameter.getWherePredicate());
    }

    @Override
    Sql getSqlDelete(DaoParameter daoParameter) {
        throw new UnsupportedOperationException();
    }

    @Override
    Sql getSqlCount(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        return Sql.count(Category.class);
    }

    @Override
    <T> List<T> getEntity(ResultSet resultSet, Sql sql) throws AppException {
        if (resultSet == null || sql == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        Select select = (Select) sql;
        String categoryTableName = Sql.getProperty(Category.class.getName());
        List<Category> categoryList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int columnIndex = 0;
                Category category = null;
                for (Column column : select) {
                    columnIndex++;
                    if (categoryTableName.equals(column.getTableName())) {
                        if (category == null) category = new Category();
                        setEntityProperty(column.getTableName(),
                                column.getColumnName(), category,
                                resultSet.getObject(columnIndex));
                    }
                }
                categoryList.add(category);
            }
            return (List<T>) categoryList;
        } catch (SQLException e) {
            throw new AppException(SQL_OPERATION_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

    Category getCategory(Long id) throws AppException {
        if (id == null) throw new AppException(NULL_NOT_ALLOWED);
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setWherePredicate(Where.Predicate.
                get(CategoryProperty.ID,
                        WherePredicateOperator.EQUAL, id));
        List<Category> categoryList = read(daoParameter);
        if (categoryList == null || categoryList.size() == 0) {
            return NULL_CATEGORY;
        }
        return categoryList.get(0);
    }

}
