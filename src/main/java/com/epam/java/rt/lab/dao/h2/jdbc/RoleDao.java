package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.Column;
import com.epam.java.rt.lab.dao.sql.Select;
import com.epam.java.rt.lab.dao.sql.Sql;
import com.epam.java.rt.lab.dao.sql.Where;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.Permission;
import com.epam.java.rt.lab.entity.rbac.Role;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * service-ms
 */
public class RoleDao extends JdbcDao {

    public RoleDao(Connection connection) throws DaoException {
        super(connection);
    }

    @Override
    Sql getSqlCreate(DaoParameter daoParameter) {
        return null;
    }

    @Override
    Sql getSqlRead(DaoParameter daoParameter) throws DaoException {
        return Sql
                .select(Role.class)
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
        String roleTableName = Sql.getProperty(Role.class.getName());
        List<Role> roleList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int columnIndex = 0;
                Role role = null;
                for (Column column : select) {
                    columnIndex++;
                    if (roleTableName.equals(column.getTableName())) {
                        if (role == null) role = new Role();
                        setEntityProperty(column.getTableName(), column.getColumnName(), role, resultSet.getObject(columnIndex));
                    } else {
                        // another entity
                    }
                }
                Dao dao = new PermissionDao(getConnection());
                List<Permission> permissionList = dao.read(new DaoParameter()
                        .setWherePredicate(Where.Predicate.get(
                                Role.Property.ID,
                                Where.Predicate.PredicateOperator.EQUAL,
                                role.getId()
                        ))

                );
                List<String> uriList = new ArrayList<>();
                for (Permission permission : permissionList)
                        uriList.add(permission.getUri());
                role.setUriList(uriList);
                roleList.add(role);
            }
            return (List<T>) roleList;
        } catch (SQLException e) {
            throw new DaoException("exception.dao.jdbc.role.get-entity", e.getCause());
        }
    }

}
