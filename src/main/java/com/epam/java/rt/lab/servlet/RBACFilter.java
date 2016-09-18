package com.epam.java.rt.lab.servlet;

import com.epam.java.rt.lab.component.NavigationComponent;
import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

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
        try (UserService userService = new UserService();) {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            Long userId = (Long) req.getSession().getAttribute("userId");
            User user = null;
            if (userId == null) {
                logger.debug("TRYING TO GET USER FROM COOKIE");
                String rememberCookieName = CookieManager.getDependantCookieName(req);
                String rememberCookieValue = CookieManager.getDependantCookieValue(req, rememberCookieName);
                if (rememberCookieValue != null) {
                    Map<String, Object> rememberValueMap = userService.getRemember(rememberCookieName);
                    if (rememberValueMap != null && rememberCookieValue.equals(rememberValueMap.get("value")) &&
                            TimestampManager.secondsBetweenTimestamps(TimestampManager.getCurrentTimestamp(),
                                    (Timestamp) rememberValueMap.get("valid")) > 0) {
                        userService.setRemember(rememberValueMap);
                        userId = (Long) rememberValueMap.get("userId");
                        user = userService.getUser(userId);
                        req.getSession().setAttribute("userId", userId);
                        req.getSession().setAttribute("userName", user.getName());
                        req.getSession().setAttribute("navbarItemArray", NavigationComponent.getNavbarItemArray(user.getRole()));
                        try {
                            rememberCookieValue = HashManager.hashString(UUID.randomUUID().toString());
                            rememberValueMap.put("value", rememberCookieValue);
                            userService.setRemember(rememberValueMap);
                            CookieManager.setDependantCookieValue(req, (HttpServletResponse) servletResponse,
                                    rememberCookieName, rememberCookieValue,
                                    Integer.valueOf(GlobalManager.getProperty("remember.days.valid")) * 86400);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CookieManager.removeDependantCookieValue
                                (req, (HttpServletResponse) servletResponse, rememberCookieName);
                        if (rememberValueMap != null) userService.removeRemember((Long) rememberValueMap.get("userId"));
                    }
                }
            }
            if (userId == null) {
                try {
                    logger.debug("ANONYMOUS URI = {}", userService.getAnonymous().getRole().getUriList());
                    if (userService.getAnonymous().getRole().getUriList().contains(req.getPathInfo())) {
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
                } catch (DaoException e) {
                    e.printStackTrace();
                    throw new ServletException("exception.servlet.rbac.get-anonymous", e.getCause());
                }
            } else {
                if (user == null) user = userService.getUser(userId);
                if (user.getRole().getUriList().contains(req.getPathInfo())) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                    if (req.getPathInfo().equals("/profile/login")) {
                        ((HttpServletResponse) servletResponse).sendRedirect(UrlManager.getContextUri(req, "/profile/view"));
                    } else {
                        ((HttpServletResponse) servletResponse).sendError(403);
                    }
                }
            }
        } catch (ConnectionException | DaoException e) {
            e.printStackTrace();
            throw new ServletException("exception.filter.rbac.user-service", e.getCause());
        }
    }

    @Override
    public void destroy() {

    }

}
