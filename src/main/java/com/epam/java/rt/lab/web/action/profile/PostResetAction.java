package com.epam.java.rt.lab.web.action.profile;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.service.LoginService;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.BaseAction;
import com.epam.java.rt.lab.web.component.FormControlValue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.epam.java.rt.lab.util.PropertyManager.*;
import static com.epam.java.rt.lab.web.action.ActionExceptionCode.ACTION_FORWARD_TO_JSP_ERROR;

public class PostResetAction extends BaseAction implements Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
        FormControlValue passwordValue =
                new FormControlValue(req.getParameter(LOGIN_PASSWORD));
        FormControlValue newPasswordValue =
                new FormControlValue(req.getParameter(LOGIN_NEW_PASSWORD));
        FormControlValue repeatPasswordValue =
                new FormControlValue(req.getParameter(LOGIN_REPEAT_PASSWORD));
        try (LoginService loginService = new LoginService()) {
            if (loginService.resetPassword(req.getSession(), passwordValue,
                    newPasswordValue, repeatPasswordValue)) {
                req.getSession().setAttribute(MESSAGE_ATTR,
                        "message.info.reset-password.success");
                resp.sendRedirect(UrlManager.getUriWithContext(req, HOME_PATH));
                return;
            }
            req.setAttribute(LOGIN_PASSWORD, passwordValue);
            req.setAttribute(LOGIN_NEW_PASSWORD, newPasswordValue);
            req.setAttribute(LOGIN_REPEAT_PASSWORD, repeatPasswordValue);
            req.getRequestDispatcher(super.getJspName()).forward(req, resp);
        } catch (IOException | ServletException e) {
            throw new AppException(ACTION_FORWARD_TO_JSP_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

}