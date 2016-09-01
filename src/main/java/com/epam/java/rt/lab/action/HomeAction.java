package com.epam.java.rt.lab.action;

import com.epam.java.rt.lab.component.NavbarComponent;
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
public class HomeAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(HomeAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try {
            logger.debug("/WEB-INF/jsp/home.jsp");
            req.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new ActionException(e.getMessage());
        }
    }

}
