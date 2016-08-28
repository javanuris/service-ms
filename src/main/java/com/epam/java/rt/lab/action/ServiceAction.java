package com.epam.java.rt.lab.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Service Management System
 */
@WebAction
public class ServiceAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ServiceAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try {
            logger.debug("/WEB-INF/jsp/service.jsp");
            req.getSession().setAttribute("navbar", NavbarComponent.getNavArray());
            req.getSession().setAttribute("navbarActive", 2);
            req.getRequestDispatcher("/WEB-INF/jsp/service.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new ActionException(e.getMessage());
        }
    }

}
