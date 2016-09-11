package com.epam.java.rt.lab.dao.h2;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.query.Column;
import com.epam.java.rt.lab.entity.rbac.Role;

import java.lang.reflect.Field;
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
                    throw new DaoException("exception.dao.jdbc.get-first.field-name");
            }
        } catch (IllegalAccessException e) {
            throw new DaoException("exception.dao.jdbc.entity-column.add-column", e.getCause());
        }
    }

    @Override
    <T> T getEntityFromResultSet(T entity, ResultSet resultSet) throws DaoException {
        try {
            System.out.println("!!! 0");
            Role role = (Role) entity;
            if (role == null) role = new Role();
            System.out.println("!!! 1");
            System.out.println("### " + resultSet.toString());
            role.setId(resultSet.getLong("id"));
            System.out.println("!!! 2");
            role.setName(resultSet.getString("name"));
            System.out.println("!!! 3");
            List<Column> columnList = new ArrayList<>();
            columnList.add(new Column("\"RolePermission\".permission_id", "\"Permission\".id", true));
            columnList.add(new Column("\"RolePermission\".role_id", role.getId()));
            ResultSet relResultSet = rawQuery("SELECT \"Permission\".uri FROM \"Permission\""
                    .concat(" JOIN \"RolePermission\"")
                    .concat(" WHERE ").concat(Column.columnListToString(columnList)),
                    columnList);
            List<String> uriList = new ArrayList<>();
            while (relResultSet.next()) uriList.add(relResultSet.getString("uri"));
            role.setUriList(uriList);
            return (T) role;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("exception.dao.jdbc.get-entity-from-result-set", e.getCause());
        }
    }

}
