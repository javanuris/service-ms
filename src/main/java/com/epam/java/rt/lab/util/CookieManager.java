package com.epam.java.rt.lab.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * service-ms
 */
public class CookieManager {

    public static Cookie getCookie(HttpServletRequest req, String name) {
        for (Cookie cookie : req.getCookies())
            if (cookie.getName().equals(name)) return cookie;
        return null;
    }

    public static void setCookie(HttpServletResponse resp, String name, String value, int maxAge, String contextPath) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath(contextPath);
        resp.addCookie(cookie);
    }

}
