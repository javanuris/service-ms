package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.Column;
import com.epam.java.rt.lab.dao.sql.Select;
import com.epam.java.rt.lab.dao.sql.Sql;
import com.epam.java.rt.lab.entity.EntityProperty;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.Permission;
import com.epam.java.rt.lab.entity.rbac.Role;
import com.epam.java.rt.lab.util.CastManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * service-ms
 */
public class PermissionDao extends JdbcDao {

    public enum RolePermissionProperty implements EntityProperty {
        ID,
        ROLE_ID,
        PERMISSION_ID;

        @Override
        public Class getEntityClass() {
            return PermissionDao.RolePermissionProperty.class;
        }
    }

    public PermissionDao(Connection connection) throws DaoException {
        super(connection);
    }

    @Override
    Sql getSqlCreate(DaoParameter daoParameter) {
        return null;
    }

    @Override
    Sql getSqlRead(DaoParameter daoParameter) throws DaoException {
        return Sql
                .select(Permission.class)
                .where(daoParameter.getWherePredicate())
                .orderBy(daoParameter.getOrderByCriteriaArray())
                .limit(daoParameter.getLimitOffset(), daoParameter.getLimitCount());
    }

    @Override
    Sql getSqlUpdate(DaoParameter daoParameter) {
        return null;
    }

    @Override
    Sql getSqlDelete(DaoParameter daoParameter) {
        return null;
    }

    @Override
    <T> List<T> getEntity(ResultSet resultSet, Sql sql) throws DaoException {
        Select select = (Select) sql;
        String permissionTableName = Sql.getProperty(Permission.class.getName());
        List<Permission> permissionList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int columnIndex = 0;
                Permission permission = null;
                for (Column column : select) {
                    columnIndex++;
                    if (permissionTableName.equals(column.getTableName())) {
                        if (permission == null) permission = new Permission();
                        setEntityProperty(column.getTableName(), column.getColumnName(), permission, resultSet.getObject(columnIndex));
                    } else {
                        // another entity
                    }
                }
                permissionList.add(permission);
            }
            return (List<T>) permissionList;
        } catch (SQLException e) {
            throw new DaoException("exception.dao.jdbc.permission.get-entity", e.getCause());
        }
    }

}
