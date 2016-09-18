package com.epam.java.rt.lab.action.profile;

import com.epam.java.rt.lab.action.Action;
import com.epam.java.rt.lab.action.ActionException;
import com.epam.java.rt.lab.action.WebAction;
import com.epam.java.rt.lab.component.FormComponent;
import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.LoginService;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.FormManager;
import com.epam.java.rt.lab.util.UrlManager;
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
public class SetPasswordAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(LoginAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try (LoginService loginService = new LoginService()) {
            FormComponent formComponent = null;
            switch (FormComponent.getStatus("profile.reset-password", UrlManager.getContextPathInfo(req), 100)) {
                case 1:
                    formComponent = FormComponent.get("profile.reset-password");
                    break;
                case 0:
                    formComponent = FormComponent.set("profile.reset-password",
                            new FormComponent.Item("profile.set-password.password.label", "password", "profile.set-password.password.label"),
                            new FormComponent.Item("profile.set-password.confirm.label", "password", "profile.set-password.confirm.label"),
                            new FormComponent.Item("profile.set-password.current.label", "password", "profile.set-password.current.label"),
                            new FormComponent.Item("profile.set-password.submit.label", "submit", ""),
                            new FormComponent.Item("profile.set-password.view-profile.label", "button",
                                    UrlManager.getContextUri(req, "/profile/view"))
                    );
                    break;
                case -1:
                    throw new ActionException("exception.action.reset-password.form-status");
            }
            req.setAttribute("resetPasswordForm", formComponent);
            if ("GET".equals(req.getMethod())) {
                logger.debug("GET");
                //
            } else if ("POST".equals(req.getMethod())) {
                logger.debug("POST");
                if (FormManager.setValuesAndValidate(req, formComponent)) {
                    logger.debug("FORM VALID");
                    if (formComponent.getItem(0).getValue().equals(formComponent.getItem(1).getValue())) {
                        logger.debug("PASSWORD AND CONFIRM EQUAL");
                        UserService userService = new UserService();
                        User user = userService.getUser((Long) req.getSession().getAttribute("userId"));
                        if (user.getLogin().getPassword().equals(formComponent.getItem(2).getValue())) {
                            logger.debug("GRANTED");
                            user.getLogin().setPassword(formComponent.getItem(0).getValue());
                            if (loginService.updatePassword(user.getLogin()) != 1) {
                                logger.debug("UPDATE ERROR");
                                String[] validationMessageArray = {"profile.reset-password.submit.error-reset-password"};
                                formComponent.getItem(3).setValidationMessageArray(validationMessageArray);
                            } else {
                                logger.debug("UPDATE SUCCESS");
                                resp.sendRedirect(UrlManager.getContextUri(req, "/profile/view"));
                                return;
                            }
                        } else {
                            logger.debug("DENIED");
                            String[] validationMessageArray = {"profile.set-password.current.error-incorrect"};
                            formComponent.getItem(2).setValidationMessageArray(validationMessageArray);
                        }
                    } else {
                        logger.debug("PASSWORD AND CONFIRM NOT EQUAL");
                        String[] validationMessageArray = {"profile.set-password.confirm.error-not-equal"};
                        formComponent.getItem(1).setValidationMessageArray(validationMessageArray);
                    }
                }
            }
            req.getRequestDispatcher("/WEB-INF/jsp/profile/set-password.jsp").forward(req, resp);
        } catch (ConnectionException | DaoException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.reset-password.login-service", e.getCause());
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.reset-password.forward", e.getCause());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.reset-password.update-login", e.getCause());
        }
    }
}