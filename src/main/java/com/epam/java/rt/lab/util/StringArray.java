package com.epam.java.rt.lab.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * service-ms
 */
public class StringArray {
    public static final String DELIMITER_COMMA_AND_SPACE = ", ";

    public static String[] splitSpaceLessNames(String source, String regex) {
        return source.replaceAll(" ", "").split(regex);
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
            result.append(source);
        }
        return result.toString();
    }

}
