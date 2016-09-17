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

    public static String getRememberCookieName(HttpServletRequest req) {
        if (req.getCookies() == null) return null;
        String ip = req.getHeader("X-FORWARDED-FOR");
        if (ip == null) ip = req.getRemoteAddr();
        try {
            return HashManager.hashString(ip.concat(req.getHeader("User-Agent")).concat(ip));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getRememberCookieValue(HttpServletRequest req, String rememberCookieName) {
        Cookie rememberCookie = getCookie(req, rememberCookieName);
        return rememberCookie == null ? null : rememberCookie.getValue();
    }

    public static void removeRememberCookieValue(HttpServletRequest req, HttpServletResponse resp, String rememberCookieName) {
        setCookie(resp, rememberCookieName, null, 0, UrlManager.getContextUri(req, "/"));
    }

    public static void setRememberCookieValue(HttpServletRequest req, HttpServletResponse resp,
                                              String rememberCookieName, String rememberCookieValue) {
        setCookie(resp, rememberCookieName, rememberCookieValue, 2592000, UrlManager.getContextUri(req, "/"));
    }

}
