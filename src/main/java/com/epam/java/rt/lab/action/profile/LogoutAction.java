package com.epam.java.rt.lab.action.profile;

import com.epam.java.rt.lab.action.Action;
import com.epam.java.rt.lab.action.ActionException;
import com.epam.java.rt.lab.action.WebAction;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.CookieManager;
import com.epam.java.rt.lab.util.UrlManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Service Management System
 */
@WebAction
public class LogoutAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(LogoutAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try {
            logger.debug("LOGOUT");
            req.getSession().removeAttribute("userId");
            req.getSession().removeAttribute("userName");
            req.getSession().removeAttribute("navbarItemArray");
            req.getSession().invalidate();
            req.removeAttribute("navbarCurrent");
            CookieManager.setCookie(resp, UserService.getRememberCookieName(), null, 0, req.getContextPath().concat("/"));
            resp.setHeader("Cache-Control", "no-cache");
            logger.debug("REDIRECTING ({})", req.getContextPath());
            resp.sendRedirect(UrlManager.getContextUri(req, "/"));
        } catch (IOException e) {
            throw new ActionException(e.getMessage());
        }
    }

}
