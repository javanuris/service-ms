package com.epam.java.rt.lab.web.validator;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.TimestampManager;
import com.epam.java.rt.lab.web.validator.Validator.ValidatorType;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.epam.java.rt.lab.exception.AppExceptionCode.*;
import static com.epam.java.rt.lab.web.validator.Validator.ValidatorType.*;
import static com.epam.java.rt.lab.web.validator.ValidatorExceptionCode.*;

public final class ValidatorFactory {

    public static final String DIGITS = "digits";

    private static final String TYPE = ".type";
    private static final String MSG = ".msg";
    private static final String NUMBER_CLASS = ".class";
    private static final String NUMBER_MIN = ".min";
    private static final String NUMBER_MAX = ".max";
    private static final String TIMESTAMP = ".timestamp";
    private static final String REGEX = ".regex";

    private static final String VALIDATOR_PROPERTY_FILE = "validator.properties";

    private static class Holder {

        private static final ValidatorFactory INSTANCE = new ValidatorFactory();

    }

    private Map<String, Validator> validatorMap = new HashMap<>();

    private ValidatorFactory() {
    }

    public static ValidatorFactory getInstance() {
        return Holder.INSTANCE;
    }

    public void initValidatorMap() throws AppException {
        ClassLoader classLoader = ValidatorFactory.class.getClassLoader();
        InputStream inputStream = classLoader.
                getResourceAsStream(VALIDATOR_PROPERTY_FILE);
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            this.validatorMap.clear();
            Enumeration<?> names = properties.propertyNames();
            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                int typeIndex = name.lastIndexOf(TYPE);
                if ((name.length() - TYPE.length()) == typeIndex) {
                    String type = properties.getProperty(name);
                    name = name.substring(0, typeIndex);
                    addValidatorFromProperties(properties, type, name);
                }
            }
            if (this.validatorMap.size() == 0) {
                String[] detailArray = {VALIDATOR_PROPERTY_FILE};
                throw new AppException(PROPERTY_EMPTY_OR_CONTENT_ERROR,
                        detailArray);
            }
        } catch (IOException e) {
            String[] detailArray = {VALIDATOR_PROPERTY_FILE};
            throw new AppException(PROPERTY_READ_ERROR,
                    e.getMessage(), e.getCause(), detailArray);
        }
    }

    private void addValidatorFromProperties(Properties properties,
                                            String validatorTypName,
                                            String validatorName)
            throws AppException {
        ValidatorType type = ValidatorType.valueOf(validatorTypName);
        String validatorMessage = properties.
                getProperty(validatorName + MSG);
        Validator validator = null;
        switch (type) {
            case BOOLEAN:
                validator = Validator.getBoolean(validatorMessage);
                break;
            case BOOLEAN_OR_NULL:
                validator = Validator.getBooleanOrNull(validatorMessage);
                break;
            case NUMBER:
            case NUMBER_OR_NULL:
                String numberClass = properties.
                        getProperty(validatorName + NUMBER_CLASS);
                String numberMinString = properties.
                        getProperty(validatorName + NUMBER_MIN);
                Number numberMin = getNumberValue(numberClass, numberMinString);
                String numberMaxString = properties.
                        getProperty(validatorName + NUMBER_MAX);
                Number numberMax = getNumberValue(numberClass, numberMaxString);
                if (NUMBER.equals(type)) {
                    validator = Validator.getNumber(validatorMessage,
                            numberMin, numberMax);
                } else {
                    validator = Validator.getNumberOrNull(validatorMessage,
                            numberMin, numberMax);
                }
                break;
            case FUTURE:
            case FUTURE_OR_NULL:
                String futureString = properties.
                        getProperty(validatorName + TIMESTAMP);
                if (!TimestampManager.isTimestamp(futureString)) {
                    String[] detailArray = {futureString};
                    throw new AppException(NOT_TIMESTAMP_VALUE_ERROR,
                            detailArray);
                }
                Timestamp future = Timestamp.valueOf(futureString);
                if (FUTURE.equals(type)) {
                    validator = Validator.getFuture(validatorMessage, future);
                } else {
                    validator = Validator.getFutureOrNull(validatorMessage,
                            future);
                }
                break;
            case PAST:
            case PAST_OR_NULL:
                String pastString = properties.
                        getProperty(validatorName + TIMESTAMP);
                if (!TimestampManager.isTimestamp(pastString)) {
                    String[] detailArray = {pastString};
                    throw new AppException(NOT_TIMESTAMP_VALUE_ERROR,
                            detailArray);
                }
                Timestamp past = Timestamp.valueOf(pastString);
                if (PAST.equals(type)) {
                    validator = Validator.getPast(validatorMessage, past);
                } else {
                    validator = Validator.getPastOrNull(validatorMessage,
                            past);
                }
                break;
            case PATTERN:
            case PATTERN_OR_NULL:
                String regex = properties.
                        getProperty(validatorName + REGEX);
                if (PATTERN.equals(type)) {
                    validator = Validator.getPattern(validatorMessage, regex);
                } else {
                    validator = Validator.getPatternOrNull(validatorMessage,
                            regex);
                }
                break;
        }
        if (validator != null) this.validatorMap.put(validatorName, validator);
    }

    private <T> T getNumberValue(String numberClassName,
                                 String stringValue)
            throws AppException {
        try {
            if (numberClassName == null) {
                throw new AppException(NULL_NOT_ALLOWED);
            }
            Class<?> numberClass = Class.forName(numberClassName);
            if (!Number.class.equals(numberClass.getSuperclass())) {
                String[] detailArray = {numberClassName, stringValue};
                throw new AppException(NOT_NUMBER_CLASS_ERROR, detailArray);
            }
            Method numberMethod = numberClass.
                    getMethod("valueOf", String.class);
            return (T) numberClass.cast(numberMethod.invoke(null, stringValue));
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            String[] detailArray = {numberClassName, stringValue};
            throw new AppException(CLASS_OR_METHOD_NOT_FOUND,
                    e.getMessage(), e.getCause(), detailArray);
        } catch (InvocationTargetException | IllegalAccessException e) {
            String[] detailArray = {numberClassName, stringValue};
            throw new AppException(METHOD_INVOKE_ERROR,
                    e.getMessage(), e.getCause(), detailArray);
        }
    }

    public Validator create(String validatorName) throws AppException {
        if (validatorName == null) throw new AppException(NULL_NOT_ALLOWED);
        Validator validator = this.validatorMap.get(validatorName);
        if (validator == null) {
            String[] detailArray = {validatorName};
            throw new AppException(VALIDATOR_NOT_FOUND, detailArray);
        }
        return validator;
    }

}
