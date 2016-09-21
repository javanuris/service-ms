package com.epam.java.rt.lab.util;

import java.util.List;

/**
 * service-ms
 */
public class StringArray {
    public static final String DELIMITER_COMMA_AND_SPACE = ", ";

    public static String[] splitSpaceLessNames(String source, String regex) {
        return source.replaceAll(" ", "").split(regex);
    }

    public static String combineList(List<?> stringList, String delimiter) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Object string : stringList) {
            if (first) {
                first = false;
            } else {
                result.append(delimiter);
            }
            result.append(string);
        }
        return result.toString();
    }

}
