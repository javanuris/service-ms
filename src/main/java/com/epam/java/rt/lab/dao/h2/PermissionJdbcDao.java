package com.epam.java.rt.lab.dao.h2;

import com.epam.java.rt.lab.dao.Argument;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.query.Column;
import com.epam.java.rt.lab.dao.query.Select;
import com.epam.java.rt.lab.dao.query.Set;
import com.epam.java.rt.lab.entity.rbac.Permission;
import com.epam.java.rt.lab.entity.rbac.Role;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * service-ms
 */
public class PermissionJdbcDao extends JdbcDao {
    private static final String DEFAULT_FROM = "\"Permission\"";

    public PermissionJdbcDao(Connection connection) throws DaoException {
        super(connection);
    }

    @Override
    String getEntityTableName() {
        return "Permission";
    }

    @Override
    <T> Column getEntityColumn(T entity, Field field) throws DaoException {
        try {
            switch (field.getName()) {
                case "id":
                    return new Column("id", fieldValue(field, entity));
                case "uri":
                    return new Column("uri", fieldValue(field, entity));
                default:
                    throw new DaoException("exception.dao.jdbc.getTransfer-entity-column.field-name");
            }
        } catch (IllegalAccessException e) {
            throw new DaoException("exception.dao.jdbc.getTransfer-entity-column.add-column", e.getCause());
        }
    }

    @Override
    <T> Set getEntitySet(T entity, Field field) throws DaoException {
        try {
            switch (field.getName()) {
                case "id":
                    return new Set("id", fieldValue(field, entity));
                case "uri":
                    return new Set("uri", fieldValue(field, entity));
                default:
                    throw new DaoException("exception.dao.jdbc.getTransfer-entity-set.field-name");
            }
        } catch (IllegalAccessException e) {
            throw new DaoException("exception.dao.jdbc.getTransfer-entity-set.add-column", e.getCause());
        }
    }

    @Override
    <T> T getEntityFromResultSet(T entity, ResultSet resultSet) throws DaoException {
        try {
            Permission permission = (Permission) entity;
            if (permission == null) permission = new Permission();
            permission.setId(resultSet.getLong("id"));
            permission.setUri(resultSet.getString("uri"));
            return (T) permission;
        } catch (SQLException e) {
            throw new DaoException("exception.dao.jdbc.getTransfer-entity-from-result-set", e.getCause());
        }
    }

    @Override
    <T> String getEntitySetNames(T entity) {
        return null;
    }

    // newly dao implementation

    @Override
    <T> List<T> getEntityList(ResultSet resultSet, Argument argument) throws SQLException, DaoException {
        List<Permission> permissionList = new ArrayList<>();
        while (resultSet.next()) permissionList.add(getEntity(resultSet, argument));
        return (List<T>) permissionList;
    }

    @Override
    <T> T getEntity(ResultSet resultSet, Argument argument) throws SQLException, DaoException {
        Permission permission = new Permission();
        for (String columnName : (List<String>) argument.get(ArgumentType.SELECT_COLUMN_LIST)) {
            if (columnName.startsWith(DEFAULT_FROM.concat("."))) {
                String shortColumnName = columnName.substring(DEFAULT_FROM.length() + 1);
                switch (shortColumnName) {
                    case "id":
                        permission.setId(resultSet.getLong(shortColumnName));
                        break;
                    case "uri":
                        permission.setUri((String) resultSet.getObject(shortColumnName));
                        break;
                }
            }
        }
        return (T) permission;
    }

    @Override
    Argument.Field getJoinWhere(String joinTable) throws DaoException {
        throw new DaoException("exception.dao.jdbc.get-join-where");
    }

    @Override
    String getDefaultFrom() {
        return DEFAULT_FROM;
    }

    @Override
    List<String> getAllSelectColumnList() {
        List<String> columnList = new ArrayList<>();
        columnList.add(DEFAULT_FROM.concat(".id"));
        columnList.add(DEFAULT_FROM.concat(".uri"));
        return columnList;
    }


}
