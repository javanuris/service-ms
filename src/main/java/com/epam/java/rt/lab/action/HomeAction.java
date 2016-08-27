package com.epam.java.rt.lab.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * semasy
 */
@WebAction
public class HomeAction implements Action {
//    private static final Logger logger = LoggerFactory.getLogger(HomeAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try {
//            logger.debug("HomeAction");
            req.getRequestDispatcher(req.getContextPath().concat("/WEB-INF/jsp/home.jsp")).forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new ActionException(e.getMessage());
        }
    }

}
