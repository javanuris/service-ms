package com.epam.java.rt.lab.web.action.profile;

import com.epam.java.rt.lab.entity.access.Login;
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

public class PostRestoreAction extends BaseAction implements Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
        FormControlValue newPasswordValue =
                new FormControlValue(req.getParameter(FORM_NEW_PASSWORD));
        FormControlValue repeatPasswordValue =
                new FormControlValue(req.getParameter(FORM_REPEAT_PASSWORD));
        try (LoginService loginService = new LoginService()) {
            Login login = (Login) req.getSession().getAttribute(LOGIN_ATTR);
            if (loginService.resetPassword(login,
                    newPasswordValue, repeatPasswordValue)) {
                req.getSession().removeAttribute(LOGIN_ATTR);
                req.getSession().setAttribute(MESSAGE_ATTR,
                        "message.info.reset-password.success");
                resp.sendRedirect(UrlManager.getUriWithContext(req, HOME_PATH));
                return;
            }
            req.setAttribute(FORM_NEW_PASSWORD, newPasswordValue);
            req.setAttribute(FORM_REPEAT_PASSWORD, repeatPasswordValue);
            req.getRequestDispatcher(super.getJspName()).forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new AppException(ACTION_FORWARD_TO_JSP_ERROR);
        }
    }
}