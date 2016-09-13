package com.epam.java.rt.lab.util;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;

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

    public static String getContextUri(HttpServletRequest req, String path) {
        return path == null ? null : req.getContextPath().concat(path);
    }

    public static void setAttributeFromUrlParameter(HttpServletRequest req) {
        if (req.getMethod().equals("GET")) {
            Enumeration<String> parameterNames = req.getParameterNames();
            String parameterName, parameterValue;
            if (parameterNames != null) {
                while (parameterNames.hasMoreElements()) {
                    parameterName = parameterNames.nextElement();
                    parameterValue = req.getParameter(parameterName);
                    req.setAttribute("url-".concat(parameterName), parameterValue);
                }
            }
        }
    }

    public static String combineUrlParameterFromAttribute(HttpServletRequest req, String... parameterNameArray) {
        StringBuilder result = new StringBuilder();
        String parameterValue;
        for (String parameterName : parameterNameArray) {
            parameterValue = (String) req.getAttribute("url-".concat(parameterName));
            if (parameterValue != null) {
                if (result.length() == 0) {
                    result.append("?");
                } else {
                    result.append("&");
                }
                result.append(parameterName).append("=").append(parameterValue);
            }
        }
        return result.toString();
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
