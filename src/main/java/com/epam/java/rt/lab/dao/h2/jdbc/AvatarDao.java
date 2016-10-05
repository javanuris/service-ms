package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.Column;
import com.epam.java.rt.lab.dao.sql.Insert;
import com.epam.java.rt.lab.dao.sql.Select;
import com.epam.java.rt.lab.dao.sql.Sql;
import com.epam.java.rt.lab.entity.rbac.Avatar;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * category-ms
 */
public class AvatarDao extends JdbcDao {

    public AvatarDao(Connection connection) throws DaoException {
        super(connection);
    }

    @Override
    Sql getSqlCreate(DaoParameter daoParameter) throws DaoException {
        Avatar avatar = (Avatar) daoParameter.getEntity();
        return Sql
                .insert(avatar)
                .values(
                        new Insert.InsertValue(Avatar.Property.NAME, avatar.getName()),
                        new Insert.InsertValue(Avatar.Property.TYPE, avatar.getType()),
                        new Insert.InsertValue(Avatar.Property.FILE, avatar.getFile()),
                        new Insert.InsertValue(Avatar.Property.MODIFIED, avatar.getModified())
                );
    }

    @Override
    Sql getSqlRead(DaoParameter daoParameter) throws DaoException {
        return Sql
                .select(Avatar.class)
                .where(daoParameter.getWherePredicate())
                .orderBy(daoParameter.getOrderByCriteriaArray())
                .limit(daoParameter.getLimitOffset(), daoParameter.getLimitCount());
    }

    @Override
    Sql getSqlUpdate(DaoParameter daoParameter) throws DaoException {
        return Sql
                .update(Avatar.class)
                .set(daoParameter.getSetValueArray())
                .where(daoParameter.getWherePredicate());
    }

    @Override
    Sql getSqlDelete(DaoParameter daoParameter) throws DaoException {
        return Sql
                .delete(Avatar.class)
                .where(daoParameter.getWherePredicate());
    }

    @Override
    Sql getSqlCount(DaoParameter daoParameter) throws DaoException {
        return null;
    }

    @Override
    <T> List<T> getEntity(ResultSet resultSet, Sql sql) throws DaoException {
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
                            avatar.setFile(resultSet.getBinaryStream(columnIndex));
                        } else {
                            setEntityProperty(column.getTableName(), column.getColumnName(), avatar, resultSet.getObject(columnIndex));
                        }
                    } else {
                        // another entity
                    }
                }
                avatarList.add(avatar);
            }
            return (List<T>) avatarList;
        } catch (SQLException e) {
            throw new DaoException("exception.dao.jdbc.avatar.get-entity", e.getCause());
        }
    }

}
