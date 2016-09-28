package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.util.StringArray;

import static com.epam.java.rt.lab.dao.sql.Sql.SIGN_POINT;
import static com.epam.java.rt.lab.dao.sql.Sql.SIGN_POINT_REGEX;

/**
 * service-ms
 */
public class Column implements Clause {

    private String tableName;
    private String columnName;

    Column(String tableName, String columnName) {
        this.tableName = tableName;
        this.columnName = columnName;
    }

    static Column of(String tableAndColumnName) throws DaoException {
        if (tableAndColumnName == null || !tableAndColumnName.contains(SIGN_POINT))
            throw new DaoException("exception.dao.sql.column.table-and-column-name");
        String tableName = tableAndColumnName.substring(0, tableAndColumnName.lastIndexOf("."));
        String columnName = tableAndColumnName.substring(tableAndColumnName.lastIndexOf(".") + 1);
        return new Column(tableName, columnName);
    }

    public String getTableName() {
        return this.tableName;
    }

    public String getColumnName() {
        return this.columnName;
    }

    @Override
    public StringBuilder appendClause(StringBuilder result) throws DaoException {
        return result.append(this.tableName).append(SIGN_POINT).append(this.columnName);
    }

}
