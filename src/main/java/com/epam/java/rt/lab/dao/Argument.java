package com.epam.java.rt.lab.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * service-ms
 */
public class Argument {
    private Map<Dao.ArgumentType, Object> argumentMap;

    public Argument() {
        this.argumentMap = new HashMap<>();
    }

    public Argument put(Dao.ArgumentType type, Object... valueArray) {
        this.argumentMap.put(type, valueArray.length == 1 ?
                valueArray[0] : new ArrayList<>(Arrays.asList(valueArray)));
        return this;
    }

    public Object get(Dao.ArgumentType type) {
        return this.argumentMap.get(type);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<Dao.ArgumentType, Object> argument : this.argumentMap.entrySet())
            result.append("\n").append(argument.getKey()).append("=").append(argument.getValue());
        return result.toString();
    }

    public static String[] splitSelectColumn(String fieldName) {
        System.out.println("fieldName = " + fieldName);
        String[] result = {"", ""};
        String[] split = fieldName.split("\\.");
        if (split.length == 1) {
            result[1] = ".".concat(Argument.getColumnName(split[0]));
        } else if (split.length == 2) {
            result[0] = "\"".concat(split[0].substring(0, 1).toUpperCase()).concat(split[0].substring(1)).concat("\"");
            result[1] = result[0].concat(".").concat(Argument.getColumnName(split[1]));
        }
        return result;
    }

    private static String getColumnName(String fieldName) {
        Pattern pattern = Pattern.compile("[A-Z]");
        Matcher matcher = pattern.matcher(fieldName);
        int startIndex = 0;
        while (matcher.find(startIndex)) {
            fieldName = fieldName.substring(0, matcher.start()).concat("_")
                    .concat(fieldName.substring(matcher.start(), matcher.end()).toLowerCase())
                    .concat(fieldName.substring(matcher.end()));
            matcher = pattern.matcher(fieldName);
        }
        return fieldName;
    }

    public static class Field <T> {
        private String name;
        private T value;
        private String compareFieldName;

        private Field(String name, T value, String compareFieldName) {
            this.name = name;
            this.value = value;
            this.compareFieldName = compareFieldName;
        }

        public static <T> Field set(String name, T value, String compareFieldName) {
            if (name == null || name.length() == 0) return null;
            return new Field(name, value, compareFieldName);
        }

        public static <T> Field set(String name, T value) {
            if (name == null || name.length() == 0) return null;
            return new Field(name, value, null);
        }

        public String getName() {
            return name;
        }

        public T getValue() {
            return value;
        }

        public String getCompareFieldName() {
            return compareFieldName;
        }

        @Override
        public boolean equals(Object obj) {
            Field field = (Field) obj;
            return field.getName().equals(this.name) &&
                    field.getValue().equals(this.value) &&
                    field.getCompareFieldName().equals(this.compareFieldName);
        }

        @Override
        public String toString() {
            return this.name.concat(
                    this.value == null && this.compareFieldName == null ?
                            " is null" : this.compareFieldName == null ?
                            " = ?" : " = ".concat(this.compareFieldName)
            );
        }
    }

}
