package com.epam.java.rt.lab.web.action.profile;

import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.Restore;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.LoginService;
import com.epam.java.rt.lab.service.ServiceException;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.*;
import com.epam.java.rt.lab.util.validator.FormValidator;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.ActionException;
import com.epam.java.rt.lab.web.component.form.Form;
import com.epam.java.rt.lab.web.component.form.FormException;
import com.epam.java.rt.lab.web.component.form.FormFactory;
import com.epam.java.rt.lab.web.component.navigation.NavigationException;
import com.epam.java.rt.lab.web.component.navigation.NavigationFactory;
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
        RESTORE
    }

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try (LoginService loginService = new LoginService()) {
            Form form = FormFactory.getInstance().create("login-profile");
            Submit submit = req.getParameter(form.getItem(3).getName()) != null ? Submit.LOGIN :
                    req.getParameter(form.getItem(4).getName()) != null ? Submit.RESTORE : null;
            if (submit == Submit.RESTORE) form.getItem(1).setIgnoreValidate(true);
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
                    case RESTORE:
                        logger.debug("SUBMIT-FORGOT");
                        if (restore(req, resp, form, loginService, login)) {
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
                form.getItem(3).addValidationMessage("message.profile.email-password-incorrect");
            } else if (login.getAttemptLeft() == 0 || login.getStatus() < 0) {
                logger.debug("LOGIN ZERO ATTEMPT LEFT OR BLOCKED");
                form.getItem(3).addValidationMessage("message.profile.login-blocked");
            } else if (!hashPassword(login.getSalt(), form.getItem(1).getValue()).equals(login.getPassword())) {
                logger.debug("LOGIN PASSWORD HASH NOT EQUAL");
                form.getItem(3).addValidationMessage("message.profile.email-password-incorrect");
                login.setAttemptLeft(login.getAttemptLeft() - 1);
                loginService.updateAttemptLeft(login);
            } else {
                logger.debug("LOGIN PASSWORD EQUAL");
                login.setAttemptLeft(Integer.valueOf(PropertyManager.getProperty("login.attempt.max")));
                loginService.updateAttemptLeft(login);
                UserService userService = new UserService();
                User user = userService.getUser(login);
                if (user == null)
                    throw new ActionException("exception.action.profile.login.user");
                req.getSession().setAttribute("user", user);
                req.getSession().setAttribute("navigationList",
                        NavigationFactory.getInstance().create(user.getRole().getName()));
                if (form.getItem(2).getValue() != null) {
                    logger.debug("LOGIN USER REMEMBER");
                    userService.addRemember(req, resp, user);
                }
                return true;
            }
            return false;
        } catch (NoSuchAlgorithmException e) {
            throw new ActionException("exception.action.post-login.login.hash", e.getCause());
        } catch (ServiceException e) {
            throw new ActionException("exception.action.post-login.category", e.getCause());
        } catch (NavigationException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.post-login.navigation", e.getCause());
        }
    }

    private boolean restore(HttpServletRequest req, HttpServletResponse resp, Form form, LoginService loginService, Login login)
            throws ActionException{
        if (login == null) {
            logger.debug("EMAIL NOT EXISTS");
            form.getItem(0).addValidationMessage("message.profile.email-not-exist");
        } else if (login.getAttemptLeft() == 0 || login.getStatus() < 0) {
            logger.debug("LOGIN ZERO ATTEMPT LEFT OR BLOCKED");
            form.getItem(0).addValidationMessage("message.profile.login-blocked");
        } else {
            logger.debug("RESTORE ACCEPTED");
            try {
                Restore restore = new Restore();
                restore.setLogin(login);
                restore.setCode(UUID.randomUUID().toString());
                restore.setCookieName(CookieManager.getUserAgentCookieName(req));
                restore.setCookieValue(HashGenerator.hashString(restore.getCode()));
                restore.setValid(TimestampManager.secondsToTimestamp(
                        TimestampManager.getCurrentTimestamp(),
                        Integer.valueOf(PropertyManager.getProperty("restore.seconds.valid"))
                ));
                if (loginService.addRestore(restore) == 0) {
                    logger.debug("STORING RESTORE CODE FAILED");
                    // TODO: need some reaction
                } else {
                    CookieManager.setCookie(
                            resp,
                            restore.getCookieName(),
                            restore.getCookieValue(),
                            TimestampManager.secondsBetweenTimestamps(
                                    TimestampManager.getCurrentTimestamp(),
                                    restore.getValid()
                            ),
                            UrlManager.getContextUri(req, "")
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

}