package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.Column;
import com.epam.java.rt.lab.dao.sql.Insert.InsertValue;
import com.epam.java.rt.lab.dao.sql.Select;
import com.epam.java.rt.lab.dao.sql.Sql;
import com.epam.java.rt.lab.entity.access.Activate;
import com.epam.java.rt.lab.entity.access.Activate.Property;
import com.epam.java.rt.lab.exception.AppException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.epam.java.rt.lab.dao.DaoExceptionCode.SQL_OPERATION_ERROR;
import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;

public class ActivateDao extends JdbcDao {

    public ActivateDao(Connection connection) throws AppException {
        super(connection);
    }

    @Override
    Sql getSqlCreate(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        Activate activate = (Activate) daoParameter.getEntity();
        return Sql.insert(activate).values(
                new InsertValue(Property.EMAIL, activate.getEmail()),
                new InsertValue(Property.SALT, activate.getSalt()),
                new InsertValue(Property.PASSWORD, activate.getPassword()),
                new InsertValue(Property.CODE, activate.getCode()),
                new InsertValue(Property.VALID, activate.getValid()));
    }

    @Override
    Sql getSqlRead(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        return Sql.select(Activate.class).
                where(daoParameter.getWherePredicate()).
                orderBy(daoParameter.getOrderByCriteriaArray()).
                limit(daoParameter.getLimitOffset(),
                        daoParameter.getLimitCount());
    }

    @Override
    Sql getSqlUpdate(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        return Sql.update(Activate.class).
                set(daoParameter.getSetValueArray()).
                where(daoParameter.getWherePredicate());
    }

    @Override
    Sql getSqlDelete(DaoParameter daoParameter) throws AppException {
        if (daoParameter == null) throw new AppException(NULL_NOT_ALLOWED);
        return Sql.delete(Activate.class).
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
                        setEntityProperty(column.getTableName(),
                                column.getColumnName(), activate,
                                resultSet.getObject(columnIndex));
                    }
                }
                activateList.add(activate);
            }
            return (List<T>) activateList;
        } catch (SQLException e) {
            throw new AppException(SQL_OPERATION_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

}
