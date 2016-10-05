package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.Column;
import com.epam.java.rt.lab.dao.sql.Insert;
import com.epam.java.rt.lab.dao.sql.Select_;
import com.epam.java.rt.lab.dao.sql.Sql;
import com.epam.java.rt.lab.entity.business.Photo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * category-ms
 */
public class PhotoDao extends JdbcDao {

    public PhotoDao(Connection connection) throws DaoException {
        super(connection);
    }

    @Override
    Sql getSqlCreate(DaoParameter daoParameter) throws DaoException {
        Photo photo = (Photo) daoParameter.getEntity();
        return Sql
                .insert(photo)
                .values(
                        new Insert.InsertValue(Photo.Property.NAME, photo.getName()),
                        new Insert.InsertValue(Photo.Property.TYPE, photo.getType()),
                        new Insert.InsertValue(Photo.Property.FILE, photo.getFile()),
                        new Insert.InsertValue(Photo.Property.MODIFIED, photo.getModified())
                );
    }

    @Override
    Sql getSqlRead(DaoParameter daoParameter) throws DaoException {
        return Sql
                .select(Photo.class)
                .where(daoParameter.getWherePredicate())
                .orderBy(daoParameter.getOrderByCriteriaArray())
                .limit(daoParameter.getLimitOffset(), daoParameter.getLimitCount());
    }

    @Override
    Sql getSqlUpdate(DaoParameter daoParameter) throws DaoException {
        return Sql
                .update(Photo.class)
                .set(daoParameter.getSetValueArray())
                .where(daoParameter.getWherePredicate());
    }

    @Override
    Sql getSqlDelete(DaoParameter daoParameter) throws DaoException {
        return Sql
                .delete(Photo.class)
                .where(daoParameter.getWherePredicate());
    }

    @Override
    Sql getSqlCount(DaoParameter daoParameter) throws DaoException {
        return null;
    }

    @Override
    <T> List<T> getEntity(ResultSet resultSet, Sql sql) throws DaoException {
        Select_ select = (Select_) sql;
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
                            photo.setFile(resultSet.getBinaryStream(columnIndex));
                        } else {
                            setEntityProperty(column.getTableName(), column.getColumnName(), photo, resultSet.getObject(columnIndex));
                        }
                    } else {
                        // another entity
                    }
                }
                photoList.add(photo);
            }
            return (List<T>) photoList;
        } catch (SQLException e) {
            throw new DaoException("exception.dao.jdbc.photo.get-entity", e.getCause());
        }
    }

}
