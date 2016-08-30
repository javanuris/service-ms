package com.epam.java.rt.lab.action.profile;

import com.epam.java.rt.lab.action.Action;
import com.epam.java.rt.lab.action.ActionException;
import com.epam.java.rt.lab.action.WebAction;
import com.epam.java.rt.lab.component.FormComponent;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.LoginService;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.FormValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Service Management System
 */
@WebAction
public class LoginAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(LoginAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try {
            FormComponent formComponent = (FormComponent) req.getSession().getAttribute("loginForm");
            if (req.getMethod().equals("GET")) {
                logger.debug("GET");
                if (formComponent != null) {
                    for (FormComponent.FormItem formItem : formComponent.getFormItemArray()) formItem.setValue("");
                } else {
                    formComponent = new FormComponent("login", "/profile/login",
                            new FormComponent.FormItem
                                    ("profile.login.form.email", "input", "profile.login.form.email", ""),
                            new FormComponent.FormItem
                                    ("profile.login.form.password", "password", "profile.login.form.password", ""),
                            new FormComponent.FormItem
                                    ("profile.login.form.submit", "submit", "profile.login.form.submit", ""));
                    req.getSession().setAttribute("loginForm", formComponent);
                    req.getRequestDispatcher("/WEB-INF/jsp/profile/login.jsp").forward(req, resp);
                }
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
                        String[] validationMessageArray = {"profile.login.message.access-denied"};
                        formComponent.getFormItemArray()[2].setValidationMessageArray(validationMessageArray);
                    } else {
                        logger.debug("GRANTED");
                        req.getSession().removeAttribute("loginForm");
                        User user = UserService.getUser(login);
                        if (user == null) throw new ActionException("profile.login.message.user-not-found");
                        req.getSession().setAttribute("user", user);
                        logger.debug("REDIRECTING");
                        String next = (String) req.getAttribute("next");
                        if (next == null) next = "/";
                        resp.sendRedirect(next);
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