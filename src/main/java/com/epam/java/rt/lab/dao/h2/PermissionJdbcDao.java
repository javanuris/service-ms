package com.epam.java.rt.lab.dao.h2;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.query.Column;
import com.epam.java.rt.lab.dao.query.Select;
import com.epam.java.rt.lab.entity.rbac.Permission;
import com.epam.java.rt.lab.entity.rbac.Role;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * service-ms
 */
public class PermissionJdbcDao extends JdbcDao {

    public PermissionJdbcDao(Connection connection) throws DaoException {
        super(connection);
    }

    @Override
    String preparedStatementMapKeyPrefix() {
        return "Permission";
    }

    @Override
    <T> Column entityColumn(T entity, Field field) throws DaoException {
        try {
            switch (field.getName()) {
                case "id":
                    return new Column("id", fieldValue(field, entity));
                case "uri":
                    return new Column("uri", fieldValue(field, entity));
                default:
                    throw new DaoException("exception.dao.jdbc.get-first.field-name");
            }
        } catch (IllegalAccessException e) {
            throw new DaoException("exception.dao.jdbc.entity-column.add-column", e.getCause());
        }
    }

    @Override
    <T> T getEntityFromResultSet(T entity, ResultSet resultSet) throws DaoException {
        try {
            Permission permission = (Permission) entity;
            permission.setId(resultSet.getLong("id"));
            permission.setUri(resultSet.getString("uri"));
            return (T) permission;
        } catch (SQLException e) {
            throw new DaoException("exception.dao.jdbc.get-entity-from-result-set", e.getCause());
        }
    }

}
