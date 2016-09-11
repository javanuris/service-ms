package com.epam.java.rt.lab.servlet;

import com.epam.java.rt.lab.component.NavbarComponent;
import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.UrlParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Service Management System
 */
@WebFilter(urlPatterns = "/servlet/*", dispatcherTypes = DispatcherType.FORWARD)
public class RBACFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(RBACFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        logger.debug("RBAC");
        try {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            Long userId = (Long) req.getSession().getAttribute("userId");
            UserService userService = new UserService();
            if (userId == null) {
                logger.debug("TRYING TO GET USER FROM COOKIE");
                Cookie rememberUserCookie = ResponseCookie.getCookie(req, UserService.getRememberCookieName());
                if (rememberUserCookie != null) userId = UserService.getRememberUserId(rememberUserCookie.getValue());
                if (userId != null) {
                    req.getSession().setAttribute("userId", userId);
                    User user = userService.getUser(userId);
                    req.getSession().setAttribute("userName", user.getName());
                    req.getSession().setAttribute("navbarItemArray", NavbarComponent.getNavbarItemArray(user.getRole()));
                    logger.debug("USER FROM COOKIE ({})", user);
                }
            }
            if (userId == null) {
                try {
                    logger.debug("getAnonymous = {}", userService.getAnonymous());
                    logger.debug("ANONYMOUS URI = {}", userService.getAnonymous().getRole().getUriList());
                    if (userService.getAnonymous().getRole().getUriList().contains(req.getPathInfo())) {
                        logger.debug("CONTAINS {}", req.getPathInfo());
                        filterChain.doFilter(servletRequest, servletResponse);
                        logger.debug("REDIRECT (SHOULD BE NULL) {}", req.getSession().getAttribute("redirect"));
                    } else {
                        logger.debug("NEED TO REDIRECT");
                        ((HttpServletResponse) servletResponse).sendRedirect("/profile/login".concat
                                (UrlParameter.combineUrlParameter(new UrlParameter.UrlParameterBuilder
                                        ("redirect", req.getContextPath().concat(req.getPathInfo())))));
                    }
                } catch (DaoException e) {
                    e.printStackTrace();
                    throw new ServletException("exception.servlet.rbac.get-anonymous", e.getCause());
                }
            } else {
                User user = userService.getUser(userId);
                if (user.getRole().getUriList().contains(req.getPathInfo())) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                    if (req.getPathInfo().equals("/profile/login")) {
                        ((HttpServletResponse) servletResponse).sendRedirect("/profile/view");
                    } else {
                        ((HttpServletResponse) servletResponse).sendError(403);
                    }
                }
            }
        } catch (DaoException| ConnectionException e) {
            e.printStackTrace();
            throw new ServletException("exception.servlet.rbac.get-user", e.getCause());
        }
    }

    @Override
    public void destroy() {

    }

}
