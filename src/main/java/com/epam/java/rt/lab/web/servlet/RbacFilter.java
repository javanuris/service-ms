package com.epam.java.rt.lab.web.servlet;

import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.LoginService;
import com.epam.java.rt.lab.service.ServiceException;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.CookieManager;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.web.Rbac.RoleException;
import com.epam.java.rt.lab.web.Rbac.RoleFactory;
import com.epam.java.rt.lab.web.component.form.FormException;
import com.epam.java.rt.lab.web.component.navigation.NavigationException;
import com.epam.java.rt.lab.web.component.navigation.NavigationFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Service Management System
 */
@WebFilter(urlPatterns = "/servlet/*", dispatcherTypes = DispatcherType.FORWARD)
public class RbacFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(RbacFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        logger.debug("RbacFilter");
        try (UserService userService = new UserService()) {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            User user = (User) req.getSession().getAttribute("user");
            if (user == null) {
                logger.debug("TRYING TO GET USER FROM COOKIE");
                String rememberCookieName = CookieManager.getUserAgentCookieName(req);
                String rememberCookieValue = CookieManager.getCookieValue(req, rememberCookieName);
                if (rememberCookieValue != null) {
                    user = userService.getUserRemember(rememberCookieName, rememberCookieValue);
                    if (user == null || user.getLogin().getAttemptLeft() == 0 || user.getLogin().getStatus() < 0) {
                        CookieManager.removeCookie(req, (HttpServletResponse) servletResponse,
                                rememberCookieName, UrlManager.getContextUri(req, ""));
                    } else {
                        logger.debug("USER DEFINED: {}", user.getName());
                        req.getSession().setAttribute("user", user);
                        req.getSession().setAttribute("navigationList",
                                NavigationFactory.getInstance().create(user.getRole().getName()));
                        userService.addRemember(req, (HttpServletResponse) servletResponse, user);
                    }
                }
            }
            if (user == null) {
                logger.debug("ANONYMOUS URI = {}", userService.getAnonymous().getRole().getUriList());
                if (RoleFactory.getInstance().createAnonymous().verifyPermission(req.getPathInfo())) {
                    logger.debug("CONTAINS {}", req.getPathInfo());
                    filterChain.doFilter(servletRequest, servletResponse);
                    logger.debug("REDIRECT (SHOULD BE NULL) {}", req.getSession().getAttribute("redirect"));
                } else {
                    logger.debug("NEED TO REDIRECT");
                    HttpServletResponse resp = (HttpServletResponse) servletResponse;
                    Map<String, String> parameterMap = UrlManager.getRequestParameterMap(req.getQueryString());
                    parameterMap.put("redirect", req.getPathInfo());
                    resp.sendRedirect(UrlManager.getContextUri(req, "/profile/login", parameterMap));
                }
            } else {
                if (RoleFactory.getInstance().create(user.getRole().getName()).verifyPermission(req.getPathInfo())) { // TODO: refactor to role
                    disableCacheForAccessSensitive((HttpServletResponse) servletResponse);
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                    if (req.getPathInfo().equals("/profile/login")) {
                        ((HttpServletResponse) servletResponse).sendRedirect(UrlManager.getContextUri(req, "/profile/view"));
                    } else {
                        ((HttpServletResponse) servletResponse).sendError(403);
                    }
                }
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ServletException("exception.filter.rbac.do-filter.user-service", e.getCause());
        } catch (NavigationException e) {
            e.printStackTrace();
            throw new ServletException("exception.filter.rbac.do-filter.navigation", e.getCause());
        } catch (RoleException e) {
            e.printStackTrace();
            throw new ServletException("exception.filter.rbac.do-filter.role-factory", e.getCause());
        } catch (FormException e) {
            e.printStackTrace();
        }
    }

    private void disableCacheForAccessSensitive (HttpServletResponse resp) {
        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);
    }

    @Override
    public void destroy() {

    }

}
