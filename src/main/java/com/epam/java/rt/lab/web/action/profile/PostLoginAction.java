package com.epam.java.rt.lab.web.action.profile;

import com.epam.java.rt.lab.entity.rbac.*;
import com.epam.java.rt.lab.service.LoginService;
import com.epam.java.rt.lab.service.ServiceException;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.*;
import com.epam.java.rt.lab.util.validator.FormValidator;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.ActionException;
import com.epam.java.rt.lab.web.component.NavigationComponent;
import com.epam.java.rt.lab.web.component.form.Form;
import com.epam.java.rt.lab.web.component.form.FormException;
import com.epam.java.rt.lab.web.component.form.FormFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.UUID;

import static com.epam.java.rt.lab.util.HashGenerator.hashPassword;

/**
 * Service Management System
 */
public class PostLoginAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(PostLoginAction.class);

    private enum Submit {
        LOGIN,
        FORGOT,
        REGISTER
    }

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try (LoginService loginService = new LoginService();
             UserService userService = new UserService()) {
            Form form = FormFactory.create("login-profile");
            Submit submit = req.getParameter(form.getItem(3).getName()) != null ? Submit.LOGIN :
                    req.getParameter(form.getItem(4).getName()) != null ? Submit.FORGOT :
                            req.getParameter(form.getItem(5).getName()) != null ? Submit.REGISTER : null;
            if (submit != Submit.LOGIN) form.getItem(1).setIgnoreValidate(true);
            if (FormValidator.validate(req, form)) {
                logger.debug("FORM VALID");
                Login login = loginService.getLogin(form.getItem(0).getValue());
                switch (submit) {
                    case LOGIN:
                        logger.debug("SUBMIT-LOGIN");
                        if (login(req, resp, form, loginService, login)) {
                            Map<String, String> parameterMap = UrlManager.getRequestParameterMap(req.getQueryString());
                            String redirect = parameterMap.remove("redirect");
                            logger.debug("REDIRECT TO {}", redirect);
                            resp.sendRedirect(UrlManager.getContextUri(req, redirect, parameterMap));
                            return;
                        }
                        break;
                    case FORGOT:
                        logger.debug("SUBMIT-FORGOT");
                        if (restore(req, resp, form, loginService, login)) {
                            logger.debug("REDIRECT TO {}", "/home");
                            resp.sendRedirect(UrlManager.getContextUri(req, "/home"));
                            return;
                        }
                        break;
                    case REGISTER:
                        logger.debug("SUBMIT-REGISTER");
                        if (register(req, resp, form, loginService, login)) {
                            logger.debug("REDIRECT TO {}", "/home");
                            resp.sendRedirect(UrlManager.getContextUri(req, "/home"));
                            return;
                        }
                        break;
                }
            }
            req.setAttribute("loginForm", form);
            req.getRequestDispatcher("/WEB-INF/jsp/profile/login.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.login.forward", e.getCause());
        } catch (FormException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.profile.login.form", e.getCause());
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ActionException();
        }
    }

    private boolean login(HttpServletRequest req, HttpServletResponse resp, Form form, LoginService loginService, Login login)
            throws ActionException {
        try {
            if (login == null) {
                logger.debug("LOGIN NOT EXISTS");
                form.getItem(3).addValidationMessage("form.submit-login.error-denied");
            } else if (login.getAttemptLeft() == 0 || login.getStatus() < 0) {
                logger.debug("LOGIN ZERO ATTEMPT LEFT OR BLOCKED");
                form.getItem(3).addValidationMessage("form.submit-login.error-blocked");
            } else if (!hashPassword(login.getSalt(), form.getItem(1).getValue()).equals(login.getPassword())) {
                logger.debug("LOGIN PASSWORD HASH NOT EQUAL");
                form.getItem(3).addValidationMessage("form.submit-login.error-denied");
                login.setAttemptLeft(login.getAttemptLeft() - 1);
                loginService.updateAttemptLeft(login);
            } else {
                logger.debug("LOGIN PASSWORD EQUAL");
                login.setAttemptLeft(Integer.valueOf(GlobalProperties.getProperty("login.attempt.max")));
                loginService.updateAttemptLeft(login);
                UserService userService = new UserService();
                User user = userService.getUser(login);
                if (user == null)
                    throw new ActionException("exception.action.profile.login.user");
                req.getSession().setAttribute("userId", user.getId());
                req.getSession().setAttribute("userName", user.getName());
                req.getSession().setAttribute("navbarItemArray",
                        NavigationComponent.getNavbarItemArray(user.getRole()));
                if (form.getItem(2).getValue() != null) {
                    logger.debug("LOGIN USER REMEMBER");
                    Remember remember = new Remember();
                    remember.setCookieName(CookieManager.getUserAgentCookieName(req));
                    remember.setCookieValue(UUID.randomUUID().toString());
//                    userService.setRemember(remember);
                    CookieManager.setCookie(resp, remember.getCookieName(), remember.getCookieValue(),
                            Integer.valueOf(GlobalProperties.getProperty("remember.days.valid")) * 86400, req.getContextPath());
                }
                return true;
            }
            return false;
        } catch (NoSuchAlgorithmException e) {
            throw new ActionException("exception.action.post-login.login.hash", e.getCause());
        } catch (ServiceException e) {
            throw new ActionException("exception.action.post-login.service", e.getCause());
        }
    }

    private boolean restore(HttpServletRequest req, HttpServletResponse resp, Form form, LoginService loginService, Login login)
            throws ActionException{
        if (login == null) {
            logger.debug("EMAIL NOT EXISTS");
            form.getItem(0).addValidationMessage("profile.login.email.error-exists-restore");
        } else if (login.getAttemptLeft() == 0 && login.getStatus() < 0) {
            logger.debug("LOGIN ZERO ATTEMPT LEFT OR BLOCKED");
            form.getItem(0).addValidationMessage("profile.login.email.error-block-restore");
        } else {
            logger.debug("RESTORE ACCEPTED");
            try {
                Restore restore = new Restore();
                restore.setLogin(login);
                restore.setCode(UUID.randomUUID().toString());
                restore.setCookieName(CookieManager.getUserAgentCookieName(req));
                restore.setCookieValue(HashGenerator.hashString(restore.getCode()));
                restore.setValid(TimestampCompare.secondsToTimestamp(
                        TimestampCompare.getCurrentTimestamp(),
                        Integer.valueOf(GlobalProperties.getProperty("restore.seconds.valid"))
                ));
                if (loginService.addRestore(restore) == 0) {
                    logger.debug("STORING RESTORE CODE FAILED");
                    // TODO: need some reaction
                } else {
                    CookieManager.setCookie(
                            resp,
                            restore.getCookieName(),
                            restore.getCookieValue(),
                            TimestampCompare.secondsBetweenTimestamps(
                                    TimestampCompare.getCurrentTimestamp(),
                                    restore.getValid()
                            ),
                            req.getContextPath()
                    );
                    req.getSession().setAttribute("restoreEmail", form.getItem(0).getValue());
                    req.getSession().setAttribute("restoreRef",
                            UrlManager.getContextRef(req, "/profile/restore-password", "email, code",
                                    form.getItem(0).getValue(), restore.getCode()));
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean register(HttpServletRequest req, HttpServletResponse resp, Form form, LoginService loginService, Login login)
            throws ActionException {
        try {
            if (login != null) {
                logger.debug("EMAIL ALREADY EXISTS");
                form.getItem(0).addValidationMessage("profile.login.email.error-exists-register");
            } else {
                logger.debug("ACTIVATE ACCEPTED");
                Activate activate = new Activate();
                activate.setEmail(form.getItem(0).getValue());
                activate.setSalt(UUID.randomUUID().toString());
                activate.setPassword(HashGenerator.hashPassword(activate.getSalt(), form.getItem(1).getValue()));
                activate.setCode(UUID.randomUUID().toString());
                activate.setValid(TimestampCompare.daysToTimestamp(
                        TimestampCompare.getCurrentTimestamp(),
                        Integer.valueOf(GlobalProperties.getProperty("activation.days.valid"))));
                if (loginService.addActivate(activate) == 0) {
                    logger.debug("STORING ACTIVATE CODE FAILED");
                    // TODO: need some reaction
                } else {
                    req.getSession().setAttribute("activationEmail", form.getItem(0).getValue());
                    req.getSession().setAttribute("activationRef",
                            UrlManager.getContextRef(req, "/profile/activate", "email, code",
                                    form.getItem(0).getValue(), activate.getCode()));
                    return true;
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.post-login.hash", e.getCause());
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.post-login.activate", e.getCause());
        }
        return false;
    }

}