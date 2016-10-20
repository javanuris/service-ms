package com.epam.java.rt.lab.web.servlet;

import com.epam.java.rt.lab.entity.access.User;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.service.ServiceException;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.CookieManager;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.web.access.RoleFactory;
import com.epam.java.rt.lab.web.component.navigation.NavigationException;
import com.epam.java.rt.lab.web.component.navigation.NavigationFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static com.epam.java.rt.lab.entity.access.User.NULL_USER;
import static com.epam.java.rt.lab.util.PropertyManager.*;

@WebFilter(urlPatterns = SERVLET_PATH + SLASH + ASTERISK,
        dispatcherTypes = DispatcherType.FORWARD)
public class AccessFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException {
        try (UserService userService = new UserService()) {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            HttpServletResponse resp = (HttpServletResponse) servletResponse;
            User user = (User) req.getSession().getAttribute(USER_ATTR);
            if (user == null) {
                user = userService.getUserRemember(req, resp);
                if (user != NULL_USER) {
                    req.getSession().setAttribute(USER_ATTR, user);
                    req.getSession().setAttribute("navigationList",
                            NavigationFactory.getInstance().
                                    create(user.getRole().getName()));
                }
            }
            if (user == NULL_USER) {
                if (RoleFactory.getInstance().createAnonymous().
                        verifyPermission(req.getPathInfo())) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                    Map<String, String> parameterMap = UrlManager.
                            getRequestParameterMap(req.getQueryString());
                    parameterMap.put(REDIRECT, req.getPathInfo());
                    resp.sendRedirect(UrlManager.getUriWithContext(req,
                            LOGIN_PATH, parameterMap));
                }
            } else {
                if (user.getRole().verifyPermission(req.getPathInfo())) {
                    resp.setHeader(HEADER_CACHE_CONTROL, HEADER_NO_CACHE_VALUE);
                    resp.setHeader(HEADER_PRAGMA, HEADER_PRAGMA_VALUE);
                    resp.setDateHeader(HEADER_DATE_EXPIRES, 0);
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                    if (req.getPathInfo().equals(LOGIN_PATH)) {
                        resp.sendRedirect(UrlManager.getContextUri(req,
                                PROFILE_VIEW_PATH));
                    } else {
                        resp.sendError(403);
                    }
                }
            }
        } catch (NavigationException e) {
            e.printStackTrace();
//            throw new ServletException("exception.filter.access.do-filter.navigation", e.getCause());
        } catch (AppException e) {
//            throw new ServletException("exception.filter.access.do-filter.role-factory", e.getCause());
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {

    }

}