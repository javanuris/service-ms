package com.epam.java.rt.lab.util;

import com.epam.java.rt.lab.component.FormComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Timestamp;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * service-ms
 */
public class FormManager {
    private static final Logger logger = LoggerFactory.getLogger(FormManager.class);
    private static final Properties patternProperties = new Properties();
    private static Lock propertiesLock = new ReentrantLock();

    private FormManager() {
    }

    public static void init() {
        if (propertiesLock.tryLock()) {
            try {
                patternProperties.load(FormManager.class.getClassLoader().getResourceAsStream("pattern.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            propertiesLock.unlock();
        }
    }

    public static String getProperty(String key) {
        if (patternProperties.size() == 0) init();
        return patternProperties.getProperty(key);
    }

    public static boolean isOnlyDigits(String value) {
        return value != null && value.matches(getProperty("digits"));
    }

    public static boolean validate(HttpServletRequest req, FormComponent formComponent) {
        logger.debug("VALIDATE");
        boolean result = true;
        for (int i = 0; i < formComponent.getItemArrayLength(); i++) {
            FormComponent.Item formItem = formComponent.getItem(i);
            formItem.setValue(req.getParameter(formItem.getLabel()));
            logger.debug("{} = {}", formItem.getLabel(), formItem.getValue());
            for (Validator validator : formItem.getValidatorList()) {
                String[] validationMessageArray = {};
                switch (validator.getType()) {
                    case BOOLEAN:
                        if (!isParsable(formItem.getValue()) ||
                                (!Boolean.TRUE.toString().equals(formItem.getValue()) &&
                                        !Boolean.FALSE.toString().equals(formItem.getValue()))) {
                            result = false;
                            validationMessageArray = new String[]{"validation.boolean"};
                        }
                        break;
                    case BOOLEAN_OR_NULL:
                        if (isParsable(formItem.getValue()) &&
                                !Boolean.TRUE.toString().equals(formItem.getValue()) &&
                                !Boolean.FALSE.toString().equals(formItem.getValue())) {
                            result = false;
                            validationMessageArray = new String[]{"validation.boolean-or-null"};

                        }
                        break;
                    case NUMBER:
                    case NUMBER_OR_NULL:
                        if (isParsable(formItem.getValue()) || validator.getType().equals(Validator.ValidatorType.NUMBER)) {
                            if (!isParsable(formItem.getValue())) {
                                validationMessageArray = new String[]{"validation.number"};
                            } else {
                                Number number = NumberCompare.getNumber(formItem.getValue(), validator.numberMin);
                                NumberCompare numberCompare = new NumberCompare();
                                if ((validator.numberMin != null && (numberCompare.compare(validator.numberMin, number) > 0)) ||
                                        (validator.numberMax != null && (numberCompare.compare(number, validator.numberMax) > 0))) {
                                    result = false;
                                    String message;
                                    if (validator.getType().equals(Validator.ValidatorType.NUMBER)) {
                                        message = "validation.number";
                                    } else {
                                        message = "validation.number-or-null";
                                    }
                                    if (validator.numberMin != null || validator.numberMax != null) {
                                        String range;
                                        if (validator.numberMin != null && validator.numberMax != null) {
                                            range = validator.numberMin.toString().concat(" .. ")
                                                    .concat(validator.numberMax.toString());
                                        } else if (validator.numberMin != null) {
                                            range = ">= ".concat(validator.numberMin.toString());
                                        } else {
                                            range = "<= ".concat(validator.numberMax.toString());
                                        }
                                        validationMessageArray = new String[]{message, range};
                                    } else {
                                        validationMessageArray = new String[]{message};
                                    }
                                }
                            }
                        }
                        break;
                    case PATTERN:
                        if (formItem.getValue() == null || formItem.getValue().matches())
                        if (isParsable(formItem.getValue())) {

                        }

                }
                if (validationMessageArray.length > 0) formItem.setValidationMessageArray(validationMessageArray);
            }
        }
        return result;
    }

    private static boolean isParsable(String value) {
        return (value != null && value.length() > 0);
    }

    public static class Validator<T extends Number> {
        private ValidatorType type;
        private T numberMin;
        private T numberMax;
        private Timestamp compareTimestamp;
        private String regex;

        public enum ValidatorType {
            BOOLEAN, NUMBER, FUTURE, PAST, PATTERN,
            BOOLEAN_OR_NULL, NUMBER_OR_NULL, FUTURE_OR_NULL, PAST_OR_NULL, PATTERN_OR_NULL
        }

        private Validator(ValidatorType type) {
            this.type = type;
        }

        public static Validator getBoolean() {
            return new Validator(ValidatorType.BOOLEAN);
        }

        public static <T extends Number> Validator getNumber(T numberMin, T numberMax) {
            Validator validator = new Validator<T>(ValidatorType.NUMBER);
            validator.numberMin = numberMin;
            validator.numberMax = numberMax;
            return validator;
        }

        private static Validator getTimestamp(ValidatorType validatorType, Timestamp timestamp) {
            Validator validator = new Validator(validatorType);
            validator.compareTimestamp = timestamp;
            return validator;
        }

        public static Validator getFuture(Timestamp timestamp) {
            return getTimestamp(ValidatorType.FUTURE, timestamp);
        }

        public static Validator getPast(Timestamp timestamp) {
            return getTimestamp(ValidatorType.PAST, timestamp);
        }

        public static Validator getPattern(String regex) {
            Validator validator = new Validator(ValidatorType.PATTERN);
            validator.regex = regex;
            return validator;
        }

        public static Validator getBooleanOrNull() {
            return new Validator(ValidatorType.BOOLEAN_OR_NULL);
        }

        public static <T extends Number> Validator getNumberOrNull(T numberMin, T numberMax) {
            Validator validator = new Validator<T>(ValidatorType.NUMBER_OR_NULL);
            validator.numberMin = numberMin;
            validator.numberMax = numberMax;
            return validator;
        }

        public static Validator getFutureOrNull(Timestamp timestamp) {
            return getTimestamp(ValidatorType.FUTURE_OR_NULL, timestamp);
        }

        public static Validator getPastOrNull(Timestamp timestamp) {
            return getTimestamp(ValidatorType.PAST_OR_NULL, timestamp);
        }

        public static Validator getPatternOrNull(String regex) {
            Validator validator = new Validator(ValidatorType.PATTERN_OR_NULL);
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
    }

}
