package com.epam.java.rt.lab.web.servlet;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.web.action.ActionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.epam.java.rt.lab.util.PropertyManager.*;

@WebServlet(urlPatterns = SERVLET_PATH + SLASH + ASTERISK)
public class FrontServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            if (SLASH.equals(req.getPathInfo())) {
                ActionFactory.getInstance().
                        create(req.getMethod(), HOME_PATH).execute(req, resp);
            } else {
                ActionFactory.getInstance().
                        create(req.getMethod(), req.getPathInfo()).
                        execute(req, resp);
            }
        } catch (AppException e) {
            throw new ServletException(e.getMessage(), e.getCause());
        }
    }

}
