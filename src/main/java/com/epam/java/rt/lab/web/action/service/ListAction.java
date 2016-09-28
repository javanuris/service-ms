package com.epam.java.rt.lab.web.action.service;

import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.ActionException;
import com.epam.java.rt.lab.web.action.WebAction;
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
public class ListAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ListAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try {
            logger.debug("/WEB-INF/jsp/service/list.jsp");
            req.getRequestDispatcher("/WEB-INF/jsp/service/list.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new ActionException(e.getMessage());
        }
    }

}
