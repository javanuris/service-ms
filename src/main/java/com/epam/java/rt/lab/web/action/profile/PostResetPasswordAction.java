package com.epam.java.rt.lab.web.action.profile;

import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.LoginService;
import com.epam.java.rt.lab.service.ServiceException;
import com.epam.java.rt.lab.util.PropertyManager;
import com.epam.java.rt.lab.util.HashGenerator;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.util.validator.FormValidator;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.ActionException;
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
import java.util.UUID;

/**
 * category-ms
 */
public class PostResetPasswordAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(GetLoginAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try (LoginService loginService = new LoginService()) {
            Form form = FormFactory.getInstance().create("reset-password");
            if (FormValidator.validate(req, form)) {
                logger.debug("FORM VALID");
                if (!form.getItem(0).getValue().equals(form.getItem(1).getValue())) {
                    form.getItem(1).addValidationMessage("message.profile.repeat-not-equal");
                } else {
                    User user = (User) req.getSession().getAttribute("user");
                    if (user != null && user.getLogin().getAttemptLeft() > 0 && user.getLogin().getStatus() >= 0) {
                        if (!HashGenerator.hashPassword(user.getLogin().getSalt(), form.getItem(2).getValue())
                                .equals(user.getLogin().getPassword())) {
                            form.getItem(2).addValidationMessage("message.profile.password-incorrect");
                        } else {
                            user.getLogin().setSalt(UUID.randomUUID().toString());
                            user.getLogin().setPassword(HashGenerator.hashPassword(user.getLogin().getSalt(), form.getItem(0).getValue()));
                            user.getLogin().setAttemptLeft(Integer.valueOf(PropertyManager.getProperty("login.attempt.max")));
                            loginService.updateLogin(user.getLogin());
                            resp.sendRedirect(UrlManager.getContextUri(req, "/profile/view"));
                            return;
                        }
                    }
                }
            }
            req.setAttribute("resetPasswordForm", form);
            req.getRequestDispatcher("/WEB-INF/jsp/profile/reset-password.jsp").forward(req, resp);
        } catch (FormException e) {
            throw new ActionException("exception.action.profile.reset-password.form", e.getCause());
        } catch (ServletException | IOException e) {
            throw new ActionException("exception.action.profile.reset-password.jsp", e.getCause());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.profile.reset-password.hash", e.getCause());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}