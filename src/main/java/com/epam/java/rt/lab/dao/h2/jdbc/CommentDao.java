package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.*;
import com.epam.java.rt.lab.entity.business.Comment;
import com.epam.java.rt.lab.entity.rbac.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * category-ms
 */
public class CommentDao extends JdbcDao {

    public CommentDao(Connection connection) throws DaoException {
        super(connection);
    }

    @Override
    Sql getSqlCreate(DaoParameter daoParameter) throws DaoException {
        Comment comment = (Comment) daoParameter.getEntity();
        return Sql
                .insert(comment)
                .values(
                        new Insert.InsertValue(Comment.Property.USER_ID, comment.getUser().getId()),
                        new Insert.InsertValue(Comment.Property.CREATED, comment.getCreated()),
                        new Insert.InsertValue(Comment.Property.APPLICATION_ID, comment.getApplicationId()),
                        new Insert.InsertValue(Comment.Property.PHOTO_ID, comment.getPhotoId()),
                        new Insert.InsertValue(Comment.Property.MESSAGE, comment.getMessage())
                );
    }

    @Override
    Sql getSqlRead(DaoParameter daoParameter) throws DaoException {
        return Sql
                .select(Comment.class)
                .where(daoParameter.getWherePredicate())
                .orderBy(daoParameter.getOrderByCriteriaArray())
                .limit(daoParameter.getLimitOffset(), daoParameter.getLimitCount());
    }

    @Override
    Sql getSqlUpdate(DaoParameter daoParameter) throws DaoException {
        return Sql
                .update(Comment.class)
                .set(daoParameter.getSetValueArray())
                .where(daoParameter.getWherePredicate());
    }

    @Override
    Sql getSqlDelete(DaoParameter daoParameter) {
        return null;
    }

    @Override
    Sql getSqlCount(DaoParameter daoParameter) throws DaoException {
        return Sql.count(Comment.class);
    }

    @Override
    <T> List<T> getEntity(ResultSet resultSet, Sql sql) throws DaoException {
        Select select = (Select) sql;
        String commentTableName = Sql.getProperty(Comment.class.getName());
        String userTableName = Sql.getProperty(User.class.getName());
        List<Comment> commentList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int columnIndex = 0;
                Comment comment = null;
                for (Column column : select) {
                    columnIndex++;
                    if (commentTableName.equals(column.getTableName())) {
                        if (comment == null) comment = new Comment();
                        setEntityProperty(column.getTableName(), column.getColumnName(), comment, resultSet.getObject(columnIndex));
                    } else {
                        if (userTableName.equals(column.getTableName())) {
                            comment.setUser((new UserDao(getConnection())
                                    .getUser((Long) resultSet.getObject(columnIndex))));
                        }
                    }
                }
                commentList.add(comment);
            }
            return (List<T>) commentList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.jdbc.comment.get-entity", e.getCause());
        }
    }

}
