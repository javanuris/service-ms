package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.Column;
import com.epam.java.rt.lab.dao.sql.Insert.InsertValue;
import com.epam.java.rt.lab.dao.sql.Select;
import com.epam.java.rt.lab.dao.sql.Sql;
import com.epam.java.rt.lab.entity.business.Photo;
import com.epam.java.rt.lab.entity.business.PhotoProperty;
import com.epam.java.rt.lab.exception.AppException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.epam.java.rt.lab.dao.DaoExceptionCode.SQL_OPERATION_ERROR;
import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;

public class PhotoDao extends JdbcDao {

    public PhotoDao(Connection connection) throws AppException {
        super(connection);
    }

    @Override
    Sql getSqlCreate(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        Photo photo = (Photo) daoParameter.getEntity();
        return Sql.insert(photo).values(
                new InsertValue(PhotoProperty.NAME, photo.getName()),
                new InsertValue(PhotoProperty.TYPE, photo.getType()),
                new InsertValue(PhotoProperty.FILE, photo.getFile()),
                new InsertValue(PhotoProperty.MODIFIED, photo.getModified()));
    }

    @Override
    Sql getSqlRead(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        return Sql.select(Photo.class).
                where(daoParameter.getWherePredicate()).
                orderBy(daoParameter.getOrderByCriteriaArray()).
                limit(daoParameter.getLimitOffset(),
                        daoParameter.getLimitCount());
    }

    @Override
    Sql getSqlUpdate(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        return Sql.update(Photo.class).
                set(daoParameter.getSetValueArray()).
                where(daoParameter.getWherePredicate());
    }

    @Override
    Sql getSqlDelete(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        return Sql.delete(Photo.class).
                where(daoParameter.getWherePredicate());
    }

    @Override
    Sql getSqlCount(DaoParameter daoParameter) throws AppException {
        throw new UnsupportedOperationException();
    }

    @Override
    <T> List<T> getEntity(ResultSet resultSet, Sql sql) throws AppException {
        if (resultSet == null || sql == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        Select select = (Select) sql;
        String photoTableName = Sql.getProperty(Photo.class.getName());
        List<Photo> photoList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int columnIndex = 0;
                Photo photo = null;
                for (Column column : select) {
                    columnIndex++;
                    if (photoTableName.equals(column.getTableName())) {
                        if (photo == null) photo = new Photo();
                        if (column.getColumnName().equals("file")) {
                            photo.setFile(resultSet.
                                    getBinaryStream(columnIndex));
                        } else {
                            setEntityProperty(column.getTableName(),
                                    column.getColumnName(), photo,
                                    resultSet.getObject(columnIndex));
                        }
                    }
                }
                photoList.add(photo);
            }
            return (List<T>) photoList;
        } catch (SQLException e) {
            throw new AppException(SQL_OPERATION_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

}
