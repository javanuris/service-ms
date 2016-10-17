package com.epam.java.rt.lab.util;

import com.epam.java.rt.lab.exception.AppException;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;
import static com.epam.java.rt.lab.util.PropertyManager.AMPERSAND;
import static com.epam.java.rt.lab.util.PropertyManager.QUESTION;

/**
 * category-ms
 */
public class UrlManager {

    private UrlManager() {
    }

    public static String getContextRef(HttpServletRequest req, String path,
                                       String parameterNames, Object... parameterValueArray) {
        Map<String, String> parameterMap = new HashMap<>();
        String[] parameterNameArray = parameterNames.replaceAll(" ", "").split(",");
        for (int i = 0; i < parameterValueArray.length; i++)
            if (parameterValueArray[i] != null)
                parameterMap.put(parameterNameArray[i], String.valueOf(parameterValueArray[i]));
        String parameterString = getRequestParameterString(parameterMap);
//        System.out.println(parameterString);
        if (parameterString.length() == 0) return null;
        return getContextUri(req, path, parameterString);
    }

    public static String getContextUri(HttpServletRequest req, String path, String... parameterArray) {
        String parameterString = getRequestParameterString(parameterArray);
        if (parameterString.length() > 0) parameterString = "?".concat(parameterString);
        return path == null ? req.getContextPath().concat("/").concat(parameterString)
                : req.getContextPath().concat(path).concat(parameterString);
    }

    public static String getContextUri(HttpServletRequest req, String path, Map<String, String> parameterMap) {
        return getContextUri(req, path, getRequestParameterString(parameterMap));
    }

    public static String getContextPathInfo(HttpServletRequest req, String... parameterArray) {
        return getContextUri(req, req.getPathInfo(), getRequestParameterString(parameterArray));
    }

    public static String getContextPathInfo(HttpServletRequest req) {
        return getContextUri(req, req.getPathInfo());
    }

    public static Map<String, String> getRequestParameterMap(String parameterString) {
        Map<String, String> parameterMap = new HashMap<>();
        if (parameterString == null) return parameterMap;
        String[] parameterArray = parameterString.split("&");
        for (String parameter : parameterArray) {
            String[] parameterNameValue = parameter.split("=");
            if (parameterNameValue.length == 2
                    && parameterNameValue[0].length() > 0 && parameterNameValue[1].length() > 0)
                parameterMap.put(parameterNameValue[0], parameterNameValue[1]);

        }
        return parameterMap;
    }

    public static String getRequestParameterString(Map<String, String> parameterMap) {
        boolean first = true;
        StringBuilder parameters = new StringBuilder();
        for (Map.Entry<String, String> parameter : parameterMap.entrySet()) {
            if (first) {
                first = false;
            } else {
                parameters.append("&");
            }
            parameters.append(parameter.getKey()).append("=").append(parameter.getValue());
        }
        return parameters.toString();
    }

    public static String getRequestParameterString(HttpServletRequest req) {
        boolean first = true;
        StringBuilder parameters = new StringBuilder();
        Enumeration names = req.getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            if (first) {
                first = false;
            } else {
                parameters.append("&");
            }
            parameters.append(name).append("=").append(req.getParameter(name));
        }
        return parameters.toString();
    }

    public static String getRequestParameterString(String[] parameterArray) {
        boolean first = true;
        StringBuilder parameterString = new StringBuilder();
        for (String parameter : parameterArray) {
            if (parameter != null && parameter.length() > 0) {
                if (first) {
                    first = false;
                } else {
                    parameterString.append("&");
                }
                parameterString.append(parameter);
            }
        }
        return parameterString.toString();
    }

    public static String getUriWithContext(HttpServletRequest req,
                                           String uri) throws AppException {
        if (req == null || uri == null) throw new AppException(NULL_NOT_ALLOWED);
        return req.getContextPath() + uri;
    }

    public static String getUriWithContext(HttpServletRequest req,
                                           String uri,
                                           Map<String, String> parameterMap)
            throws AppException {
        if (parameterMap == null) throw new AppException(NULL_NOT_ALLOWED);
        return getUriWithContext(req, uri) + QUESTION
                + StringCombiner.combine(parameterMap, AMPERSAND);

    }

}
