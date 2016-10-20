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
import java.util.Map;

import static com.epam.java.rt.lab.util.PropertyManager.*;
import static com.epam.java.rt.lab.web.action.ActionExceptionCode.ACTION_FORWARD_TO_JSP_ERROR;

public class PostLoginAction extends BaseAction implements Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
        FormControlValue emailValue =
                new FormControlValue(req.getParameter(FORM_EMAIL));
        FormControlValue passwordValue =
                new FormControlValue(req.getParameter(FORM_PASSWORD));
        FormControlValue rememberMeValue =
                ((req.getParameter(FORM_REMEMBER_ME) == null)
                        ? new FormControlValue()
                        : new FormControlValue("checked"));
        try (LoginService loginService = new LoginService()) {
            if (req.getParameter(FORM_SUBMIT_LOGIN) != null) {
                if (loginService.login(req, resp,
                        emailValue, passwordValue, rememberMeValue)) {
                    Map<String, String> parameterMap = UrlManager.
                            getRequestParameterMap(req.getQueryString());
                    String redirect = parameterMap.remove(REDIRECT);
                    if (redirect == null) redirect = HOME_PATH;
                    if (parameterMap.size() > 0) {
                        resp.sendRedirect(UrlManager.
                                getUriWithContext(req, redirect, parameterMap));
                    } else {
                        resp.sendRedirect(UrlManager.
                                getUriWithContext(req, redirect));
                    }
                    return;
                }
            } else if (req.getParameter(FORM_SUBMIT_RESTORE) != null) {
                if (loginService.restore(req, resp, emailValue)) {
                    resp.sendRedirect(UrlManager.
                            getUriWithContext(req, HOME_PATH));
                    return;
                }
            }
            req.setAttribute(FORM_EMAIL, emailValue);
            req.setAttribute(FORM_PASSWORD, passwordValue);
            req.setAttribute(FORM_REMEMBER_ME, rememberMeValue);
            req.getRequestDispatcher(super.getJspName()).forward(req, resp);
        } catch (ServletException | IOException e) {
            String[] detailArray = {super.getJspName()};
            throw new AppException(ACTION_FORWARD_TO_JSP_ERROR,
                    e.getMessage(), e.getCause(), detailArray);
        }
    }

}