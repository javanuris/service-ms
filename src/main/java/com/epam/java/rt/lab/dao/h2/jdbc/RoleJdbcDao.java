package com.epam.java.rt.lab.dao.h2.jdbc;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.Column;
import com.epam.java.rt.lab.dao.sql.Select;
import com.epam.java.rt.lab.dao.sql.Sql;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.Role;

import java.sql.Connection;
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
        String entityTable = Sql.getProperty("Login");
        Select select = (Select) sql;
        List<Login> loginList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int columnIndex = 0;
                Login login = new Login();
                for (Column column : select) {
                    columnIndex++;
                    if (column.getTableName().equals(entityTable)) {
                        switch (column.getColumnName()) {
                            case "id":
                                login.setId(resultSet.getLong(columnIndex));
                                break;
                            case "email":
                                login.setEmail((String) resultSet.getObject(columnIndex));
                                break;
                            case "password":
                                login.setPassword((String) resultSet.getObject(columnIndex));
                                break;
                            case "attempt_left":
                                login.setAttemptLeft((Integer) resultSet.getObject(columnIndex));
                                break;
                            case "status":
                                login.setStatus((Integer) resultSet.getObject(columnIndex));
                                break;
                        }
                    } else {
                        // there are no referenced entities for login
                    }
                }
            }
            return null;
        } catch (SQLException e) {
            throw new DaoException("exception.dao.jdbc.login.get-entity", e.getCause());
        }
    }


//    @Override
//    String getEntityTableName() {
//        return "Role";
//    }
//
//    @Override
//    <T> Column getEntityColumn(T entity, Property field) throws DaoException {
//        try {
//            switch (field.getName()) {
//                case "id":
//                    return new Column("id", fieldValue(field, entity));
//                case "name.regex":
//                    return new Column("name.regex", fieldValue(field, entity));
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
//                case "name.regex":
//                    return new Set("name.regex", fieldValue(field, entity));
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
//            Role role = (Role) entity;
//            if (role == null) role = new Role();
//            role.setId(resultSet.getLong("id"));
//            role.setName(resultSet.getString("name.regex"));
//            List<Column> columnList = new ArrayList<>();
//            columnList.add(new Column("\"RolePermission\".permission_id", "\"Permission\".id", true));
//            columnList.add(new Column("\"RolePermission\".role_id", role.getId()));
//            String sqlString = "SELECT \"Permission\".uri FROM \"Permission\""
//                    .concat(" JOIN \"RolePermission\"")
//                    .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "="));
//            try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlString);
//                 ResultSet relResultSet = setPreparedStatementValues(preparedStatement, columnList).executeQuery();) {
//                List<String> uriList = new ArrayList<>();
//                while (relResultSet.next()) uriList.add(relResultSet.getString("uri"));
//                role.setUriList(uriList);
//                return (T) role;
//            } catch (SQLException e) {
//                e.printStackTrace();
//                throw new DaoException("exception.dao.getTransfer-entity-from-result-set.uri-list", e.getCause());
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new DaoException("exception.dao.jdbc.getTransfer-entity-from-result-set.role", e.getCause());
//        }
//    }
//
//    @Override
//    <T> int relUpdate(T entity, String setNames) throws DaoException {
//        int updateCount = 0;
//        Role role = (Role) entity;
//        List<Column> columnList = new ArrayList<>();
//        columnList.add(new Column("\"RolePermission\".permission_id", "\"Permission\".id", true));
//        columnList.add(new Column("\"RolePermission\".role_id", role.getId()));
//        String sqlString = "SELECT \"RolePermission\".id \"Permission\".uri FROM \"Permission\""
//                .concat(" JOIN \"RolePermission\"")
//                .concat(" WHERE ").concat(Column.columnListToString(columnList, "AND", "="));
//        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlString);
//             ResultSet relResultSet = preparedStatement.executeQuery();) {
//            List<String> uriList = role.getUriList();
//            List<String> resUriList = new ArrayList<>();
//            // delete references
//            columnList.clear();
//            while (relResultSet.next()) {
//                if (!uriList.contains(relResultSet.getString("uri"))) {
//                    columnList.add(new Column("id", relResultSet.getLong("id")));
//                } else {
//                    resUriList.add(relResultSet.getString("uri"));
//                }
//            }
//            sqlString = "DELETE FROM \"RolePermission\""
//                    .concat(" WHERE ").concat(Column.columnListToString(columnList, "OR", "="));
//            try (PreparedStatement deleteStatement = getConnection().prepareStatement(sqlString);) {
//                setPreparedStatementValues(deleteStatement, columnList);
//                updateCount += deleteStatement.executeUpdate();
//            } catch (SQLException e) {
//                e.printStackTrace();
//                throw new DaoException("exception.dao.rel-update.delete", e.getCause());
//            }
//            // define unreferenced
//            columnList.clear();
//            for (String uri : uriList) if (!resUriList.contains(uri)) columnList.add(new Column("uri", uri));
//            sqlString = "SELECT * FROM \"Permission\""
//                    .concat(" WHERE ").concat(Column.columnListToString(columnList, "OR", "="));
//            try (PreparedStatement selectStatement = getConnection().prepareStatement(sqlString);
//                 ResultSet resultSet = setPreparedStatementValues(selectStatement, columnList).executeQuery();) {
//                // add references
//                columnList.clear();
//                while (resultSet.next()) columnList.add(new Column("permission_id", resultSet.getLong("id")));
//                sqlString = "INSERT INTO \"RolePermission\" (role_id, permission_id) VALUES (?, ?)";
//                try (PreparedStatement addStatement = getConnection().prepareStatement(sqlString);) {
//                    List<Column> insertList = new ArrayList<>();
//                    for (Column column : columnList) {
//                        addStatement.clearParameters();
//                        insertList.clear();
//                        insertList.add(new Column("", role.getId()));
//                        insertList.add(new Column("", column.value));
//                        updateCount += setPreparedStatementValues(preparedStatement, insertList).executeUpdate();
//                    }
//                    return updateCount;
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                    throw new DaoException("exception.dao.rel-update.result-set", e.getCause());
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//                throw new DaoException("exception.dao.rel-update.result-set", e.getCause());
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new DaoException("exception.dao.rel-update.result-set", e.getCause());
//        }
//    }
//
//    @Override
//    <T> String getEntitySetNames(T entity) {
//        return null;
//    }


    // newly dao implementation

//    @Override
//    <T> List<T> getEntityList(ResultSet resultSet, JdbcParameter jdbcParameter) throws SQLException, DaoException {
//        List<Role> roleList = new ArrayList<>();
//        while (resultSet.next()) roleList.add(getEntity(resultSet, jdbcParameter));
//        return (List<T>) roleList;
//    }
//
//    @Override
//    <T> T getEntity(ResultSet resultSet, JdbcParameter jdbcParameter) throws SQLException, DaoException {
////        Role role = new Role();
////        List<JdbcParameter.Column> columnList =
////                CastManager.getList(jdbcParameter.get(JdbcParameterType.SELECT_COLUMN_LIST), JdbcParameter.Column.class);
////        for (JdbcParameter.Column column : columnList) {
////            if (column.getTableName().equals(DEFAULT_TABLE)) {
////                switch (column.getColumnName()) {
////                    case "id":
////                        role.setId(resultSet.getLong(column.getColumnName()));
////                        break;
////                    case "name":
////                        role.setName((String) resultSet.getObject(column.getColumnName()));
////                        break;
////                    case "uri_list":
////                        JdbcDao_ jdbcDao = new PermissionJdbcDao(getConnection());
////                        JdbcParameter permissionJdbcParameter = JdbcParameter.of(new Parameter_()
////                                        .result(Permission.Property.URI)
////                                        .filter(Parameter_.Field.set(Role.Property.ID, role.getId()))
////                                        .order(OrderType.ASC, Permission.Property.URI),
////                                jdbcDao
////                        );
////                        String joinTables = (String) permissionJdbcParameter.get(JdbcParameterType.JOIN_TABLES);
////                        joinTables = joinTables.concat(QueryBuilder_.COMMA_DELIMITER).concat(ROLE_PERMISSION_TABLE);
////                        permissionJdbcParameter.put(JdbcParameterType.JOIN_TABLES, joinTables);
////                        List<JdbcParameter.Field> fieldList =
////                                CastManager.getList(permissionJdbcParameter.get(JdbcParameterType.WHERE_FIELD_LIST), JdbcParameter.Field.class);
////                        fieldList.add(new JdbcParameter.Field(
////                                null,
////                                new JdbcParameter.Column(ROLE_PERMISSION_TABLE, ROLE_PERMISSION_JOIN_COLUMN),
////                                new JdbcParameter.Column(DEFAULT_TABLE, DEFAULT_JOIN_COLUMN)
////                        ));
////                        List<Permission> permissionList = jdbcDao.getAll(permissionJdbcParameter);
////                        List<String> uriList = new ArrayList<>();
////                        for (Permission permission : permissionList) uriList.add(permission.getUri());
////                        role.setUriList(uriList);
////                        break;
////                }
////            }
////        }
////        return (T) role;
//        return null;
//    }
//
//    @Override
//    JdbcParameter.Column getColumn(EntityProperty entityProperty) {
//        String entityName = entityProperty.getClass().getSuperclass().getSimpleName();
//        if (entityName.equals(Role.class.getSimpleName()))
//            return new JdbcParameter.Column(DEFAULT_TABLE, entityProperty.toString().toLowerCase());
//        return null;
//    }
//
//    @Override
//    JdbcParameter.Field getParameterJoinWhereField(String joinTable) throws DaoException {
//        throw new DaoException("exception.dao.role.get-parameter-join-where-field");
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
//        columnList.add(DEFAULT_TABLE.concat(".name"));
//        return columnList;
//    }
//
}
