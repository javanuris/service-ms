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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * service-ms
 */
public class RoleJdbcDao extends JdbcDao {

    public RoleJdbcDao(Connection connection) throws DaoException {
        super(connection);
    }

    @Override
    String preparedStatementMapKeyPrefix() {
        return "Role";
    }

    @Override
    <T> Column entityColumn(T entity, Field field) throws DaoException {
        try {
            switch (field.getName()) {
                case "id":
                    return new Column("id", fieldValue(field, entity));
                case "name":
                    return new Column("name", fieldValue(field, entity));
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
            Role role = (Role) entity;
            role.setId(resultSet.getLong("id"));
            role.setName(resultSet.getString("name"));
            

            // uriList
            return (T) role;
        } catch (SQLException e) {
            throw new DaoException("exception.dao.jdbc.get-entity-from-result-set", e.getCause());
        }
    }

    @Override
    public <T> List<T> getAll(T entity, String fieldNames) throws DaoException {
        throw new DaoException("exception.dao.jdbc.unsupported");
    }

    @Override
    public <T> List<T> getAll(T entity, String fieldNames, String columnNames) throws DaoException {
        throw new DaoException("exception.dao.jdbc.unsupported");
    }

}
