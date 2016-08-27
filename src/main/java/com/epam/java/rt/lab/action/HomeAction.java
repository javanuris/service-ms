package com.epam.java.rt.lab.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service Management System
 */
@WebAction
public class HomeAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(HomeAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try {
            List<NavComponent> navArray = new ArrayList<>();
            navArray.add(new NavComponent("active", "nav.users", "#"));
            navArray.add(new NavComponent("", "nav.persons", "/persons"));
            navArray.add(new NavComponent("", "nav.services", "/services"));
            logger.debug("/WEB-INF/jsp/home.jsp");
            req.getSession().setAttribute("navArray", navArray);
            req.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new ActionException(e.getMessage());
        }
    }

}
