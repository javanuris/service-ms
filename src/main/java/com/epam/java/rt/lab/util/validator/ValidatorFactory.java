package com.epam.java.rt.lab.util.validator;

import com.epam.java.rt.lab.web.component.form.FormFactory;
import com.epam.java.rt.lab.util.StringArray;
import com.epam.java.rt.lab.util.validator.Validator.ValidatorType;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * category-ms
 */
public final class ValidatorFactory {

    private static final String VALIDATORS = "validators";
    private static final String VALIDATOR_TYPE = ".type";
    private static final String VALIDATOR_MSG = ".msg";
    private static final String VALIDATOR_NUMBER_CLASS = ".class";
    private static final String VALIDATOR_NUMBER_MIN = ".min";
    private static final String VALIDATOR_NUMBER_MAX = ".max";
    private static final String VALIDATOR_TIMESTAMP = ".timestamp";
    private static final String VALIDATOR_REGEX = ".regex";

    private static Map<String, Validator> validatorMap = new HashMap<>();

    private static <T> T getNumberValue(String numberClassName, String stringValue) throws ValidatorException {
        try {
            Class numberClass = Class.forName(numberClassName);
            Method numberMethod = numberClass.getMethod("valueOf", String.class);
            return (T) numberClass.cast(numberMethod.invoke(null, stringValue));
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new ValidatorException("exception.validator.validator-factory.create-number-value.for-name", e.getCause());
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ValidatorException("exception.validator.validator-factory.create-number-value.invoke", e.getCause());
        }
    }

    private static void loadProperties() throws ValidatorException {
        Properties properties = new Properties();
        try {
            properties.load(FormFactory.class.getClassLoader().getResourceAsStream("validator.properties"));
            ValidatorFactory.validatorMap.clear();
            for (String validatorName : StringArray.splitSpaceLessNames(properties.getProperty(VALIDATORS), ",")) {
                ValidatorType type = ValidatorType.valueOf(properties.getProperty(validatorName.concat(VALIDATOR_TYPE)));
                Validator validator = null;
                switch (type) {
                    case BOOLEAN:
                        validator = Validator.getBoolean(properties.getProperty(validatorName.concat(VALIDATOR_MSG)));
                        break;
                    case NUMBER:
                        validator = Validator.getNumber(properties.getProperty(validatorName.concat(VALIDATOR_MSG)),
                                getNumberValue(properties.getProperty(validatorName.concat(VALIDATOR_NUMBER_CLASS)),
                                        properties.getProperty(validatorName.concat(VALIDATOR_NUMBER_MIN))),
                                getNumberValue(properties.getProperty(validatorName.concat(VALIDATOR_NUMBER_CLASS)),
                                        properties.getProperty(validatorName.concat(VALIDATOR_NUMBER_MAX)))
                        );
                        break;
                    case FUTURE:
                        validator = Validator.getFuture(properties.getProperty(validatorName.concat(VALIDATOR_MSG)),
                                Timestamp.valueOf(properties.getProperty(validatorName.concat(VALIDATOR_TIMESTAMP)))
                        );
                        break;
                    case PAST:
                        validator = Validator.getPast(properties.getProperty(validatorName.concat(VALIDATOR_MSG)),
                                Timestamp.valueOf(properties.getProperty(validatorName.concat(VALIDATOR_TIMESTAMP)))
                        );
                        break;
                    case PATTERN:
                        validator = Validator.getPattern(properties.getProperty(validatorName.concat(VALIDATOR_MSG)),
                                properties.getProperty(validatorName.concat(VALIDATOR_REGEX))
                        );
                        break;
                    case BOOLEAN_OR_NULL:
                        validator = Validator.getBooleanOrNull(properties.getProperty(validatorName.concat(VALIDATOR_MSG)));
                        break;
                    case NUMBER_OR_NULL:
                        validator = Validator.getNumberOrNull(properties.getProperty(validatorName.concat(VALIDATOR_MSG)),
                                getNumberValue(properties.getProperty(validatorName.concat(VALIDATOR_NUMBER_CLASS)),
                                        properties.getProperty(validatorName.concat(VALIDATOR_NUMBER_MIN))),
                                getNumberValue(properties.getProperty(validatorName.concat(VALIDATOR_NUMBER_CLASS)),
                                        properties.getProperty(validatorName.concat(VALIDATOR_NUMBER_MAX)))
                        );
                        break;
                    case FUTURE_OR_NULL:
                        validator = Validator.getFutureOrNull(properties.getProperty(validatorName.concat(VALIDATOR_MSG)),
                                Timestamp.valueOf(properties.getProperty(validatorName.concat(VALIDATOR_TIMESTAMP)))
                        );
                        break;
                    case PAST_OR_NULL:
                        validator = Validator.getPastOrNull(properties.getProperty(validatorName.concat(VALIDATOR_MSG)),
                                Timestamp.valueOf(properties.getProperty(validatorName.concat(VALIDATOR_TIMESTAMP)))
                        );
                        break;
                    case PATTERN_OR_NULL:
                        validator = Validator.getPatternOrNull(properties.getProperty(validatorName.concat(VALIDATOR_MSG)),
                                properties.getProperty(validatorName.concat(VALIDATOR_REGEX))
                        );
                        break;
                }
                if (validator != null) validatorMap.put(validatorName, validator);
            }
        } catch (IOException | ValidatorException e) {
            e.printStackTrace();
            throw new ValidatorException("exception.validator.properties", e.getCause());
        }
    }

    public static Validator create(String validatorName) throws ValidatorException {
        if (validatorName == null) return null;
        if (ValidatorFactory.validatorMap.size() == 0) ValidatorFactory.loadProperties();
        Validator validator = ValidatorFactory.validatorMap.get(validatorName);
        if (validator != null) return validator;
        throw new ValidatorException("exception.util.validator.validator-factory.create: " +validatorName);
    }

}
