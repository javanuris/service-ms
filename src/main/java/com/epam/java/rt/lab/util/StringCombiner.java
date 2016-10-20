package com.epam.java.rt.lab.util;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.sql.Clause;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.exception.AppExceptionCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;
import static com.epam.java.rt.lab.util.PropertyManager.EQUAL;

/**
 * category-ms
 */
public class StringCombiner {

    public static final String SPACE = " ";

    public static String[] splitSpaceLessNames(String source, String regex) {
        return source.replaceAll(SPACE, "").split(regex);
    }

    public static <T> String combine(List<T> sourceList, String delimiter) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (T source : sourceList) {
            if (first) {
                first = false;
            } else {
                result.append(delimiter);
            }
            result.append(source.toString());
        }
        return result.toString();
    }

    public static <T> StringBuilder combine(StringBuilder result, List<T> sourceList, String delimiter) throws DaoException {
        boolean first = true;
        for (T source : sourceList) {
            if (first) {
                first = false;
            } else {
                result.append(delimiter);
            }
            if (source instanceof Clause) {
                ((Clause) source).appendClause(result);
            } else {
                result.append(source.toString());
            }
        }
        return result;
    }

    public static String combine(Map<String, String> map, String delimiter)
            throws AppException {
        if (map == null || delimiter == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        StringBuilder result = new StringBuilder();
        for (Map.Entry entry : map.entrySet()) {
            result.append(delimiter).append(entry.getKey()).
                    append(EQUAL).append(entry.getValue());
        }
        if (result.length() < delimiter.length()) return "";
        return result.toString().substring(delimiter.length());
    }

}
