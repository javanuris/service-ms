package com.epam.java.rt.lab.exception;

import com.epam.java.rt.lab.util.PropertyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.epam.java.rt.lab.util.PropertyManager.DEF_LOCALE_COUNTRY_KEY;
import static com.epam.java.rt.lab.util.PropertyManager.DEF_LOCALE_LANG_KEY;
import static com.epam.java.rt.lab.util.StringArray.combine;

public class AppException extends Exception {

    public enum AppExceptionCode implements ExceptionCode {
        NULL_EXCEPTION(0),
        NULL_NOT_ALLOWED(1);

        private final int number;

        private AppExceptionCode(int number) {
            this.number = number;
        }

        @Override
        public int getNumber() {
            return this.number;
        }

    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AppException.class);
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

    protected AppException(ExceptionCode exceptionCode, String message,
                           Throwable cause, boolean enableSuppression,
                           boolean writableStackTrace, String[] detailArray) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.exceptionCode = exceptionCode;
        this.detailArray = detailArray;
        log();
    }

    private void log() {
        MDC.put("exceptionSource",
                this.exceptionCode.getClass().getSimpleName());
        MDC.put("exceptionCode",
                String.valueOf(this.exceptionCode.getNumber()));
        LOGGER.error(exceptionMessage());
    }

    private String exceptionMessage() {
        if (exceptionCode == null) {
            exceptionCode = AppExceptionCode.NULL_EXCEPTION;
        }
        String key = exceptionCode.getClass().getSimpleName()
                + PropertyManager.POINT + exceptionCode;
        return exceptionBundle.getString(key) + getDetailsString()
                + ((super.getMessage() == null) ? ""
                : "\n" + super.getMessage());
    }

    private String getDetailsString() {
        if (this.detailArray == null) return "";
        return PropertyManager.SPACE + PropertyManager.LEFT_PARENTHESIS +
                combine(new ArrayList<>(Arrays.asList(this.detailArray)),
                        PropertyManager.COMMA_WITH_SPACE)
                + PropertyManager.RIGHT_PARENTHESIS;
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
