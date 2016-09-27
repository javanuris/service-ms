package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.EntityProperty;
import com.epam.java.rt.lab.entity.rbac.Permission;
import com.epam.java.rt.lab.util.CastManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * service-ms
 */
public class PermissionJdbcDao extends JdbcDao_ {

    private static final String DEFAULT_TABLE = "\"Permission\"";
    private static final String ROLE_PERMISSION_TABLE = "\"RolePermission\"";

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
//
//    @Override
//    <T> List<T> getEntityList(ResultSet resultSet, JdbcParameter jdbcParameter)
//            throws SQLException, DaoException {
//        List<Permission> permissionList = new ArrayList<>();
//        while (resultSet.next()) permissionList.add(getEntity(resultSet, jdbcParameter));
//        return (List<T>) permissionList;
//    }
//
//    @Override
//    <T> T getEntity(ResultSet resultSet, JdbcParameter jdbcParameter) throws SQLException, DaoException {
////        Permission permission = new Permission();
////        List<JdbcParameter.Column> columnList =
////                CastManager.getList(jdbcParameter.get(JdbcParameterType.SELECT_COLUMN_LIST), JdbcParameter.Column.class);
////        for (JdbcParameter.Column column : columnList) {
////            if (column.getTableName().equals(DEFAULT_TABLE)) {
////                switch (column.getColumnName()) {
////                    case "id":
////                        permission.setId(resultSet.getLong(column.getColumnName()));
////                        break;
////                    case "uri":
////                        permission.setUri((String) resultSet.getObject(column.getColumnName()));
////                        break;
////                }
////            }
////        }
////        return (T) permission;
//        return null;
//    }
//
//    @Override
//    JdbcParameter.Column getColumn(EntityProperty entityProperty) {
//        String entityName = entityProperty.getClass().getSuperclass().getSimpleName();
//        if (entityName.equals(Permission.class.getSimpleName())) {
//            return new JdbcParameter.Column(DEFAULT_TABLE, entityProperty.toString().toLowerCase());
//        } else if (entityName.equals(JdbcDao_.class.getSimpleName())) {
//            if (entityProperty.getClass().getSimpleName().equals(ROLE_PERMISSION_TABLE.replaceAll("\"", "")))
//                return new JdbcParameter.Column(ROLE_PERMISSION_TABLE, entityProperty.toString().toLowerCase());
//        }
//        return null;
//    }
//
//    @Override
//    JdbcParameter.Field getParameterJoinWhereField(String joinTable) throws DaoException {
//        throw new DaoException("exception.dao.jdbc.permission.get-parameter-join-where-field");
//    }
//
//    @Override
//    String getParameterDefaultFrom() {
//        return DEFAULT_TABLE;
//    }
//
//    @Override
//    List<String> getParameterAllSelectColumnList() {
//        List<String> columnList = new ArrayList<>();
//        columnList.add(DEFAULT_TABLE.concat(".id"));
//        columnList.add(DEFAULT_TABLE.concat(".uri"));
//        return columnList;
//    }
//

}
