package com.epam.java.rt.lab.web.action.profile;

import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.ServiceException;
import com.epam.java.rt.lab.service.UserService;
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
        try (UserService userService = new UserService()) {
            logger.debug("LOGOUT");
            Long userId = (Long) req.getSession().getAttribute("userId");
            req.getSession().removeAttribute("userId");
            req.getSession().removeAttribute("userName");
            req.getSession().removeAttribute("navbarItemArray");
            req.getSession().invalidate();
            req.removeAttribute("navbarCurrent");
            CookieManager.removeCookie(req, resp, CookieManager.getUserAgentCookieName(req));
            User user = userService.getUser(userId);
            userService.removeUserRemember(user);
            resp.setHeader("Cache-FormControl", "no-cache");
            logger.debug("REDIRECTING ({})", req.getContextPath());
            resp.sendRedirect(UrlManager.getContextUri(req, "/"));
        } catch (IOException e) {
            throw new ActionException("exception.action.logout.redirect", e.getCause());
        } catch (ServiceException e) {
            throw new ActionException("exception.action.logout.service", e.getCause());
        }
    }

}
