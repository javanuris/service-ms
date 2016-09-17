package com.epam.java.rt.lab.action.profile;

import com.epam.java.rt.lab.action.Action;
import com.epam.java.rt.lab.action.ActionException;
import com.epam.java.rt.lab.action.WebAction;
import com.epam.java.rt.lab.component.FormComponent;
import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.service.ActivationService;
import com.epam.java.rt.lab.service.LoginService;
import com.epam.java.rt.lab.util.FormManager;
import com.epam.java.rt.lab.util.UrlManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * Service Management System
 */
@WebAction
public class RegisterAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(RegisterAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try (LoginService loginService = new LoginService()) {
            FormComponent formComponent = null;
            switch (FormComponent.getStatus("profile.register", UrlManager.getContextPathInfo(req), 100)) {
                case 1:
                    formComponent = FormComponent.get("profile.register");
                    break;
                case 0:
                    formComponent = FormComponent.set("profile.register",
                            new FormComponent.Item("profile.register.first-name.label", "input", "profile.register.first-name.label"),
                            new FormComponent.Item("profile.register.email.label", "input", "profile.register.email.label"),
                            new FormComponent.Item("profile.register.submit.label", "submit", ""),
                            new FormComponent.Item("profile.register.login.label", "button",
                                    UrlManager.getContextUri(req, "/profile/login"))
                    );
                    break;
                case -1:
                    throw new ActionException("exception.action.register.form-status");
            }
            req.setAttribute("registerForm", formComponent);
            if ("GET".equals(req.getMethod())) {
                logger.debug("GET");
                // fake-mail-message
                String subAction = req.getParameter("sub-action");
                if (subAction != null) {
                    switch (subAction) {
                        case "activate":

                            break;
                        case "activate-message":
                            req.setAttribute("activationCode", req.getParameter("activation-code"));
                            req.setAttribute("activationFirstName", req.getParameter("first-name"));
                            req.setAttribute("activationEmail", req.getParameter("email"));
                            req.getRequestDispatcher("/WEB-INF/jsp/profile/activate-message.jsp").forward(req, resp);
                            return;
                    }
                }
            } else if ("POST".equals(req.getMethod())) {
                logger.debug("POST");
                if (FormManager.setValuesAndValidate(req, formComponent)) {
                    logger.debug("FORM VALID");
                    if (loginService.isLoginExists(formComponent.getItem(1).getValue())) {
                        logger.debug("EMAIL EXISTS");
                        String[] validationMessageArray = {"profile.register.email.error-exists"};
                        formComponent.getItem(1).setValidationMessageArray(validationMessageArray);
                    } else {
                        ActivationService activationService = new ActivationService();
                        String activationCode = activationService.getActivationCode
                                (formComponent.getItem(0).getValue(), formComponent.getItem(1).getValue());
                        resp.sendRedirect(UrlManager.getContextRef(req, "/profile/register", "sub-action, first-name, email, activation-code",
                                "activate-message", formComponent.getItem(0).getValue(), formComponent.getItem(1).getValue(), activationCode));
                        return;
                    }
                }
            }
            req.getRequestDispatcher("/WEB-INF/jsp/profile/register.jsp").forward(req, resp);
        } catch (ConnectionException | DaoException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.register.login-service", e.getCause());
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.register.forward", e.getCause());
        }
    }

}