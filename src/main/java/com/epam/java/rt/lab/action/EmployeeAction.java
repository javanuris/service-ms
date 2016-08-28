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
public class EmployeeAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try {
            logger.debug("/WEB-INF/jsp/employee.jsp");
            req.getSession().setAttribute("navbar", NavbarComponent.getNavArray());
            req.getSession().setAttribute("navbarActive", 3);
            req.getRequestDispatcher("/WEB-INF/jsp/employee.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new ActionException(e.getMessage());
        }
    }

}
