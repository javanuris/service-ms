package com.epam.java.rt.lab.exception;

import com.epam.java.rt.lab.util.PropertyManager;
import com.epam.java.rt.lab.util.StringCombiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.*;

import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_EXCEPTION;
import static com.epam.java.rt.lab.util.PropertyManager.DEF_LOCALE_COUNTRY_KEY;
import static com.epam.java.rt.lab.util.PropertyManager.DEF_LOCALE_LANG_KEY;

/**
 * The class {@code AppException} extends {@code Exception} class
 * and contain additional fields and enum to output detailed exception logs
 */
public class AppException extends Exception {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(AppException.class);
    private static final String EXCEPTION_BUNDLE = "i18n/exception";

    private static ResourceBundle exceptionBundle;

    private ExceptionCode exceptionCode;
    private String[] detailArray;

    public static void initExceptionBundle() {
        String language = PropertyManager.getProperty(DEF_LOCALE_LANG_KEY);
        String country = PropertyManager.getProperty(DEF_LOCALE_COUNTRY_KEY);
        if ((language != null) && (country != null)) {
            Locale locale = new Locale(language, country);
            exceptionBundle = ResourceBundle.getBundle(EXCEPTION_BUNDLE, locale);
        } else if (language != null) {
            Locale locale = new Locale(language);
            exceptionBundle = ResourceBundle.getBundle(EXCEPTION_BUNDLE, locale);
        } else {
            exceptionBundle = ResourceBundle.getBundle(EXCEPTION_BUNDLE);
        }
    }

    public AppException(ExceptionCode exceptionCode) {
        super();
        this.exceptionCode = exceptionCode;
        log();
    }

    public AppException(ExceptionCode exceptionCode, String message) {
        super(message);
        this.exceptionCode = exceptionCode;
        log();
    }

    public AppException(ExceptionCode exceptionCode, String message,
                        Throwable cause) {
        super(message, cause);
        this.exceptionCode = exceptionCode;
        log();
    }

    public AppException(ExceptionCode exceptionCode, Throwable cause) {
        super(cause);
        this.exceptionCode = exceptionCode;
        log();
    }

    protected AppException(ExceptionCode exceptionCode, String message,
                           Throwable cause, boolean enableSuppression,
                           boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.exceptionCode = exceptionCode;
        log();
    }

    public AppException(ExceptionCode exceptionCode, String[] detailArray) {
        super();
        this.exceptionCode = exceptionCode;
        this.detailArray = detailArray;
        log();
    }

    public AppException(ExceptionCode exceptionCode, String message,
                        String[] detailArray) {
        super(message);
        this.exceptionCode = exceptionCode;
        this.detailArray = detailArray;
        log();
    }

    public AppException(ExceptionCode exceptionCode, String message,
                        Throwable cause, String[] detailArray) {
        super(message, cause);
        this.exceptionCode = exceptionCode;
        this.detailArray = detailArray;
        log();
    }

    public AppException(ExceptionCode exceptionCode, Throwable cause,
                        String[] detailArray) {
        super(cause);
        this.exceptionCode = exceptionCode;
        this.detailArray = detailArray;
        log();
    }

    public AppException(ExceptionCode exceptionCode, String message,
                        Throwable cause, boolean enableSuppression,
                        boolean writableStackTrace, String[] detailArray) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.exceptionCode = exceptionCode;
        this.detailArray = detailArray;
        log();
    }

    private void log() {
        MDC.put("exceptionCause",
                this.getStackTrace()[0].toString());
        MDC.put("exceptionSource",
                this.exceptionCode.getClass().getSimpleName());
        MDC.put("exceptionCode",
                String.valueOf(this.exceptionCode.getNumber()));
        LOGGER.error(exceptionMessage());
    }

    private String exceptionMessage() {
        if (exceptionCode == null) {
            exceptionCode = NULL_EXCEPTION;
        }
        String key = exceptionCode.getClass().getSimpleName()
                + PropertyManager.POINT + exceptionCode;
        String message = key;
        try {
            message = exceptionBundle.getString(key);
        } catch (MissingResourceException e) {
            //
        }
        return message + getDetailsString()
                + ((super.getMessage() == null) ? ""
                : PropertyManager.SPACE + super.getMessage());
    }

    private String getDetailsString() {
        if (this.detailArray == null) return "";
        try {
            return PropertyManager.SPACE + PropertyManager.LEFT_PARENTHESIS +
                    StringCombiner.combine(new ArrayList<>(Arrays.
                                    asList(this.detailArray)),
                            PropertyManager.COMMA_WITH_SPACE)
                    + PropertyManager.RIGHT_PARENTHESIS;
        } catch (AppException e) {
            return "";
        }
    }

    public ExceptionCode getExceptionCode() {
        return this.exceptionCode;
    }

    @Override
    public String getMessage() {
        return (super.getMessage() != null)
                ? super.getMessage()
                : exceptionMessage();
    }

    @Override
    public String getLocalizedMessage() {
        return exceptionMessage();
    }

}