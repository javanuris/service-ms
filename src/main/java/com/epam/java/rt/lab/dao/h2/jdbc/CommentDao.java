package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.Column;
import com.epam.java.rt.lab.dao.sql.Insert.InsertValue;
import com.epam.java.rt.lab.dao.sql.Select;
import com.epam.java.rt.lab.dao.sql.Sql;
import com.epam.java.rt.lab.entity.access.User;
import com.epam.java.rt.lab.entity.business.Comment;
import com.epam.java.rt.lab.entity.business.Comment.Property;
import com.epam.java.rt.lab.exception.AppException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.epam.java.rt.lab.dao.DaoExceptionCode.SQL_OPERATION_ERROR;
import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;

public class CommentDao extends JdbcDao {

    public CommentDao(Connection connection) throws AppException {
        super(connection);
    }

    @Override
    Sql getSqlCreate(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        Comment comment = (Comment) daoParameter.getEntity();
        return Sql.insert(comment).values(
                new InsertValue(Property.USER_ID, comment.getUser().getId()),
                new InsertValue(Property.CREATED, comment.getCreated()),
                new InsertValue(Property.APPLICATION_ID,
                        comment.getApplicationId()),
                new InsertValue(Property.PHOTO_ID, comment.getPhotoId()),
                new InsertValue(Property.MESSAGE, comment.getMessage()));
    }

    @Override
    Sql getSqlRead(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        return Sql.select(Comment.class).
                where(daoParameter.getWherePredicate()).
                orderBy(daoParameter.getOrderByCriteriaArray()).
                limit(daoParameter.getLimitOffset(),
                        daoParameter.getLimitCount());
    }

    @Override
    Sql getSqlUpdate(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        return Sql.update(Comment.class).
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
        return Sql.count(Comment.class);
    }

    @Override
    <T> List<T> getEntity(ResultSet resultSet, Sql sql) throws AppException {
        if (resultSet == null || sql == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
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
                        setEntityProperty(column.getTableName(),
                                column.getColumnName(), comment,
                                resultSet.getObject(columnIndex));
                    } else {
                        if (userTableName.equals(column.getTableName())) {
                            comment.setUser((new UserDao(getConnection())
                                    .getUser((Long) resultSet.
                                            getObject(columnIndex))));
                        }
                    }
                }
                commentList.add(comment);
            }
            return (List<T>) commentList;
        } catch (SQLException e) {
            throw new AppException(SQL_OPERATION_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

}