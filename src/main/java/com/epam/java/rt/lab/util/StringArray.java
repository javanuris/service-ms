package com.epam.java.rt.lab.util;

import com.epam.java.rt.lab.dao.sql.Clause;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * service-ms
 */
public class StringArray {

    public static final String SPACE = " ";

    public static String[] splitSpaceLessNames(String source, String regex) {
        return source.replaceAll(SPACE, "").split(regex);
    }

    public static <T> String combine(List<T> sourceList, String delimiter) throws Exception {
        return combine(new StringBuilder(), sourceList, delimiter).toString();
    }

    public static <T> StringBuilder combine(StringBuilder result, List<T> sourceList, String delimiter) throws Exception {
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

}
