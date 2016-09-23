package com.epam.java.rt.lab.action.profile;

import com.epam.java.rt.lab.action.Action;
import com.epam.java.rt.lab.action.ActionException;
import com.epam.java.rt.lab.action.WebAction;
import com.epam.java.rt.lab.component.ComponentException;
import com.epam.java.rt.lab.component.form.Form;
import com.epam.java.rt.lab.component.NavigationComponent;
import com.epam.java.rt.lab.component.form.FormFactory;
import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.LoginService;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.*;
import com.epam.java.rt.lab.util.validator.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
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
            Form form = FormFactory.getInstance().create("profile-login");
//            switch (Form.getStatus("profile.login", UrlManager.getContextPathInfo(req), 100)) {
//                case 1:
//                    form = Form.create("profile.login");
//                    break;
//                case 0:
//                    form = Form.set("profile.login",
//                            new Form.Item("profile.login.email.label", "input", "profile.login.email.label",
//                                    ValidatorFactory.Validator.getRegex(ValidatorFactory.getRegex("email.regex"), "validation.email")),
//                            new Form.Item("profile.login.password.label", "password", "profile.login.password.label",
//                                    ValidatorFactory.Validator.getRegex(ValidatorFactory.getRegex("password.regex"), "validation.password")),
//                            new Form.Item("profile.login.remember.label", "checkbox", "profile.login.remember.label", null),
//                            new Form.Item("profile.login.submit-login.label", "submit", "submit-login", null),
//                            new Form.Item("profile.login.submit-forgot.label", "submit", "submit-forgot", null),
//                            new Form.Item("profile.login.submit-register.label", "submit", "submit-register", null)
//                    );
//                    break;
//                case -1:
//                    throw new ActionException("exception.action.login.form-status");
//            }
            req.setAttribute("loginForm", form);
            if ("GET".equals(req.getMethod())) {
                logger.debug("GET");
                form.setActionParameterString(UrlManager.getRequestParameterString(req));
            } else if ("POST".equals(req.getMethod())) {
                logger.debug("POST");
                if (req.getParameter(form.getItem(4).getPlaceholder()) != null ||
                        req.getParameter(form.getItem(5).getPlaceholder()) != null)
                    form.getItem(1).setIgnoreValidate(true);
                if (ValidatorFactory.validate(req, form)) {
                    logger.debug("FORM VALID");
                    if (req.getParameter(form.getItem(3).getPlaceholder()) != null) {
                        logger.debug("SUBMIT-LOGIN");
                        Login login = loginService.getLogin(form.getItem(0).getValue());
                        if (login == null || !login.getPassword().equals(form.getItem(1).getValue()) ||
                                login.getAttemptLeft() == 0 || login.getStatus() < 0) {
                            logger.debug("DENIED");
                            if (login != null && (login.getAttemptLeft() <= 0 || login.getStatus() < 0)) {
                                String[] validationMessageArray = {"profile.login.submit-login.error-blocked"};
                                form.getItem(3).setValidationMessageArray(validationMessageArray);
                            } else {
                                if (login != null && login.getAttemptLeft() > 0) {
                                    login.setAttemptLeft(login.getAttemptLeft() - 1);
                                    loginService.updateAttemptLeft(login);
                                }
                                String[] validationMessageArray = {"profile.login.submit-login.error-denied"};
                                form.getItem(3).setValidationMessageArray(validationMessageArray);
                            }
                        } else {
                            logger.debug("GRANTED");
                            login.setAttemptLeft(Integer.valueOf(GlobalProperties.getProperty("login.attempt.max")));
                            loginService.updateAttemptLeft(login);
                            UserService userService = new UserService();
                            User user = userService.getUser(login);
                            if (user == null)
                                throw new ActionException("exception.action.login.user-login");
                            req.getSession().setAttribute("userId", user.getId());
                            req.getSession().setAttribute("userName", user.getName());
                            req.getSession().setAttribute("navbarItemArray", NavigationComponent.getNavbarItemArray(user.getRole()));
                            if (form.getItem(2).getValue() != null) {
                                logger.debug("REMEMBER ME");
                                try {
                                    String rememberCookieName = CookieManager.getDependantCookieName(req);
                                    String rememberCookieValue = HashGenerator.hashString(UUID.randomUUID().toString());
                                    CookieManager.setDependantCookieValue(req, resp, rememberCookieName, rememberCookieValue,
                                            Integer.valueOf(GlobalProperties.getProperty("remember.days.valid")) * 86400);
                                    Map<String, Object> rememberValueMap = new HashMap<>();
                                    rememberValueMap.put("userId", user.getId());
                                    rememberValueMap.put("name.regex", rememberCookieName);
                                    rememberValueMap.put("value", rememberCookieValue);
                                    rememberValueMap.put("valid",
                                            TimestampCompare.daysToTimestamp(TimestampCompare.getCurrentTimestamp(),
                                                    Integer.valueOf(GlobalProperties.getProperty("remember.days.valid"))));
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
                    } else if (req.getParameter(form.getItem(4).getPlaceholder()) != null) {
                        logger.debug("SUBMIT-FORGOT");
                        Login login = loginService.getLogin(form.getItem(0).getValue());
                        if (login == null) {
                            logger.debug("EMAIL NOT EXISTS");
                            String[] validationMessageArray = {"profile.login.email.error-exists-forgot"};
                            form.getItem(0).setValidationMessageArray(validationMessageArray);
                        } else {
                            if (login.getAttemptLeft() > 0 && login.getStatus() >= 0) {
                                try {
                                    String forgotCookieName = "_".concat(CookieManager.getDependantCookieName(req));
                                    String forgotCookieValue = HashGenerator.hashString(UUID.randomUUID().toString());
                                    CookieManager.setDependantCookieValue(req, resp, forgotCookieName, forgotCookieValue,
                                            Integer.valueOf(GlobalProperties.getProperty("forgot.seconds.valid")));
                                    loginService.setForgotCode(form.getItem(0).getValue(), forgotCookieValue);
                                    req.getSession().setAttribute("forgotEmail", form.getItem(0).getValue());
                                    req.getSession().setAttribute("forgotCode", forgotCookieValue);
                                    req.getSession().setAttribute("forgotRef",
                                            UrlManager.getContextRef(req, "/profile/forgot-password", "email, code",
                                                    form.getItem(0).getValue(), forgotCookieValue));
                                    resp.sendRedirect(UrlManager.getContextUri(req, "/home"));
                                    return;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                String[] validationMessageArray = {"profile.login.email.error-block-forgot"};
                                form.getItem(0).setValidationMessageArray(validationMessageArray);
                            }
                        }
                    } else if (req.getParameter(form.getItem(5).getPlaceholder()) != null) {
                        logger.debug("SUBMIT-REGISTER");
                        if (loginService.isLoginExists(form.getItem(0).getValue())) {
                            logger.debug("EMAIL EXISTS ERROR");
                            String[] validationMessageArray = {"profile.login.email.error-exists-register"};
                            form.getItem(0).setValidationMessageArray(validationMessageArray);
                        } else {
                            String activationCode = loginService.createActivationCode
                                    (form.getItem(0).getValue(), form.getItem(1).getValue());
                            req.getSession().setAttribute("activationEmail", form.getItem(0).getValue());
                            req.getSession().setAttribute("activationCode", activationCode);
                            req.getSession().setAttribute("activationRef",
                                    UrlManager.getContextRef(req, "/profile/activate", "email, code",
                                            form.getItem(0).getValue(), activationCode));
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
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.login.update", e.getCause());
        } catch (ComponentException e) {
            e.printStackTrace();
        }
    }

}