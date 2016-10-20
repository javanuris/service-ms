package com.epam.java.rt.lab.web.action.profile;

import com.epam.java.rt.lab.entity.access.Login;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.service.LoginService;
import com.epam.java.rt.lab.util.CookieManager;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.ActionExceptionCode;
import com.epam.java.rt.lab.web.action.BaseAction;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.epam.java.rt.lab.entity.access.Login.NULL_LOGIN;
import static com.epam.java.rt.lab.util.PropertyManager.*;
import static com.epam.java.rt.lab.web.action.ActionExceptionCode.ACTION_FORWARD_TO_JSP_ERROR;

public class GetRestoreAction extends BaseAction implements Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
        try (LoginService loginService = new LoginService()) {
            req.getSession().removeAttribute(RESTORE_EMAIL_ATTR);
            req.getSession().removeAttribute(RESTORE_REF_ATTR);
            String email = req.getParameter(FORM_EMAIL);
            String code = req.getParameter(FORM_CODE);
            if (email == null && code == null) {
                resp.sendRedirect(UrlManager.getUriWithContext(req, HOME_PATH));
                return;
            }
            String restoreCookieName =
                    CookieManager.getUserAgentCookieName(req);
            String restoreCookieValue = CookieManager.
                    getCookieValue(req, restoreCookieName);
            CookieManager.removeCookie(resp, restoreCookieName,
                    UrlManager.getUriWithContext(req, ""));
            Login login = loginService.getRestoreLogin(email, code,
                    restoreCookieName, restoreCookieValue);
            if (login == NULL_LOGIN) {
                resp.sendRedirect(UrlManager.getUriWithContext(req, HOME_PATH));
                return;
            }
            req.getSession().setAttribute(LOGIN_ATTR, login);
            req.getRequestDispatcher(super.getJspName()).forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new AppException(ACTION_FORWARD_TO_JSP_ERROR);
        }
    }
}