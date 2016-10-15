package com.epam.java.rt.lab.util;

import com.epam.java.rt.lab.exception.AppException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.epam.java.rt.lab.util.UtilExceptionCode.PROPERTY_READ_ERROR;

public final class PropertyManager {

    public static final String UNDERSCORE = "_";
    public static final String SPACE = " ";
    public static final String SLASH = "/";
    public static final String COMMA = ",";
    public static final String COMMA_WITH_SPACE = ", ";
    public static final String POINT = ".";
    public static final String EQUAL = "=";
    public static final String LEFT_PARENTHESIS = "(";
    public static final String RIGHT_PARENTHESIS = ")";

    public static final String DEF_LOCALE_LANG_KEY = "def.locale.lang";
    public static final String DEF_LOCALE_COUNTRY_KEY = "def.locale.country";

    private static final String GLOBAL_PROPERTY_FILE = "global.properties";

    private static Properties properties = new Properties();

    private PropertyManager() {
    }

    public static void initGlobalProperties() throws AppException {
        ClassLoader classLoader = PropertyManager.class.getClassLoader();
        InputStream inputStream = classLoader.
                getResourceAsStream(GLOBAL_PROPERTY_FILE);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            String[] detailArray = {GLOBAL_PROPERTY_FILE, e.getMessage()};
            throw new AppException(PROPERTY_READ_ERROR, e.getCause(),
                    detailArray);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

}
