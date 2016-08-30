package com.epam.java.rt.lab.action.profile;

import com.epam.java.rt.lab.action.Action;
import com.epam.java.rt.lab.action.ActionException;
import com.epam.java.rt.lab.action.WebAction;
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
public class ViewAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ViewAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try {
            logger.debug("/WEB-INF/jsp/profile/view.jsp");
            req.getSession().setAttribute("navbarCurrent", "/profile/view");
            req.getRequestDispatcher("/WEB-INF/jsp/profile/view.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new ActionException(e.getMessage());
        }
    }

}
