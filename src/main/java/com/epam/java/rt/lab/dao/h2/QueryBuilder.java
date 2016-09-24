package com.epam.java.rt.lab.dao.h2;

import com.epam.java.rt.lab.dao.h2.jdbc.JdbcParameter;
import com.epam.java.rt.lab.dao.types.FunctionType;
import com.epam.java.rt.lab.dao.types.JdbcParameterType;
import com.epam.java.rt.lab.dao.types.OrderType;
import com.epam.java.rt.lab.dao.types.QueryType;
import com.epam.java.rt.lab.util.StringArray;

import java.util.List;

/**
 * The {@code QueryBuilder} class represents private static methods
 * to generate sql query to sql-database and one public static method
 * {@code getSql} to return generated sql query.
 *
 * @author Rollan Taigulov
 * @see JdbcParameter
 * @see JdbcParameterType
 * @see QueryType
 * @see FunctionType
 * @see OrderType
 */
public class QueryBuilder {

    private static final String SELECT = "SELECT ";
    private static final String FROM = " FROM ";
    private static final String JOIN = " JOIN ";
    private static final String WHERE = " WHERE ";
    private static final String ORDER_BY = " ORDER BY ";
    private static final String LIMIT = " LIMIT ";
    private static final String AND_DELIMITER = " AND ";

    public static final String COMMA_DELIMITER = ", ";
    public static final String SPACE = " ";

    public static final String FUNCTION_COUNT_RESULT = "count";
    private static final String FUNCTION_COUNT_SELECT = "SELECT COUNT(*) AS ";

    /**
     * Appends all generated parts of sql query according to {@code QueryType}.
     *
     * @param jdbcParameter
     *        The value object to exchange necessary data between jdbc-methods,
     *        stores {@code JdbcParameterType} data
     * @see JdbcParameter
     * @see JdbcParameterType
     * @see QueryType
     * @see FunctionType
     * @see OrderType
     */
    public static String getSql(JdbcParameter jdbcParameter) {
        StringBuilder result = new StringBuilder();
        switch ((QueryType) jdbcParameter.get(JdbcParameterType.QUERY_TYPE)) {
            case FUNCTION:
                return funcQuery(result, jdbcParameter).toString();
            case READ:
                return result
                        .append(select(jdbcParameter))
                        .append(from(jdbcParameter))
                        .append(join(jdbcParameter))
                        .append(where(jdbcParameter))
                        .append(order(jdbcParameter))
                        .append(limit(jdbcParameter)).toString();
        }
        return null;
    }

    private static StringBuilder funcQuery(StringBuilder result, JdbcParameter jdbcParameter) {
        switch ((FunctionType) jdbcParameter.get(JdbcParameterType.FUNCTION)) {
            case COUNT:
                return result
                        .append(FUNCTION_COUNT_SELECT).append(SPACE).append(FUNCTION_COUNT_RESULT)
                        .append(from(jdbcParameter))
                        .append(join(jdbcParameter))
                        .append(where(jdbcParameter));
        }
        return result;
    }

    private static String select(JdbcParameter jdbcParameter) {
        return SELECT.concat(StringArray.combine(
                (List<?>) jdbcParameter.get(JdbcParameterType.SELECT_COLUMN_LIST),
                COMMA_DELIMITER
        ));
    }

    private static String from(JdbcParameter jdbcParameter) {
        return FROM.concat((String) jdbcParameter.get(JdbcParameterType.FROM_TABLE));
    }

    private static String join(JdbcParameter jdbcParameter) {
        Object joinTables = jdbcParameter.get(JdbcParameterType.JOIN_TABLES);
        return joinTables == null ? "" : JOIN.concat((String) joinTables);
    }

    private static String where(JdbcParameter jdbcParameter) {
        Object whereColumnList = jdbcParameter.get(JdbcParameterType.WHERE_FIELD_LIST);
        return whereColumnList == null ? "" : WHERE.concat(StringArray.combine(
                (List<?>) whereColumnList,
                AND_DELIMITER
        ));
    }

    private static String order(JdbcParameter jdbcParameter) {
        Object orderColumnList = jdbcParameter.get(JdbcParameterType.ORDER_COLUMN_LIST);
        Object orderType = jdbcParameter.get(JdbcParameterType.ORDER_TYPE);
        return orderColumnList == null || orderType == null ? "" :
                ORDER_BY.concat(StringArray.combine(
                        (List<?>) orderColumnList,
                        COMMA_DELIMITER
                )).concat(orderType instanceof OrderType ? SPACE.concat(orderType.toString()) : "");
    }

    private static String limit(JdbcParameter jdbcParameter) {
        return (jdbcParameter.get(JdbcParameterType.LIMIT_OFFSET) == null) ||
                jdbcParameter.get(JdbcParameterType.LIMIT_COUNT) == null ? "" :
                LIMIT.concat(String.valueOf(jdbcParameter.get(JdbcParameterType.LIMIT_OFFSET))).concat(COMMA_DELIMITER)
                        .concat(String.valueOf(jdbcParameter.get(JdbcParameterType.LIMIT_COUNT)));
    }

}
