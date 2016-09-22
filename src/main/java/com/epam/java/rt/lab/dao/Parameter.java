package com.epam.java.rt.lab.dao;

import com.epam.java.rt.lab.dao.h2.QueryBuilder;
import com.epam.java.rt.lab.dao.query.Query;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * service-ms
 */
public class Parameter {

    public enum Type {
        // special types used to prepare sql query
        _QUERY_TYPE,
        _FUNC_NAME,
        _SELECT_COLUMN_LIST,
        _SET_FIELD_LIST,
        _FROM_TABLE,
        _JOIN_TABLES,
        _WHERE_COLUMN_LIST,
        _ORDER_COLUMN_LIST,
        // user/client types to set sql/dao parameters/arguments
        GENERATE_VALUE_ARRAY,   // String with generate values delimited by comma or string array of generate values
        RESULT_FIELD_ARRAY,     // String with field names delimited by comma or string array of field names
        LIMIT_OFFSET,           // Long value of offset
        LIMIT_COUNT,            // Long value of count
        ORDER_TYPE,             // Enum OrderType, could be ASC or DESC
        ORDER_FIELD_NAME_ARRAY, // String with field names delimited by comma or string array of field names
        SET_FIELD_ARRAY,        // Array of wrapper class type
        WHERE_FIELD_ARRAY,      // Array of wrapper class type
        CUSTOM                  // Extra type used to store mapped values/objects
    }

    private Map<Type, Object> argumentMap;

    public Parameter() {
        this.argumentMap = new HashMap<>();
    }

    public Parameter put(Type type, Object... valueArray) {
        this.argumentMap.put(type, valueArray.length == 1 ? valueArray[0] : valueArray);
        return this;
    }

    public Parameter generate(QueryBuilder.GenerateValueType... generateValueArray) {
        this.argumentMap.put(Type.GENERATE_VALUE_ARRAY, generateValueArray);
        return this;
    }

    public Parameter result(String... fieldNameArray) {
        this.argumentMap.put(Type.RESULT_FIELD_ARRAY, fieldNameArray);
        return this;
    }

    public Parameter limit(Long offset, Long count) {
        this.argumentMap.put(Type.LIMIT_OFFSET, offset);
        this.argumentMap.put(Type.LIMIT_COUNT, count);
        return this;
    }

    public Parameter order(QueryBuilder.OrderType orderType, String... fieldNames) {
        this.argumentMap.put(Type.ORDER_TYPE, orderType);
        this.argumentMap.put(Type.ORDER_FIELD_NAME_ARRAY, fieldNames);
        return this;
    }

    public Parameter values(Field... fieldArray) {
        this.argumentMap.put(Type.SET_FIELD_ARRAY, fieldArray);
        return this;
    }

    public Parameter filter(Field... fieldArray) {
        this.argumentMap.put(Type.WHERE_FIELD_ARRAY, fieldArray);
        return this;
    }

    public Object get(Type type) {
        return this.argumentMap.get(type);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<Type, Object> argument : this.argumentMap.entrySet())
            result.append("\n").append(argument.getKey()).append("=").append(argument.getValue());
        return result.toString();
    }

    public static String[] splitAndConvertFieldNames(String fieldName) {
        System.out.println("fieldName = " + fieldName);
        String[] result = {"", ""};
        String[] split = fieldName.split("\\.");
        if (split.length == 1) {
            result[1] = ".".concat(Parameter.getColumnName(split[0]));
        } else if (split.length == 2) {
            result[0] = "\"".concat(split[0].substring(0, 1).toUpperCase()).concat(split[0].substring(1)).concat("\"");
            result[1] = result[0].concat(".").concat(Parameter.getColumnName(split[1]));
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
