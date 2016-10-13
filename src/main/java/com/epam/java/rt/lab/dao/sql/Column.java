package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.dao.DaoException;

import static com.epam.java.rt.lab.dao.sql.Sql.SIGN_POINT;

/**
 * {@code Column} class defines {@code Column} object for sql statement
 *
 * @see Sql
 */
public class Column implements Clause {

    /**
     * Stores table name
     */
    private String tableName;
    /** Stores column name */
    private String columnName;

    /**
     * Initiates new {@code Column} object with defined
     * table and column name
     *
     * @param tableName     {@code String} representation valueOf table name
     * @param columnName    {@code String} representation valueOf column name
     */
    Column(String tableName, String columnName) {
        this.tableName = tableName;
        this.columnName = columnName;
    }

    /**
     * Returns new {@code Column} object from full column name,
     * which contains full table name and column name divided
     * by point sign
     *
     * @param tableAndColumnName    {@code String} representation valueOf
     *                              table name and column name divided by point
     *                              sign
     * @return                      {@code Column} object
     * @throws DaoException
     */
    static Column of(String tableAndColumnName) throws DaoException {
        if (tableAndColumnName == null || !tableAndColumnName.contains(SIGN_POINT))
            throw new DaoException("exception.dao.sql.column.table-and-column-name");
        String tableName = tableAndColumnName.substring(0, tableAndColumnName.lastIndexOf("."));
        String columnName = tableAndColumnName.substring(tableAndColumnName.lastIndexOf(".") + 1);
        return new Column(tableName, columnName);
    }

    /**
     * Returns defined at initiation table name
     *
     * @return                      {@code String} representation valueOf table name
     */
    public String getTableName() {
        return this.tableName;
    }

    /**
     * Returns defined at initiation column name
     *
     * @return                      {@code String} representation valueOf column name
     */
    public String getColumnName() {
        return this.columnName;
    }

    @Override
    public StringBuilder appendClause(StringBuilder result) throws DaoException {
        return result.append(this.tableName).append(SIGN_POINT).append(this.columnName);
    }

}
