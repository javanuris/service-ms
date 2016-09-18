package com.epam.java.rt.lab.action.profile;

import com.epam.java.rt.lab.action.Action;
import com.epam.java.rt.lab.action.ActionException;
import com.epam.java.rt.lab.action.WebAction;
import com.epam.java.rt.lab.component.FormComponent;
import com.epam.java.rt.lab.component.NavigationComponent;
import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.LoginService;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Service Management System
 */
@WebAction
public class LoginAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(LoginAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try (LoginService loginService = new LoginService()) {
            FormComponent formComponent = null;
            switch (FormComponent.getStatus("profile.login", UrlManager.getContextPathInfo(req), 100)) {
                case 1:
                    formComponent = FormComponent.get("profile.login");
                    break;
                case 0:
                    formComponent = FormComponent.set("profile.login",
                            new FormComponent.Item("profile.login.email.label", "input", "profile.login.email.label"),
                            new FormComponent.Item("profile.login.password.label", "password", "profile.login.password.label"),
                            new FormComponent.Item("profile.login.remember.label", "checkbox", "profile.login.remember.label"),
                            new FormComponent.Item("profile.login.submit-login.label", "submit", "submit-login"),
                            new FormComponent.Item("profile.login.reset-password.label", "button",
                                    UrlManager.getContextUri(req, "/profile/reset-password")),
                            new FormComponent.Item("profile.login.submit-register.label", "submit", "submit-register")
                    );
                    break;
                case -1:
                    throw new ActionException("exception.action.login.form-status");
            }
            req.setAttribute("loginForm", formComponent);
            if ("GET".equals(req.getMethod())) {
                logger.debug("GET");
                formComponent.setActionParameterString(UrlManager.getRequestParameterString(req));
            } else if ("POST".equals(req.getMethod())) {
                logger.debug("POST");
                if (FormManager.setValuesAndValidate(req, formComponent)) {
                    logger.debug("FORM VALID");
                    if (req.getParameter(formComponent.getItem(3).getPlaceholder()) != null) {
                        logger.debug("SUBMIT-LOGIN");
                        Login login = loginService.getLogin(
                                formComponent.getItem(0).getValue(),
                                formComponent.getItem(1).getValue()
                        );
                        if (login == null) {
                            logger.debug("DENIED");
                            String[] validationMessageArray = {"profile.login.submit-login.error-denied"};
                            formComponent.getItem(3).setValidationMessageArray(validationMessageArray);
                        } else {
                            logger.debug("GRANTED");
                            UserService userService = new UserService();
                            User user = userService.getUser(login);
                            if (user == null)
                                throw new ActionException("exception.action.login.user-login");
                            req.getSession().setAttribute("userId", user.getId());
                            req.getSession().setAttribute("userName", user.getName());
                            req.getSession().setAttribute("navbarItemArray", NavigationComponent.getNavbarItemArray(user.getRole()));
                            if (formComponent.getItem(2).getValue() != null) {
                                logger.debug("REMEMBER ME");
                                try {
                                    String rememberCookieName = CookieManager.getRememberCookieName(req);
                                    String rememberCookieValue = HashManager.hashString(UUID.randomUUID().toString());
                                    CookieManager.setRememberCookieValue(req, resp, rememberCookieName, rememberCookieValue);
                                    Map<String, Object> rememberValueMap = new HashMap<>();
                                    rememberValueMap.put("userId", user.getId());
                                    rememberValueMap.put("name", rememberCookieName);
                                    rememberValueMap.put("value", rememberCookieValue);
                                    rememberValueMap.put("valid",
                                            TimestampManager.daysToTimestamp(TimestampManager.getCurrentTimestamp(), 30));
                                    userService.setRemember(rememberValueMap);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            Map<String, String> parameterMap = UrlManager.getRequestParameterMap(req.getQueryString());
                            String redirect = parameterMap.remove("redirect");
                            logger.debug("REDIRECT TO {}", redirect);
                            resp.sendRedirect(UrlManager.getContextUri(req, redirect, parameterMap));
                            return;
                        }
                    } else if (req.getParameter(formComponent.getItem(5).getPlaceholder()) != null) {
                        logger.debug("SUBMIT-REGISTER");
                        if (loginService.isLoginExists(formComponent.getItem(0).getValue())) {
                            logger.debug("EMAIL EXISTS ERROR");
                            String[] validationMessageArray = {"profile.login.email.error-exists-register"};
                            formComponent.getItem(0).setValidationMessageArray(validationMessageArray);
                        } else {
                            req.getSession().setAttribute("activationEmail", formComponent.getItem(0).getValue());
                            req.getSession().setAttribute("activationCode", loginService.createActivationCode
                                    (formComponent.getItem(0).getValue(), formComponent.getItem(1).getValue()));
                            req.getSession().setAttribute("activationRef", UrlManager.getContextUri(req, "/profile/activate"));
                            resp.sendRedirect(UrlManager.getContextUri(req, "/home"));
                            return;
                        }
                    }
                }
            }
            req.getRequestDispatcher("/WEB-INF/jsp/profile/login.jsp").forward(req, resp);
        } catch (ConnectionException | DaoException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.login.login-service", e.getCause());
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.login.forward", e.getCause());
        }
    }

}