package com.epam.java.rt.lab.servlet;

import com.epam.java.rt.lab.util.UrlParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Service Management System
 */
@WebFilter(urlPatterns = "/*", dispatcherTypes = DispatcherType.REQUEST)
public class UrlFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(UrlFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        servletRequest.setAttribute("debugMode", true);
        UrlParameter.setAttributeFromUrlParameter((HttpServletRequest) servletRequest);
        String requestURI = ((HttpServletRequest) servletRequest).getRequestURI()
                .substring(((HttpServletRequest) servletRequest).getContextPath().length()).toLowerCase();
        logger.debug(requestURI);
        if (requestURI.startsWith("/static/") || requestURI.startsWith("/webjars/") || requestURI.equals("/favicon.ico")) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            servletRequest.getRequestDispatcher("/servlet".concat(requestURI))
                    .forward(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
