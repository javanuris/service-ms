package com.epam.java.rt.lab.util;

import com.epam.java.rt.lab.entity.rbac.Remember;

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

    public static void setCookie(HttpServletResponse resp, String name, String value, int maxAge) {
        if (name != null) {
            Cookie cookie = new Cookie(name, value);
            cookie.setMaxAge(maxAge);
            cookie.setPath("/");
            resp.addCookie(cookie);
        }
    }

    public static void removeCookie(HttpServletRequest req, HttpServletResponse resp, String cookieName) {
        setCookie(resp, cookieName, null, 0);
    }

    public static String getUserAgentCookieName(HttpServletRequest req) {
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

    public static String getCookieValue(HttpServletRequest req, String cookieName) {
        Cookie cookie = getCookie(req, cookieName);
        return cookie == null ? null : cookie.getValue();
    }

}
