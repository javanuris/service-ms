package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.Parameter;
import com.epam.java.rt.lab.dao.types.JdbcParameterType;
import com.epam.java.rt.lab.entity.rbac.Permission;
import com.epam.java.rt.lab.util.CastManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * service-ms
 */
public class PermissionJdbcDao extends JdbcDao {

    private static final String DEFAULT_TABLE = "\"Permission\"";
    private static final String DEFAULT_JOIN_COLUMN = "id";
    private static final String ROLE_PERMISSION_TABLE = "\"RolePermission\"";
    private static final String ROLE_PERMISSION_JOIN_COLUMN = "permission_id";

    public PermissionJdbcDao(Connection connection) throws DaoException {
        super(connection);
    }

//    @Override
//    String getEntityTableName() {
//        return "Permission";
//    }
//
//    @Override
//    <T> Column getEntityColumn(T entity, Property field) throws DaoException {
//        try {
//            switch (field.getName()) {
//                case "id":
//                    return new Column("id", fieldValue(field, entity));
//                case "uri":
//                    return new Column("uri", fieldValue(field, entity));
//                default:
//                    throw new DaoException("exception.dao.jdbc.getTransfer-entity-column.field-name");
//            }
//        } catch (IllegalAccessException e) {
//            throw new DaoException("exception.dao.jdbc.getTransfer-entity-column.add-column", e.getCause());
//        }
//    }
//
//    @Override
//    <T> Set getEntitySet(T entity, Property field) throws DaoException {
//        try {
//            switch (field.getName()) {
//                case "id":
//                    return new Set("id", fieldValue(field, entity));
//                case "uri":
//                    return new Set("uri", fieldValue(field, entity));
//                default:
//                    throw new DaoException("exception.dao.jdbc.getTransfer-entity-set.field-name");
//            }
//        } catch (IllegalAccessException e) {
//            throw new DaoException("exception.dao.jdbc.getTransfer-entity-set.add-column", e.getCause());
//        }
//    }
//
//    @Override
//    <T> T getEntityFromResultSet(T entity, ResultSet resultSet) throws DaoException {
//        try {
//            Permission permission = (Permission) entity;
//            if (permission == null) permission = new Permission();
//            permission.setId(resultSet.getLong("id"));
//            permission.setUri(resultSet.getString("uri"));
//            return (T) permission;
//        } catch (SQLException e) {
//            throw new DaoException("exception.dao.jdbc.getTransfer-entity-from-result-set", e.getCause());
//        }
//    }
//
//    @Override
//    <T> String getEntitySetNames(T entity) {
//        return null;
//    }

    // newly dao implementation

    @Override
    <T> List<T> getEntityList(ResultSet resultSet, JdbcParameter jdbcParameter)
            throws SQLException, DaoException {
        List<Permission> permissionList = new ArrayList<>();
        while (resultSet.next()) permissionList.add(getEntity(resultSet, jdbcParameter));
        return (List<T>) permissionList;
    }

    @Override
    <T> T getEntity(ResultSet resultSet, JdbcParameter jdbcParameter) throws SQLException, DaoException {
        Permission permission = new Permission();
        List<JdbcParameter.Column> columnList =
                CastManager.getList(jdbcParameter.get(JdbcParameterType.SELECT_COLUMN_LIST), JdbcParameter.Column.class);
        for (JdbcParameter.Column column : columnList) {
            if (column.getTableName().equals(DEFAULT_TABLE)) {
                switch (column.getColumnName()) {
                    case "id":
                        permission.setId(resultSet.getLong(column.getColumnName()));
                        break;
                    case "uri":
                        permission.setUri((String) resultSet.getObject(column.getColumnName()));
                        break;
                }
            }
        }
        return (T) permission;
    }

    @Override
    String getTableName(String entityClassName) {
        if (entityClassName.equals(Permission.class.getSimpleName())) {
            return DEFAULT_TABLE;
        }
        return "";
     }

    @Override
    JdbcParameter.Field getParameterJoinWhereField(String joinTable) throws DaoException {
        throw new DaoException("exception.dao.jdbc.permission.get-parameter-join-where-field");
    }

    @Override
    String getParameterDefaultFrom() {
        return DEFAULT_TABLE;
    }

    @Override
    List<String> getParameterAllSelectColumnList() {
        List<String> columnList = new ArrayList<>();
        columnList.add(DEFAULT_TABLE.concat(".id"));
        columnList.add(DEFAULT_TABLE.concat(".uri"));
        return columnList;
    }


}
