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
        return value != null && value.matches(getProperty("digits.regex"));
    }

    public static boolean validate(HttpServletRequest req, FormComponent formComponent) {
        logger.debug("VALIDATE");
        Validator validator;
        boolean result = true;
        for (int i = 0; i < formComponent.getItemArrayLength(); i++) {
            FormComponent.Item formItem = formComponent.getItem(i);
            formItem.setValue(req.getParameter(formItem.getLabel()));
            logger.debug("{} = {}", formItem.getLabel(), formItem.getValue());
            if (!formItem.getIgnoreValidate()) {
                String[] validationMessageArray = {};
                validator = formItem.getValidator();
                if (validator != null) {
                    logger.debug("validator = {}", validator.getType());
                    if (validator.getType().equals(Validator.ValidatorType.BOOLEAN)) {
                        if (!isParsable(formItem.getValue()) ||
                                (!Boolean.TRUE.toString().equals(formItem.getValue()) &&
                                        !Boolean.FALSE.toString().equals(formItem.getValue()))) {
                            result = false;
                            validationMessageArray = new String[]{validator.validationMessage};
                        }
                    } else if (validator.getType().equals(Validator.ValidatorType.BOOLEAN_OR_NULL)) {
                        if (isParsable(formItem.getValue()) &&
                                !Boolean.TRUE.toString().equals(formItem.getValue()) &&
                                !Boolean.FALSE.toString().equals(formItem.getValue())) {
                            result = false;
                            validationMessageArray = new String[]{validator.validationMessage};
                        }
                    } else if (validator.getType().equals(Validator.ValidatorType.NUMBER) ||
                            validator.getType().equals(Validator.ValidatorType.NUMBER_OR_NULL)) {
                        if (isParsable(formItem.getValue()) || validator.getType().equals(Validator.ValidatorType.NUMBER)) {
                            if (!isParsable(formItem.getValue())) {
                                validationMessageArray = new String[]{validator.validationMessage};
                            } else {
                                Number number = NumberCompare.getNumber(formItem.getValue(), validator.numberMin);
                                NumberCompare numberCompare = new NumberCompare();
                                if ((validator.numberMin != null && (numberCompare.compare(validator.numberMin, number) > 0)) ||
                                        (validator.numberMax != null && (numberCompare.compare(number, validator.numberMax) > 0))) {
                                    result = false;
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
                                        validationMessageArray = new String[]{validator.validationMessage, range};
                                    } else {
                                        validationMessageArray = new String[]{validator.validationMessage};
                                    }
                                }
                            }
                        }
                    } else if (validator.getType().equals(Validator.ValidatorType.PATTERN)) {
                        if (formItem.getValue() == null || !formItem.getValue().matches(validator.getRegex())) {
                            result = false;
                            validationMessageArray = new String[]{validator.validationMessage};
                        }
                    } else if (validator.getType().equals(Validator.ValidatorType.PATTERN_OR_NULL)) {
                        if (formItem.getValue() != null && !formItem.getValue().matches(validator.getRegex())) {
                            result = false;
                            validationMessageArray = new String[]{validator.validationMessage};
                        }
                    }
                    if (validationMessageArray.length > 0) formItem.setValidationMessageArray(validationMessageArray);
                }
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
        private String validationMessage;

        public enum ValidatorType {
            BOOLEAN, NUMBER, FUTURE, PAST, PATTERN,
            BOOLEAN_OR_NULL, NUMBER_OR_NULL, FUTURE_OR_NULL, PAST_OR_NULL, PATTERN_OR_NULL
        }

        private Validator(ValidatorType type, String validationMessage) {
            this.type = type;
            this.validationMessage = validationMessage;
        }

        public static Validator getBoolean(String validationMessage) {
            return new Validator(ValidatorType.BOOLEAN, validationMessage);
        }

        public static <T extends Number> Validator getNumber(T numberMin, T numberMax, String validationMessage) {
            Validator validator = new Validator<T>(ValidatorType.NUMBER, validationMessage);
            validator.numberMin = numberMin;
            validator.numberMax = numberMax;
            return validator;
        }

        private static Validator getTimestamp(ValidatorType validatorType, Timestamp timestamp, String validationMessage) {
            Validator validator = new Validator(validatorType, validationMessage);
            validator.compareTimestamp = timestamp;
            return validator;
        }

        public static Validator getFuture(Timestamp timestamp, String validationMessage) {
            return getTimestamp(ValidatorType.FUTURE, timestamp, validationMessage);
        }

        public static Validator getPast(Timestamp timestamp, String validationMessage) {
            return getTimestamp(ValidatorType.PAST, timestamp, validationMessage);
        }

        public static Validator getPattern(String regex, String validationMessage) {
            Validator validator = new Validator(ValidatorType.PATTERN, validationMessage);
            validator.regex = regex;
            return validator;
        }

        public static Validator getBooleanOrNull(String validationMessage) {
            return new Validator(ValidatorType.BOOLEAN_OR_NULL, validationMessage);
        }

        public static <T extends Number> Validator getNumberOrNull(T numberMin, T numberMax, String validationMessage) {
            Validator validator = new Validator<T>(ValidatorType.NUMBER_OR_NULL, validationMessage);
            validator.numberMin = numberMin;
            validator.numberMax = numberMax;
            return validator;
        }

        public static Validator getFutureOrNull(Timestamp timestamp, String validationMessage) {
            return getTimestamp(ValidatorType.FUTURE_OR_NULL, timestamp, validationMessage);
        }

        public static Validator getPastOrNull(Timestamp timestamp, String validationMessage) {
            return getTimestamp(ValidatorType.PAST_OR_NULL, timestamp, validationMessage);
        }

        public static Validator getPatternOrNull(String regex, String validationMessage) {
            Validator validator = new Validator(ValidatorType.PATTERN_OR_NULL, validationMessage);
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

        public String getValidationMessage() {
            return validationMessage;
        }
    }

}
