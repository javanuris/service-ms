package com.epam.java.rt.lab.action.profile;

import com.epam.java.rt.lab.action.Action;
import com.epam.java.rt.lab.action.ActionException;
import com.epam.java.rt.lab.action.WebAction;
import com.epam.java.rt.lab.component.FormComponent;
import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.Role;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.LoginService;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.CookieManager;
import com.epam.java.rt.lab.util.FormManager;
import com.epam.java.rt.lab.util.TimestampManager;
import com.epam.java.rt.lab.util.UrlManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * service-ms
 */
@WebAction
public class ForgotPasswordAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try (LoginService loginService = new LoginService()) {
            FormComponent formComponent = null;
            switch (FormComponent.getStatus("profile.forgot-password", UrlManager.getContextPathInfo(req), 100)) {
                case 1:
                    formComponent = FormComponent.get("profile.forgot-password");
                    break;
                case 0:
                    formComponent = FormComponent.set("profile.forgot-password",
                            new FormComponent.Item("profile.forgot-password.password.label", "password", "profile.forgot-password.password.label"),
                            new FormComponent.Item("profile.forgot-password.confirm.label", "password", "profile.forgot-password.confirm.label"),
                            new FormComponent.Item("profile.forgot-password.submit.label", "submit", ""),
                            new FormComponent.Item("profile.forgot-password.login.label", "button",
                                    UrlManager.getContextUri(req, "/profile/login"))
                    );
                    break;
                case -1:
                    throw new ActionException("exception.action.forgot-password.form-status");
            }
            req.setAttribute("forgotPasswordForm", formComponent);
            String forgotEmail = req.getParameter("email");
            String forgotCode = req.getParameter("code");
            logger.debug("email: {}, code: {}", forgotEmail, forgotCode);
            req.getSession().removeAttribute("forgotEmail");
            req.getSession().removeAttribute("forgotCode");
            req.getSession().removeAttribute("forgotRef");
            String forgotCookieName = "_".concat(CookieManager.getDependantCookieName(req));
            String forgotCookieValue = CookieManager.getDependantCookieValue(req, forgotCookieName);
            logger.debug("forgotCookieValue = {}", forgotCookieValue);
            Login login = null;
            if (forgotCookieValue != null && forgotEmail != null && forgotCode != null &&
                    forgotCookieValue.equals(forgotCode)) {
                login = loginService.confirmForgotCode(forgotEmail, forgotCode);
            } else {
                CookieManager.removeDependantCookieValue(req, resp, forgotCookieName);
                resp.sendRedirect(UrlManager.getContextUri(req, "/home"));
                return;
            }
            if ("GET".equals(req.getMethod())) {
                logger.debug("GET");
                if (login == null) {
                    req.getSession().setAttribute("message", "message.forgot.error-confirm");
                    resp.sendRedirect(UrlManager.getContextUri(req, "/home"));
                    return;
                }
                formComponent.setActionParameterString(UrlManager.getRequestParameterString(req));
            } else if ("POST".equals(req.getMethod())) {
                logger.debug("POST");
                String[] parameterArray = {
                        "email=".concat(req.getParameter("email")),
                        "code=".concat(req.getParameter("code"))
                };
                formComponent.setActionParameterString(UrlManager.getRequestParameterString(parameterArray));
                if (FormManager.setValuesAndValidate(req, formComponent)) {
                    logger.debug("FORM VALID");
                    if (formComponent.getItem(0).getValue().equals(formComponent.getItem(1).getValue())) {
                        logger.debug("PASSWORD AND CONFIRM EQUAL");
                        login.setPassword(formComponent.getItem(0).getValue());
                        if (loginService.updatePassword(login) != 1) {
                            logger.debug("UPDATE ERROR");
                            String[] validationMessageArray = {"profile.reset-password.submit.error-reset-password"};
                            formComponent.getItem(2).setValidationMessageArray(validationMessageArray);
                        } else {
                            logger.debug("UPDATE SUCCESS");
                            loginService.removeForgotCode(login.getEmail());
                            CookieManager.removeDependantCookieValue(req, resp, forgotCookieName);
                            resp.sendRedirect(UrlManager.getContextUri(req, "/profile/login"));
                            return;
                        }
                    } else {
                        logger.debug("PASSWORD AND CONFIRM NOT EQUAL");
                        String[] validationMessageArray = {"profile.reset-password.confirm.error-not-equal"};
                        formComponent.getItem(1).setValidationMessageArray(validationMessageArray);
                    }
                }
            }
            req.getRequestDispatcher("/WEB-INF/jsp/profile/forgot-password.jsp").forward(req, resp);
        } catch (ConnectionException | DaoException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.forgot-password.login-service", e.getCause());
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.forgot-password.forward", e.getCause());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.forgot-password.update-login", e.getCause());
        }
    }
}