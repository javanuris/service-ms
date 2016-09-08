package com.epam.java.rt.lab.action.profile;

import com.epam.java.rt.lab.action.Action;
import com.epam.java.rt.lab.action.ActionException;
import com.epam.java.rt.lab.action.WebAction;
import com.epam.java.rt.lab.component.FormComponent;
import com.epam.java.rt.lab.component.NavbarComponent;
import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.LoginService;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.servlet.ResponseCookie;
import com.epam.java.rt.lab.util.FormValidator;
import com.epam.java.rt.lab.util.UrlParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * service-ms
 */
@WebAction
public class ResetPasswordAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(LoginAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        FormComponent formComponent = (FormComponent) req.getSession().getAttribute("resetPasswordForm");
        try {
            if (req.getMethod().equals("GET")) {
                logger.debug("GET");
                if (UrlParameter.getUrlParameter(req, "cancel", null) != null) {
                    req.getSession().removeAttribute("resetPasswordForm");
                    resp.sendRedirect("/profile/view");
                    return;
                }
                if (formComponent != null) {
                    for (FormComponent.FormItem formItem : formComponent.getFormItemArray()) {
                        formItem.setValue("");
                        formItem.setValidationMessageArray(null);
                    }
                } else {
                    formComponent = new FormComponent("reset-password", "/profile/reset-password",
                            new FormComponent.FormItem
                                    ("profile.reset-password.password.label", "password", "profile.reset-password.password.label", ""),
                            new FormComponent.FormItem
                                    ("profile.reset-password.confirm.label", "password", "profile.reset-password.confirm.label", ""),
                            new FormComponent.FormItem
                                    ("profile.reset-password.old.label", "password", "profile.reset-password.old.label", ""),
                            new FormComponent.FormItem
                                    ("profile.reset-password.submit.label", "submit", "", ""),
                            new FormComponent.FormItem
                                    ("profile.reset-password.cancel.label", "button", req.getContextPath().concat("/profile/reset-password")
                                            .concat(UrlParameter.combineUrlParameter(new UrlParameter.UrlParameterBuilder("cancel", "true"))), ""));
                    req.getSession().setAttribute("resetPasswordForm", formComponent);
                }
                req.getRequestDispatcher("/WEB-INF/jsp/profile/reset-password.jsp").forward(req, resp);
            } else if (req.getMethod().equals("POST")) {
                logger.debug("POST");
                for (FormComponent.FormItem formItem : formComponent.getFormItemArray())
                    formItem.setValue(req.getParameter(formItem.getLabel()));
                if (FormValidator.validate(formComponent.getFormItemArray())) {
                    logger.debug("VALID");
                    LoginService loginService = new LoginService();
                    Login login = loginService.getLogin(
                            formComponent.getFormItemArray()[0].getValue(),
                            formComponent.getFormItemArray()[1].getValue());
                    if (login == null) {
                        logger.debug("DENIED");
                        String[] validationMessageArray = {"profile.login.submit.error-auth"};
                        formComponent.getFormItemArray()[3].setValidationMessageArray(validationMessageArray);
                    } else {
                        logger.debug("GRANTED");
                        req.getSession().removeAttribute("profileForm");
                        User user = (new UserService()).getUser(login);
                        if (user == null) throw new ActionException("profile.login.message.user-not-found");
                        req.getSession().setAttribute("userId", user.getId());
                        req.getSession().setAttribute("userName", user.getName());
                        req.getSession().setAttribute("navbarItemArray", NavbarComponent.getNavbarItemArray(user.getRole()));
                        logger.debug("REMEMBER = {}", formComponent.getFormItemArray()[2].getValue());
                        if (formComponent.getFormItemArray()[2].getValue() != null) {
                            logger.debug("REMEMBERING USER");
                            ResponseCookie.setCookie(
                                    resp,
                                    UserService.getRememberCookieName(),
                                    UserService.setRememberUserId(user.getId()),
                                    2592000, req.getContextPath().concat("/"));
                        }
                        String redirect = (String) req.getSession().getAttribute("redirect");
                        req.getSession().removeAttribute("redirect");
                        req.getSession().removeAttribute("loginForm");
                        if (redirect == null) redirect = req.getContextPath().concat("/");
                        logger.debug("REDIRECTING ({})", redirect);
                        resp.sendRedirect(redirect);
                        logger.debug("REDIRECTED");
                        return;
                    }
                }
                logger.debug("NOT VALID");
                req.getRequestDispatcher("/WEB-INF/jsp/profile/login.jsp").forward(req, resp);
            }
        } catch (ServletException | IOException | ConnectionException | DaoException | SQLException e) {
            throw new ActionException(e.getMessage());
        }
    }
}