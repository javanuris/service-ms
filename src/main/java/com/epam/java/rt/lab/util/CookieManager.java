package com.epam.java.rt.lab.util;

import com.epam.java.rt.lab.exception.AppException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;

public class CookieManager {

    private static final String X_FORWARDED_FOR = "X-FORWARDED-FOR";
    private static final String USER_AGENT = "User-Agent";

    private static Cookie getCookie(HttpServletRequest req, String name)
            throws AppException {
        if (req == null || name == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        if (req.getCookies() == null) throw new AppException(NULL_NOT_ALLOWED);
        for (Cookie cookie : req.getCookies())
            if (cookie.getName().equals(name)) return cookie;
        return null;
    }

    public static void setCookie(HttpServletResponse resp, String name,
                                 String value, int maxAge, String contextPath)
            throws AppException {
        if (resp == null || name == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath(contextPath);
        resp.addCookie(cookie);
    }

    public static void removeCookie(HttpServletResponse resp,
                                    String cookieName, String contextPath)
            throws AppException {
        if (resp == null || cookieName == null || contextPath == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        setCookie(resp, cookieName, null, 0, contextPath);
    }

    public static String getUserAgentCookieName(HttpServletRequest req)
            throws AppException {
        if (req.getCookies() == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        String ip = req.getHeader(X_FORWARDED_FOR);
        if (ip == null) ip = req.getRemoteAddr();
        return HashGenerator.hashString(ip + req.getHeader(USER_AGENT) + ip);
    }

    public static String getCookieValue(HttpServletRequest req,
                                        String cookieName)
            throws AppException {
        if (req == null || cookieName == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        Cookie cookie = getCookie(req, cookieName);
        return cookie == null ? "" : cookie.getValue();
    }

}