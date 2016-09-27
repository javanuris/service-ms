package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.util.StringArray;

import static com.epam.java.rt.lab.dao.sql.Sql.SIGN_POINT;

/**
 * service-ms
 */
class Column {

    private String tableName;
    private String columnName;

    Column(String tableName, String columnName) {
        this.tableName = tableName;
        this.columnName = columnName;
    }

    static Column of(String tableAndColumnName) throws DaoException {
        if (tableAndColumnName == null || !tableAndColumnName.contains(SIGN_POINT))
            throw new DaoException("exception.dao.sql.column.table-and-column-name");
        String[] split = StringArray.splitSpaceLessNames(tableAndColumnName, SIGN_POINT);
        return new Column(split[0], split[1]);
    }

    String getTableName() {
        return tableName;
    }

    String getColumnName() {
        return columnName;
    }

    @Override
    public String toString() {
        return this.tableName.concat(this.columnName);
    }

}
