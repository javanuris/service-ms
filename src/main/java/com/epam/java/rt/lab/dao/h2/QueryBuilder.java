package com.epam.java.rt.lab.dao.h2;

import com.epam.java.rt.lab.dao.Parameter;
import com.epam.java.rt.lab.util.StringArray;

import java.util.List;

import static com.epam.java.rt.lab.dao.Parameter.Type.*;

/**
 * service-ms
 */
public class QueryBuilder {

    public enum Type {
        CREATE,
        READ,
        UPDATE,
        DELETE,
        FUNC
    }

    public enum OrderType {
        ASC,
        DESC
    }

    public static String getSql(Parameter parameter) {
        StringBuilder result = new StringBuilder();
        switch ((Type) parameter.get(_QUERY_TYPE)) {
            case FUNC:
                return funcQuery(result, parameter).toString();
            case READ:
                return result
                        .append(select(parameter))
                        .append(from(parameter))
                        .append(join(parameter))
                        .append(where(parameter))
                        .append(order(parameter))
                        .append(limit(parameter)).toString();
        }
        return null;
    }

    private static StringBuilder funcQuery(StringBuilder result, Parameter parameter) {
        switch ((String) parameter.get(_FUNC_NAME)) {
            case "COUNT":
                return result
                        .append("SELECT COUNT(*) AS count")
                        .append(from(parameter))
                        .append(join(parameter))
                        .append(where(parameter));
        }
        return result;
    }

    private static String select(Parameter parameter) {
        return "SELECT ".concat(StringArray.combine((List<String>) parameter.get(_SELECT_COLUMN_LIST), ", "));
    }

    private static String from(Parameter parameter) {
        return " FROM ".concat((String) parameter.get(_FROM_TABLE));
    }

    private static String join(Parameter parameter) {
        Object joinTables = parameter.get(_JOIN_TABLES);
        return joinTables == null ? "" : " JOIN ".concat((String) joinTables);
    }

    private static String where(Parameter parameter) {
        Object whereColumnList = parameter.get(_WHERE_COLUMN_LIST);
        return whereColumnList == null ? "" :
                " WHERE ".concat(StringArray.combine((List<Parameter.Field>) whereColumnList, " AND "));
    }

    private static String order(Parameter parameter) {
        Object orderColumnList = parameter.get(_ORDER_COLUMN_LIST);
        Object orderType = parameter.get(ORDER_TYPE);
        return orderColumnList == null || orderType == null ? "" :
                " ORDER BY ".concat(StringArray.combine((List<String>) orderColumnList, ", "))
                        .concat(orderType == OrderType.ASC ? " ASC" :
                                (orderType == OrderType.DESC ? " DESC" :
                                        (String) orderType));
    }

    private static String limit(Parameter parameter) {
        return (parameter.get(LIMIT_OFFSET) == null) || (parameter.get(LIMIT_COUNT) == null) ? "" :
                " LIMIT ".concat(String.valueOf(parameter.get(LIMIT_OFFSET)))
                        .concat(", ").concat(String.valueOf(parameter.get(LIMIT_COUNT)));
    }

}
