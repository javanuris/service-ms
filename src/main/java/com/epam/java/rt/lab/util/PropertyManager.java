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

    public static final String ESCAPED_QUESTION = "\\?";
    public static final String ESCAPED_BACKSLASH = "\\?";
    public static final String ESCAPED_POINT = "\\.";

    public static final String DEF_LOCALE_LANG_KEY = "def.locale.lang";
    public static final String DEF_LOCALE_COUNTRY_KEY = "def.locale.country";
    public static final String REMEMBER_DAYS_VALID_KEY = "remember.days.valid";

    public static final long UPLOAD_FILE_MAX_SIZE = 3145728;

    public static final String GET = "GET";
    public static final String POST = "POST";

    public static final String USER_ATTR = "user";
    public static final String JSP_ATTR = "jsp";

    public static final String ID = "id";
    public static final String PATH = "path";
    public static final String FILE = "file";
    public static final String REDIRECT = "redirect";

    public static final String USER_FIRST_NAME = "firstName";
    public static final String USER_MIDDLE_NAME = "middleName";
    public static final String USER_LAST_NAME = "lastName";
    public static final String USER_AVATAR_DOWNLOAD = "avatarDownload";

    public static final String HOME_PATH = "/home";
    public static final String LOGIN_PATH = "/profile/login";
    public static final String PROFILE_VIEW_PATH = "/profile/view";
    public static final String JSP_BASE_PATH = "/WEB-INF/jsp";
    public static final String JSP_EXTENSION = ".jsp";

    public static final String SERVLET_PATH = "/servlet";
    public static final String FILE_UPLOAD_PATH = "/file/upload";
    public static final String FILE_DOWNLOAD_PATH = "/file/download";
    public static final String FILE_AVATAR_PREFIX = "/avatar";

    public static final String AVATAR_UPLOAD_TYPE = "avatar";
    public static final String PHOTO_UPLOAD_TYPE = "photo";

    public static final String HEADER_IF_MODIFIED_SINCE = "If-Modified-Since";
    public static final String HEADER_CONTENT_LENGTH = "Content-Length";
    public static final String HEADER_CACHE_CONTROL = "Cache-Control";
    public static final String HEADER_NO_CACHE_VALUE = "no-cache, no-store, must-revalidate";
    public static final String HEADER_PRAGMA = "Pragma";
    public static final String HEADER_PRAGMA_VALUE = "no-cache";
    public static final String HEADER_DATE_EXPIRES = "Expires";

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
