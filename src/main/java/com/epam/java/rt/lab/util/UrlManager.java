package com.epam.java.rt.lab.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * service-ms
 */
public class UrlManager {

    private UrlManager() {
    }

    public static String getUriForButton(HttpServletRequest req, String path, String parameterName) {
        return UrlManager.getContextUri(req, path)
                .concat(UrlManager.combineUrlParameter(new UrlManager.UrlParameterBuilder(parameterName, "true")));
    }

    public static String getContextRef(HttpServletRequest req, String path,
                                       String parameterNames, Object... parameterValueArray) {
        Map<String, String> parameterMap = new HashMap<>();
        String[] parameterNameArray = parameterNames.replaceAll(" ", "").split(",");
        for (int i = 0; i < parameterValueArray.length; i++)
            if (parameterValueArray[i] != null)
                parameterMap.put(parameterNameArray[i], String.valueOf(parameterValueArray[i]));
        String parameterString = getRequestParameterString(parameterMap);
        System.out.println(parameterString);
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














    public static String combineUrlParameter(UrlParameterBuilder... urlParameterBuilderArray) {
        StringBuilder result = new StringBuilder();
        for (UrlParameterBuilder urlParameterBuilder : urlParameterBuilderArray) {
            if (result.length() == 0) {
                result.append("?");
            } else {
                result.append("&");
            }
            result.append(urlParameterBuilder.getName()).append("=").append(urlParameterBuilder.getValue());
        }
        return result.toString();
    }

    public static String getUrlParameter(HttpServletRequest req, String parameterName, String defaultValue) {
        String parameterValue = req.getParameter(parameterName);
        return parameterValue == null ? UrlManager.getContextUri(req, defaultValue) : parameterValue;
    }

    public static String getUrlParameterFromAttribute(HttpServletRequest req, String parameterName, String defaultValue) {
        String parameterValue = (String) req.getAttribute("url-".concat(parameterName));
        return parameterValue == null ? UrlManager.getContextUri(req, defaultValue) : parameterValue;
    }

    public static class UrlParameterBuilder {
        private String name;
        private String value;

        public UrlParameterBuilder(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }
}
