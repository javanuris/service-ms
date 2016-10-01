package com.epam.java.rt.lab.web.servlet;

import com.epam.java.rt.lab.util.CookieManager;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.web.action.ActionException;
import com.epam.java.rt.lab.web.action.ActionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Service Management System
 */
@WebServlet(urlPatterns = "/servlet/*")
public class FrontServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(FrontServlet.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("FrontServlet: {}", CookieManager.getCookie(req, "REQUEST_PARAMETER_REDIRECT") != null ?
                CookieManager.getCookie(req, "REQUEST_PARAMETER_REDIRECT").getValue() : "REDIRECT_COOKIE_EMPTY");
        logger.debug("PARAMETERS: {}", UrlManager.getRequestParameterString(req));
        try {
            if ("/".equals(req.getPathInfo())) {
                ActionFactory.getInstance().create(req.getMethod(), "/home").execute(req, resp);
            } else {
                ActionFactory.getInstance().create(req.getMethod(), req.getPathInfo()).execute(req, resp);
            }
        } catch (ActionException e) {
            logger.error("ActionException", e);
            throw new ServletException(e.getMessage());
        }
    }

}
