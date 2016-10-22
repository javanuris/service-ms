package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.Column;
import com.epam.java.rt.lab.dao.sql.Insert.InsertValue;
import com.epam.java.rt.lab.dao.sql.Select;
import com.epam.java.rt.lab.dao.sql.Sql;
import com.epam.java.rt.lab.entity.access.Avatar;
import com.epam.java.rt.lab.entity.access.Avatar.Property;
import com.epam.java.rt.lab.exception.AppException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.epam.java.rt.lab.dao.DaoExceptionCode.SQL_OPERATION_ERROR;
import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;

public class AvatarDao extends JdbcDao {

    public AvatarDao(Connection connection) throws AppException {
        super(connection);
    }

    @Override
    Sql getSqlCreate(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        Avatar avatar = (Avatar) daoParameter.getEntity();
        return Sql.insert(avatar).values(
                new InsertValue(Property.NAME, avatar.getName()),
                new InsertValue(Property.TYPE, avatar.getType()),
                new InsertValue(Property.FILE, avatar.getFile()),
                new InsertValue(Property.MODIFIED, avatar.getModified()));
    }

    @Override
    Sql getSqlRead(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        return Sql.select(Avatar.class).
                where(daoParameter.getWherePredicate()).
                orderBy(daoParameter.getOrderByCriteriaArray()).
                limit(daoParameter.getLimitOffset(),
                        daoParameter.getLimitCount());
    }

    @Override
    Sql getSqlUpdate(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        return Sql.update(Avatar.class).
                set(daoParameter.getSetValueArray()).
                where(daoParameter.getWherePredicate());
    }

    @Override
    Sql getSqlDelete(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        return Sql.delete(Avatar.class).
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
        String avatarTableName = Sql.getProperty(Avatar.class.getName());
        List<Avatar> avatarList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int columnIndex = 0;
                Avatar avatar = null;
                for (Column column : select) {
                    columnIndex++;
                    if (avatarTableName.equals(column.getTableName())) {
                        if (avatar == null) avatar = new Avatar();
                        if (column.getColumnName().equals("file")) {
                            avatar.setFile(resultSet.
                                    getBinaryStream(columnIndex));
                        } else {
                            setEntityProperty(column.getTableName(),
                                    column.getColumnName(), avatar,
                                    resultSet.getObject(columnIndex));
                        }
                    }
                }
                avatarList.add(avatar);
            }
            return (List<T>) avatarList;
        } catch (SQLException e) {
            throw new AppException(SQL_OPERATION_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

}
