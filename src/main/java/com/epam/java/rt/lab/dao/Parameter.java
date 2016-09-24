package com.epam.java.rt.lab.dao;

import com.epam.java.rt.lab.dao.types.OrderType;
import com.epam.java.rt.lab.dao.types.ParameterType;
import com.epam.java.rt.lab.entity.EntityProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * service-ms
 */
public class Parameter {

    private Map<ParameterType, Object> parameterMap;

    public Parameter() {
        this.parameterMap = new HashMap<>();
    }

    public Parameter put(ParameterType type, Object... valueArray) {
        this.parameterMap.put(type, valueArray.length == 1 ? valueArray[0] : valueArray);
        return this;
    }

    public Parameter result(EntityProperty... entityPropertyArray) {
        this.parameterMap.put(ParameterType.RESULT_PROPERTY_ARRAY, entityPropertyArray);
        return this;
    }

    public Parameter limit(Long offset, Long count) {
        this.parameterMap.put(ParameterType.LIMIT_OFFSET, offset);
        this.parameterMap.put(ParameterType.LIMIT_COUNT, count);
        return this;
    }

    public Parameter order(OrderType orderType, EntityProperty... entityPropertyArray) {
        this.parameterMap.put(ParameterType.ORDER_TYPE, orderType);
        this.parameterMap.put(ParameterType.ORDER_PROPERTY_ARRAY, entityPropertyArray);
        return this;
    }

    public Parameter values(Field... fieldArray) {
        this.parameterMap.put(ParameterType.SET_FIELD_ARRAY, fieldArray);
        return this;
    }

    public Parameter filter(Field... fieldArray) {
        this.parameterMap.put(ParameterType.WHERE_FIELD_ARRAY, fieldArray);
        return this;
    }

    public Object get(ParameterType type) {
        return this.parameterMap.get(type);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<ParameterType, Object> argument : this.parameterMap.entrySet())
            result.append("\n").append(argument.getKey()).append("=").append(argument.getValue());
        return result.toString();
    }

    public static String[] splitAndConvertFieldNames(String fieldName) {
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
        private EntityProperty entityProperty;
        private T value;
        private EntityProperty compareEntityProperty;

        private Field(EntityProperty entityProperty, T value, EntityProperty compareEntityProperty) {
            this.entityProperty = entityProperty;
            this.value = value;
            this.compareEntityProperty = compareEntityProperty;
        }

        public static <T> Field set(EntityProperty entityProperty, EntityProperty compareEntityProperty) {
            if (entityProperty == null || compareEntityProperty == null) return null;
            return new Field(entityProperty, null, compareEntityProperty);
        }

        public static <T> Field set(EntityProperty entityProperty, T value) {
            if (entityProperty == null) return null;
            return new Field(entityProperty, value, null);
        }

        public EntityProperty getEntityProperty() {
            return this.entityProperty;
        }

        public T getValue() {
            return value;
        }

        public EntityProperty getCompareEntityProperty() {
            return this.compareEntityProperty;
        }

        @Override
        public boolean equals(Object obj) {
            Field field = (Field) obj;
            return field != null && (field.entityProperty.equals(this.entityProperty) &&
                    field.value.equals(this.value) && field.compareEntityProperty.equals(this.compareEntityProperty));
        }

        @Override
        public String toString() {
            return this.entityProperty.toString().concat(
                    this.value == null && this.compareEntityProperty == null ?
                            " is null" : this.compareEntityProperty == null ?
                            " = ?" : " = ".concat(this.compareEntityProperty.toString())
            );
        }
    }

}
