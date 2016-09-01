package com.epam.java.rt.lab.action.profile;

import com.epam.java.rt.lab.action.Action;
import com.epam.java.rt.lab.action.ActionException;
import com.epam.java.rt.lab.action.WebAction;
import com.epam.java.rt.lab.component.FormComponent;
import com.epam.java.rt.lab.component.NavbarComponent;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.LoginService;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.servlet.ResponseCookie;
import com.epam.java.rt.lab.util.FormValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Service Management System
 */
@WebAction
public class LoginAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(LoginAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try {
            if (req.getSession().getAttribute("user") != null) {
                logger.debug("REDIRECTING");
                resp.sendRedirect("/profile/view");
                logger.debug("REDIRECTED");
                return;
            }
            FormComponent formComponent = (FormComponent) req.getSession().getAttribute("loginForm");
            if (req.getMethod().equals("GET")) {
                logger.debug("GET");
                if (formComponent != null) {
                    for (FormComponent.FormItem formItem : formComponent.getFormItemArray()) {
                        formItem.setValue("");
                        formItem.setValidationMessageArray(null);
                    }
                } else {
                    formComponent = new FormComponent("login", "/profile/login",
                            new FormComponent.FormItem
                                    ("profile.login.email.label", "input", "profile.login.email.label", ""),
                            new FormComponent.FormItem
                                    ("profile.login.password.label", "password", "profile.login.password.label", ""),
                            new FormComponent.FormItem
                                    ("profile.login.remember.label", "checkbox", "profile.login.remember.label", ""),
                            new FormComponent.FormItem
                                    ("profile.login.submit.label", "submit", "profile.login.submit.label", ""));
                    req.getSession().setAttribute("loginForm", formComponent);
                }
                req.getRequestDispatcher("/WEB-INF/jsp/profile/login.jsp").forward(req, resp);
            } else if (req.getMethod().equals("POST")) {
                logger.debug("POST");
                for (FormComponent.FormItem formItem : formComponent.getFormItemArray())
                    formItem.setValue(req.getParameter(formItem.getLabel()));
                if (FormValidator.validate(formComponent.getFormItemArray())) {
                    logger.debug("VALID");
                    Login login = LoginService.getLogin(
                            formComponent.getFormItemArray()[0].getValue(),
                            formComponent.getFormItemArray()[1].getValue());
                    if (login == null) {
                        logger.debug("DENIED");
                        String[] validationMessageArray = {"profile.login.submit.error-auth"};
                        formComponent.getFormItemArray()[3].setValidationMessageArray(validationMessageArray);
                    } else {
                        logger.debug("GRANTED");
                        req.getSession().removeAttribute("loginForm");
                        User user = UserService.getUser(login);
                        if (user == null) throw new ActionException("profile.login.message.user-not-found");
                        req.getSession().setAttribute("user", user);
                        req.getSession().setAttribute("navbarItemArray", NavbarComponent.getNavbarItemArray(user.getRole()));
                        logger.debug("REMEMBER = {}", formComponent.getFormItemArray()[2].getValue());
                        if (formComponent.getFormItemArray()[2].getValue() != null) {
                            logger.debug("REMEMBERING USER");
                            ResponseCookie.setCookie(
                                    resp,
                                    UserService.getRememberCookieName(),
                                    UserService.setRememberUser(user),
                                    2592000, req.getContextPath().concat("/"));
                        }
                        String redirect = (String) req.getSession().getAttribute("redirect");
                        req.getSession().removeAttribute("redirect");
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
        } catch (ServletException | IOException e) {
            throw new ActionException(e.getMessage());
        }
    }

}