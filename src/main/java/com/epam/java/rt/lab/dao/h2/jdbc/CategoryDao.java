package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.*;
import com.epam.java.rt.lab.entity.business.Category;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * category-ms
 */
public class CategoryDao extends JdbcDao {

    public CategoryDao(Connection connection) throws DaoException {
        super(connection);
    }

    @Override
    Sql getSqlCreate(DaoParameter daoParameter) throws DaoException {
        Category category = (Category) daoParameter.getEntity();
        return Sql
                .insert(category)
                .values(
                        new Insert.InsertValue(Category.Property.CREATED, category.getCreated()),
                        new Insert.InsertValue(Category.Property.PARENT_ID,
                                 category.getParent() == null ? null : category.getParent().getId()),
                        new Insert.InsertValue(Category.Property.NAME, category.getName())
                );
    }

    @Override
    Sql getSqlRead(DaoParameter daoParameter) throws DaoException {
        return Sql
                .select(Category.class)
                .where(daoParameter.getWherePredicate())
                .orderBy(daoParameter.getOrderByCriteriaArray())
                .limit(daoParameter.getLimitOffset(), daoParameter.getLimitCount());
    }

    @Override
    Sql getSqlUpdate(DaoParameter daoParameter) throws DaoException {
        return Sql
                .update(Category.class)
                .set(daoParameter.getSetValueArray())
                .where(daoParameter.getWherePredicate());
    }

    @Override
    Sql getSqlDelete(DaoParameter daoParameter) {
        return null;
    }

    @Override
    Sql getSqlCount(DaoParameter daoParameter) throws DaoException {
        return Sql.count(Category.class);
    }

    @Override
    <T> List<T> getEntity(ResultSet resultSet, Sql sql) throws DaoException {
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
                        if (column.getColumnName().equals("parent_id")) { // TODO: variable instead literal
                            // fill only id
                            Long parentId = (Long) resultSet.getObject(columnIndex);
                            if (parentId != null) {
                                Category parent = new Category();
                                parent.setId(parentId);
                                category.setParent(parent);
                            }
                        } else {
                            setEntityProperty(column.getTableName(), column.getColumnName(), category, resultSet.getObject(columnIndex));
                        }
                    } else {
                        // another entity
                    }
                }
                categoryList.add(category);
            }
            return (List<T>) categoryList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.jdbc.category.get-entity", e.getCause());
        }
    }

    Category getCategory(Long id) throws DaoException {
        if (id == null) return null;
        List<Category> categoryList = read(new DaoParameter()
                .setWherePredicate(Where.Predicate.get(
                        Category.Property.ID,
                        Where.Predicate.PredicateOperator.EQUAL,
                        id
                ))
        );
        if (categoryList == null || categoryList.size() == 0) return null;
        return categoryList.get(0);
    }

}
