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
    public static final String HYPHEN = "-";
    public static final String COLON = ":";
    public static final String QUESTION = "?";
    public static final String AMPERSAND = "&";
    public static final String ASTERISK = "*";
    public static final String LEFT_PARENTHESIS = "(";
    public static final String RIGHT_PARENTHESIS = ")";

    public static final String DEF_LOCALE_LANG_KEY = "def.locale.lang";
    public static final String DEF_LOCALE_COUNTRY_KEY = "def.locale.country";

    public static final long UPLOAD_FILE_MAX_SIZE = 3145728;

    public static final String GET = "GET";
    public static final String POST = "POST";

    public static final String USER_ATTR = "user";
    public static final String JSP_ATTR = "jsp";

    public static final String ID = "id";
    public static final String USER_FIRST_NAME = "firstName";
    public static final String USER_MIDDLE_NAME = "middleName";
    public static final String USER_LAST_NAME = "lastName";
    public static final String USER_AVATAR_DOWNLOAD = "avatarDownload";

    public static final String JSP_BASE_PATH = "/WEB-INF/jsp";
    public static final String JSP_EXTENSION = ".jsp";

    public static final String FILE_UPLOAD_PATH = "/file/upload";
    public static final String FILE_DOWNLOAD_PATH = "/file/download";
    public static final String FILE_AVATAR_PREFIX = "/avatar";

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
