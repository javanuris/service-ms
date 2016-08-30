package com.epam.java.rt.lab.action.profile;

import com.epam.java.rt.lab.action.Action;
import com.epam.java.rt.lab.action.ActionException;
import com.epam.java.rt.lab.action.WebAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Service Management System
 */
@WebAction
public class LogoutAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(LogoutAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try {
            req.getSession().removeAttribute("user");
            req.getSession().removeAttribute("navbarItemArray");
            req.removeAttribute("navbarCurrent");
            resp.sendRedirect("/");
        } catch (IOException e) {
            throw new ActionException(e.getMessage());
        }
    }

}
