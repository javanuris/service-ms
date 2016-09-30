package com.epam.java.rt.lab.web.action.profile;

import com.epam.java.rt.lab.util.CookieManager;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.ActionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Service Management System
 */
public class GetLogoutAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(GetLogoutAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try {
            logger.debug("LOGOUT");
            req.getSession().removeAttribute("userId");
            req.getSession().removeAttribute("userName");
            req.getSession().removeAttribute("navbarItemArray");
            req.getSession().invalidate();
            req.removeAttribute("navbarCurrent");
            CookieManager.removeCookie(req, resp, CookieManager.getUserAgentCookieName(req));
            resp.setHeader("Cache-FormControl", "no-cache");
            logger.debug("REDIRECTING ({})", req.getContextPath());
            resp.sendRedirect(UrlManager.getContextUri(req, "/"));
        } catch (IOException e) {
            throw new ActionException(e.getMessage());
        }
    }

}
