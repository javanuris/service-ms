package com.epam.java.rt.lab.dao.sql;

import com.epam.java.rt.lab.exception.AppException;

import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;
import static com.epam.java.rt.lab.util.PropertyManager.POINT;

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
     * @param tableAndColumnName    {@code String} representation of
     *                              table name and column name divided by point
     *                              sign
     * @return                      {@code Column} object
     * @throws AppException
     */
    static Column from(String tableAndColumnName) throws AppException {
        if (tableAndColumnName == null
                || !tableAndColumnName.contains(POINT)) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        int lastPointIndex = tableAndColumnName.lastIndexOf(POINT);
        String tableName = tableAndColumnName.substring(0, lastPointIndex);
        String columnName = tableAndColumnName.substring(lastPointIndex + 1);
        return new Column(tableName, columnName);
    }

    /**
     * Returns defined at initiation table name
     *
     * @return                      {@code String} representation of table name
     */
    public String getTableName() {
        return this.tableName;
    }

    /**
     * Returns defined at initiation column name
     *
     * @return                      {@code String} representation of column name
     */
    public String getColumnName() {
        return this.columnName;
    }

    @Override
    public StringBuilder appendClause(StringBuilder result)
            throws AppException {
        if (result == null) throw new AppException(NULL_NOT_ALLOWED);
        return result.append(this.tableName + POINT + this.columnName);
    }

}