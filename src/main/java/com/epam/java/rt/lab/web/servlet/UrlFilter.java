package com.epam.java.rt.lab.web.servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.epam.java.rt.lab.util.PropertyManager.*;

@WebFilter(urlPatterns = SLASH + ASTERISK,
        dispatcherTypes = DispatcherType.REQUEST)
public class UrlFilter implements Filter {

    private static final String DEBUG_MODE_ATTR = "debugMode";
    private static final String UTF_8 = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {
        servletRequest.setAttribute(DEBUG_MODE_ATTR, true);
        servletRequest.setCharacterEncoding(UTF_8);
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String requestURI = req.getRequestURI().
                substring((req).getContextPath().length()).toLowerCase();
        if (requestURI.startsWith(STATIC_PATH_WITH_SLASH)
                || requestURI.startsWith(WEBJARS_PATH_WITH_SLASH)
                || requestURI.startsWith(FILE_PATH_WITH_SLASH)
                || requestURI.equals(FAVICON_PATH)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            servletRequest.getRequestDispatcher(SERVLET_PATH + requestURI).
                    forward(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
