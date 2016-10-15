package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.Column;
import com.epam.java.rt.lab.dao.sql.Insert;
import com.epam.java.rt.lab.dao.sql.Select;
import com.epam.java.rt.lab.dao.sql.Sql;
import com.epam.java.rt.lab.entity.access.Activate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * category-ms
 */
public class ActivateDao extends JdbcDao {

    public ActivateDao(Connection connection) throws DaoException {
        super(connection);
    }

    @Override
    Sql getSqlCreate(DaoParameter daoParameter) throws DaoException {
        Activate activate = (Activate) daoParameter.getEntity();
        return Sql
                .insert(activate)
                .values(
                        new Insert.InsertValue(Activate.Property.EMAIL, activate.getEmail()),
                        new Insert.InsertValue(Activate.Property.SALT, activate.getSalt()),
                        new Insert.InsertValue(Activate.Property.PASSWORD, activate.getPassword()),
                        new Insert.InsertValue(Activate.Property.CODE, activate.getCode()),
                        new Insert.InsertValue(Activate.Property.VALID, activate.getValid())
                );
    }

    @Override
    Sql getSqlRead(DaoParameter daoParameter) throws DaoException {
        return Sql
                .select(Activate.class)
                .where(daoParameter.getWherePredicate())
                .orderBy(daoParameter.getOrderByCriteriaArray())
                .limit(daoParameter.getLimitOffset(), daoParameter.getLimitCount());
    }

    @Override
    Sql getSqlUpdate(DaoParameter daoParameter) throws DaoException {
        return Sql
                .update(Activate.class)
                .set(daoParameter.getSetValueArray())
                .where(daoParameter.getWherePredicate());
    }

    @Override
    Sql getSqlDelete(DaoParameter daoParameter) throws DaoException {
        return Sql
                .delete(Activate.class)
                .where(daoParameter.getWherePredicate());
    }

    @Override
    Sql getSqlCount(DaoParameter daoParameter) throws DaoException {
        return null;
    }

    @Override
    <T> List<T> getEntity(ResultSet resultSet, Sql sql) throws DaoException {
        Select select = (Select) sql;
        String activateTableName = Sql.getProperty(Activate.class.getName());
        List<Activate> activateList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int columnIndex = 0;
                Activate activate = null;
                for (Column column : select) {
                    columnIndex++;
                    if (activateTableName.equals(column.getTableName())) {
                        if (activate == null) activate = new Activate();
                        setEntityProperty(column.getTableName(), column.getColumnName(), activate, resultSet.getObject(columnIndex));
                    } else {
                        // another entity
                    }
                }
                activateList.add(activate);
            }
            return (List<T>) activateList;
        } catch (SQLException e) {
            throw new DaoException("exception.dao.jdbc.activate.get-entity", e.getCause());
        }
    }

}
