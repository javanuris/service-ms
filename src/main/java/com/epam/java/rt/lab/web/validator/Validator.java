package com.epam.java.rt.lab.web.validator;

import com.epam.java.rt.lab.util.NumberCompare;

import java.sql.Timestamp;

/**
 * category-ms
 */
public class Validator<T extends Number> {
    private ValidatorType type;
    private String msg;
    private T numberMin;
    private T numberMax;
    private Timestamp compareTimestamp;
    private String regex;

    enum ValidatorType {
        BOOLEAN, NUMBER, FUTURE, PAST, PATTERN,
        BOOLEAN_OR_NULL, NUMBER_OR_NULL, FUTURE_OR_NULL, PAST_OR_NULL, PATTERN_OR_NULL
    }

    Validator(ValidatorType type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    static Validator getBoolean(String msg) {
        return new Validator(ValidatorType.BOOLEAN, msg);
    }

    static <T extends Number> Validator getNumber(String msg, T numberMin, T numberMax) {
        Validator validator = new Validator<T>(ValidatorType.NUMBER, msg);
        validator.numberMin = numberMin;
        validator.numberMax = numberMax;
        return validator;
    }

    private static Validator getTimestamp(ValidatorType validatorType, String msg, Timestamp timestamp) {
        Validator validator = new Validator(validatorType, msg);
        validator.compareTimestamp = timestamp;
        return validator;
    }

    static Validator getFuture(String msg, Timestamp timestamp) {
        return getTimestamp(ValidatorType.FUTURE, msg, timestamp);
    }

    static Validator getPast(String msg, Timestamp timestamp) {
        return getTimestamp(ValidatorType.PAST, msg, timestamp);
    }

    static Validator getPattern(String msg, String regex) {
        Validator validator = new Validator(ValidatorType.PATTERN, msg);
        validator.regex = regex;
        return validator;
    }

    static Validator getBooleanOrNull(String msg) {
        return new Validator(ValidatorType.BOOLEAN_OR_NULL, msg);
    }

    static <T extends Number> Validator getNumberOrNull(String msg, T numberMin, T numberMax) {
        Validator validator = new Validator<T>(ValidatorType.NUMBER_OR_NULL, msg);
        validator.numberMin = numberMin;
        validator.numberMax = numberMax;
        return validator;
    }

    static Validator getFutureOrNull(String msg, Timestamp timestamp) {
        return getTimestamp(ValidatorType.FUTURE_OR_NULL, msg, timestamp);
    }

    static Validator getPastOrNull(String msg, Timestamp timestamp) {
        return getTimestamp(ValidatorType.PAST_OR_NULL, msg, timestamp);
    }

    static Validator getPatternOrNull(String msg, String regex) {
        Validator validator = new Validator(ValidatorType.PATTERN_OR_NULL, msg);
        validator.regex = regex;
        return validator;
    }

    public ValidatorType getType() {
        return type;
    }

    public T getNumberMin() {
        return numberMin;
    }

    public T getNumberMax() {
        return numberMax;
    }

    public Timestamp getCompareTimestamp() {
        return compareTimestamp;
    }

    public String getRegex() {
        return regex;
    }

    public String getMsg() {
        return msg;
    }

    private boolean isParsable(String value) {
        return (value != null && value.length() > 0);
    }

    public String[] validate(String value) {
        if (this.type.equals(ValidatorType.BOOLEAN)) {
            if (!isParsable(value) ||
                    (!Boolean.TRUE.toString().equals(value) &&
                            !Boolean.FALSE.toString().equals(value)))
                return new String[]{this.msg};
        } else if (this.type.equals(ValidatorType.BOOLEAN_OR_NULL)) {
            if (isParsable(value) &&
                    !Boolean.TRUE.toString().equals(value) &&
                    !Boolean.FALSE.toString().equals(value))
                return new String[]{this.msg};
        } else if (this.type.equals(ValidatorType.NUMBER) ||
                this.type.equals(ValidatorType.NUMBER_OR_NULL)) {
            if (isParsable(value) || this.type.equals(ValidatorType.NUMBER)) {
                try {
                    if (!isParsable(value)) return new String[]{this.msg};
                    Number number = NumberCompare.getNumber(value, this.numberMin);
                    NumberCompare numberCompare = new NumberCompare();
                    if ((this.numberMin != null && (numberCompare.compare(this.numberMin, number) > 0)) ||
                            (this.numberMax != null && (numberCompare.compare(number, this.numberMax) > 0))) {
                        if (this.numberMin != null || this.numberMax != null) {
                            String range;
                            if (this.numberMin != null && this.numberMax != null) {
                                range = this.numberMin.toString().concat(" .. ")
                                        .concat(this.numberMax.toString());
                            } else if (this.numberMin != null) {
                                range = ">= ".concat(this.numberMin.toString());
                            } else {
                                range = "<= ".concat(this.numberMax.toString());
                            }
                            return new String[]{this.msg, range};
                        } else {
                            return new String[]{this.msg};
                        }
                    }
                } catch (ClassCastException | NumberFormatException e) {
                    return new String[]{this.msg};
                }
            }
            // TODO: FUTURE AND PAST VALIDATION (EMPTY TIMESTAMP MEANS CURRENT)
        } else if (this.type.equals(ValidatorType.PATTERN)) {
            if (value == null || !value.matches(this.regex))
                return new String[]{this.msg};
        } else if (this.type.equals(ValidatorType.PATTERN_OR_NULL)) {
            if (value != null && !value.matches(this.regex))
                return new String[]{this.msg};
        }
        return new String[]{};
    }

}