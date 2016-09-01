package com.epam.java.rt.lab.servlet;

import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            logger.debug("TRYING TO GET USER FROM COOKIE");
            Cookie rememberUserCookie = ResponseCookie.getCookie(req, UserService.getRememberCookieName());
            if (rememberUserCookie != null) user = UserService.getRememberUser(rememberUserCookie.getValue());
            logger.debug("USER FROM COOKIE ({})", user);
        }
        logger.debug("user = {}", user);
        if (user == null) {
            if (UserService.getAnonymous().getRole().getUriList().contains(req.getPathInfo())) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                req.getSession().setAttribute("redirect", req.getContextPath().concat(req.getPathInfo()));
                req.getRequestDispatcher("/servlet/profile/login").forward(servletRequest, servletResponse);
            }
        } else {
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
    }

    @Override
    public void destroy() {

    }

}
