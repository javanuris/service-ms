package com.epam.java.rt.lab.web.action;

import com.epam.java.rt.lab.exception.AppException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.epam.java.rt.lab.util.PropertyManager.MESSAGE_ATTR;
import static com.epam.java.rt.lab.web.action.ActionExceptionCode.ACTION_FORWARD_TO_JSP_ERROR;

public class GetHomeAction extends BaseAction implements Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
        try {
            String message =
                    (String) req.getSession().getAttribute(MESSAGE_ATTR);
            if (message != null) {
                req.getSession().removeAttribute(MESSAGE_ATTR);
                req.setAttribute(MESSAGE_ATTR, message);
            }
            req.getRequestDispatcher(super.getJspName()).forward(req, resp);
        } catch (ServletException | IOException e) {
            String[] detailArray = {super.getJspName()};
            throw new AppException(ACTION_FORWARD_TO_JSP_ERROR,
                    e.getMessage(), e.getCause(), detailArray);
        }
    }

}
