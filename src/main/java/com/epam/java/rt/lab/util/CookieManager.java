package com.epam.java.rt.lab.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * service-ms
 */
public class CookieManager {

    public static Cookie getCookie(HttpServletRequest req, String name) {
        if (req.getCookies() != null && name != null) {
            for (Cookie cookie : req.getCookies())
                if (cookie.getName().equals(name)) return cookie;
        }
        return null;
    }

    public static void setCookie(HttpServletResponse resp, String name, String value, int maxAge, String contextPath) {
        if (name != null) {
            Cookie cookie = new Cookie(name, value);
            cookie.setMaxAge(maxAge);
            cookie.setPath(contextPath);
            resp.addCookie(cookie);
        }
    }

    public static String getDependantCookieName(HttpServletRequest req) {
        if (req.getCookies() == null) return null;
        String ip = req.getHeader("X-FORWARDED-FOR");
        if (ip == null) ip = req.getRemoteAddr();
        try {
            return HashGenerator.hashString(ip.concat(req.getHeader("User-Agent")).concat(ip));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDependantCookieValue(HttpServletRequest req, String dependantCookieName) {
        Cookie rememberCookie = getCookie(req, dependantCookieName);
        return rememberCookie == null ? null : rememberCookie.getValue();
    }

    public static void removeDependantCookieValue(HttpServletRequest req, HttpServletResponse resp, String dependantCookieName) {
        setCookie(resp, dependantCookieName, null, 0, UrlManager.getContextUri(req, "/"));
    }

    public static void setDependantCookieValue(HttpServletRequest req, HttpServletResponse resp,
                                               String dependantCookieName, String dependantCookieValue, int maxAge) {
        setCookie(resp, dependantCookieName, dependantCookieValue, maxAge, UrlManager.getContextUri(req, "/"));
    }

}
