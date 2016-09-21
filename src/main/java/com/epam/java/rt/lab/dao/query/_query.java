package com.epam.java.rt.lab.dao.query;

import com.epam.java.rt.lab.dao.Argument;
import com.epam.java.rt.lab.dao.Dao;
import com.epam.java.rt.lab.util.StringArray;

import java.util.List;

/**
 * service-ms
 */
public class _query {

    public static String create(Argument argument) {
        StringBuilder result = new StringBuilder();
        switch ((Dao.QueryType) argument.get(Dao.ArgumentType.QUERY_TYPE)) {
            case FUNC:
                return funcQuery(result, argument).toString();
            case READ:
                return result
                        .append(select(argument))
                        .append(from(argument))
                        .append(join(argument))
                        .append(where(argument))
                        .append(order(argument))
                        .append(limit(argument)).toString();
        }
        return null;
    }

    private static StringBuilder funcQuery(StringBuilder result, Argument argument) {
        switch ((String) argument.get(Dao.ArgumentType.FUNC_NAME)) {
            case "COUNT":
                return result
                        .append("SELECT COUNT(*) AS count FROM ")
                        .append(argument.get(Dao.ArgumentType.FROM_TABLE))
                        .append(";");
        }
        return result;
    }

    private static String select(Argument argument) {
        return "SELECT ".concat(StringArray.combineList((List<?>) argument.get(Dao.ArgumentType.SELECT_COLUMN_LIST), ", "));
    }

    private static String from(Argument argument) {
        return " FROM ".concat((String) argument.get(Dao.ArgumentType.FROM_TABLE));
    }

    private static String join(Argument argument) {
        return argument.get(Dao.ArgumentType.JOIN_TABLES) == null ? "" :
                " JOIN ".concat((String) argument.get(Dao.ArgumentType.JOIN_TABLES));
    }

    private static String where(Argument argument) {
        return argument.get(Dao.ArgumentType.WHERE_LIST) == null ? "" :
                " WHERE ".concat(StringArray.combineList((List<Argument.Field>) argument.get(Dao.ArgumentType.WHERE_LIST), " AND "));
    }

    private static String order(Argument argument) {
        return argument.get(Dao.ArgumentType.ORDER_COLUMNS) == null || argument.get(Dao.ArgumentType.ORDER_TYPE) == null ? "" :
                " ORDER BY ".concat((String) argument.get(Dao.ArgumentType.ORDER_COLUMNS))
                        .concat(argument.get(Dao.ArgumentType.ORDER_TYPE) == Dao.OrderType.ASC ? " ASC" :
                                argument.get(Dao.ArgumentType.ORDER_TYPE) == Dao.OrderType.DESC ? " DESC" :
                                        (String) argument.get(Dao.ArgumentType.ORDER_TYPE));
    }

    private static String limit(Argument argument) {
        return argument.get(Dao.ArgumentType.LIMIT_OFFSET) == null || argument.get(Dao.ArgumentType.LIMIT_COUNT) == null ? "" :
                " LIMIT ".concat(String.valueOf(argument.get(Dao.ArgumentType.LIMIT_OFFSET)))
                        .concat(", ").concat(String.valueOf(argument.get(Dao.ArgumentType.LIMIT_COUNT)));
    }
}
