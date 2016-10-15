package com.epam.java.rt.lab.web.action;

import com.epam.java.rt.lab.exception.AppException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.epam.java.rt.lab.web.action.ActionExceptionCode.ACTION_FORWARD_TO_JSP_ERROR;

public class GetHomeAction implements Action {

    private static final String JSP = "/WEB-INF/jsp/home.jsp";

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
        try {
            req.getRequestDispatcher(JSP).forward(req, resp);
        } catch (ServletException | IOException e) {
            String[] detailArray = {e.getMessage(), JSP};
            throw new AppException(ACTION_FORWARD_TO_JSP_ERROR, e.getMessage(),
                    detailArray);
        }
    }

}
