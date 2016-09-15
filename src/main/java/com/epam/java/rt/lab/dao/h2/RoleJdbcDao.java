package com.epam.java.rt.lab.dao.h2;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.query.Column;
import com.epam.java.rt.lab.dao.query.Set;
import com.epam.java.rt.lab.entity.rbac.Role;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * service-ms
 */
public class RoleJdbcDao extends JdbcDao {

    public RoleJdbcDao(Connection connection) throws DaoException {
        super(connection);
    }

    @Override
    String getEntityTableName() {
        return "Role";
    }

    @Override
    <T> Column getEntityColumn(T entity, Field field) throws DaoException {
        try {
            switch (field.getName()) {
                case "id":
                    return new Column("id", fieldValue(field, entity));
                case "name":
                    return new Column("name", fieldValue(field, entity));
                default:
                    throw new DaoException("exception.dao.jdbc.get-entity-column.field-name");
            }
        } catch (IllegalAccessException e) {
            throw new DaoException("exception.dao.jdbc.get-entity-column.add-column", e.getCause());
        }
    }

    @Override
    <T> Set getEntitySet(T entity, Field field) throws DaoException {
        try {
            switch (field.getName()) {
                case "id":
                    return new Set("id", fieldValue(field, entity));
                case "name":
                    return new Set("name", fieldValue(field, entity));
                default:
                    throw new DaoException("exception.dao.jdbc.get-entity-set.field-name");
            }
        } catch (IllegalAccessException e) {
            throw new DaoException("exception.dao.jdbc.get-entity-set.add-column", e.getCause());
        }
    }

    @Override
    <T> T getEntityFromResultSet(T entity, ResultSet resultSet) throws DaoException {
        try {
            Role role = (Role) entity;
            if (role == null) role = new Role();
            role.setId(resultSet.getLong("id"));
            role.setName(resultSet.getString("name"));
            List<Column> columnList = new ArrayList<>();
            columnList.add(new Column("\"RolePermission\".permission_id", "\"Permission\".id", true));
            columnList.add(new Column("\"RolePermission\".role_id", role.getId()));
            String sqlString = "SELECT \"Permission\".uri FROM \"Permission\""
                    .concat(" JOIN \"RolePermission\"")
                    .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "="));
            try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlString);
                 ResultSet relResultSet = setPreparedStatementValues(preparedStatement, columnList).executeQuery();) {
                List<String> uriList = new ArrayList<>();
                while (relResultSet.next()) uriList.add(relResultSet.getString("uri"));
                role.setUriList(uriList);
                return (T) role;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DaoException("exception.dao.get-entity-from-result-set.uri-list", e.getCause());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.jdbc.get-entity-from-result-set.role", e.getCause());
        }
    }

    @Override
    <T> int relUpdate(T entity, String setNames) throws DaoException {
        int updateCount = 0;
        Role role = (Role) entity;
        List<Column> columnList = new ArrayList<>();
        columnList.add(new Column("\"RolePermission\".permission_id", "\"Permission\".id", true));
        columnList.add(new Column("\"RolePermission\".role_id", role.getId()));
        String sqlString = "SELECT \"RolePermission\".id \"Permission\".uri FROM \"Permission\""
                .concat(" JOIN \"RolePermission\"")
                .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "="));
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlString);
             ResultSet relResultSet = preparedStatement.executeQuery();) {
            List<String> uriList = role.getUriList();
            List<String> resUriList = new ArrayList<>();
            // delete references
            columnList.clear();
            while (relResultSet.next()) {
                if (!uriList.contains(relResultSet.getString("uri"))) {
                    columnList.add(new Column("id", relResultSet.getLong("id")));
                } else {
                    resUriList.add(relResultSet.getString("uri"));
                }
            }
            sqlString = "DELETE FROM \"RolePermission\""
                    .concat(" WHERE ").concat(Column.columnListToString(columnList, "OR", "="));
            try (PreparedStatement deleteStatement = getConnection().prepareStatement(sqlString);) {
                setPreparedStatementValues(deleteStatement, columnList);
                updateCount += deleteStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DaoException("exception.dao.rel-update.delete", e.getCause());
            }
            // define unreferenced
            columnList.clear();
            for (String uri : uriList) if (!resUriList.contains(uri)) columnList.add(new Column("uri", uri));
            sqlString = "SELECT * FROM \"Permission\""
                    .concat(" WHERE ").concat(Column.columnListToString(columnList, "OR", "="));
            try (PreparedStatement selectStatement = getConnection().prepareStatement(sqlString);
                 ResultSet resultSet = setPreparedStatementValues(selectStatement, columnList).executeQuery();) {
                // add references
                columnList.clear();
                while (resultSet.next()) columnList.add(new Column("permission_id", resultSet.getLong("id")));
                sqlString = "INSERT INTO \"RolePermission\" (role_id, permission_id) VALUES (?, ?)";
                try (PreparedStatement addStatement = getConnection().prepareStatement(sqlString);) {
                    List<Column> insertList = new ArrayList<>();
                    for (Column column : columnList) {
                        addStatement.clearParameters();
                        insertList.clear();
                        insertList.add(new Column("", role.getId()));
                        insertList.add(new Column("", column.value));
                        updateCount += setPreparedStatementValues(preparedStatement, insertList).executeUpdate();
                    }
                    return updateCount;
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new DaoException("exception.dao.rel-update.result-set", e.getCause());
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DaoException("exception.dao.rel-update.result-set", e.getCause());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.rel-update.result-set", e.getCause());
        }
    }
}
