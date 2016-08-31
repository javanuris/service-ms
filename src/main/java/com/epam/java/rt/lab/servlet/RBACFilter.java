package com.epam.java.rt.lab.servlet;

import com.epam.java.rt.lab.entity.rbac.Role;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
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
        logger.debug("user = {}", user);
        if (user == null) user = UserService.getAnonymous();
        Role role = user.getRole();
        logger.debug("role = {}", role);
        logger.debug("pathInfo = {}", req.getPathInfo());
        logger.debug("role.getUriList() = {}", role.getUriList());
        if (role != null && role.getUriList().contains(req.getPathInfo())) {
            logger.debug("pass");
            filterChain.doFilter(servletRequest, servletResponse);
        }else {
            logger.debug("error");
            req.setAttribute("error", "error.message.forbiden");
            ((HttpServletResponse) servletResponse).sendError(403);
        }

    }

    @Override
    public void destroy() {

    }
}
