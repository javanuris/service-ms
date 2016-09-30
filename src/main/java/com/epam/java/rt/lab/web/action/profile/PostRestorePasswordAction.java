package com.epam.java.rt.lab.web.action.profile;

import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.service.LoginService;
import com.epam.java.rt.lab.service.ServiceException;
import com.epam.java.rt.lab.util.GlobalProperties;
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
 * service-ms
 */
public class PostRestorePasswordAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(PostRestorePasswordAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try (LoginService loginService = new LoginService()) {
            Form form = FormFactory.create("restore-profile");
            if (FormValidator.validate(req, form)) {
                if (!form.getItem(0).getValue().equals(form.getItem(1).getValue())) {
                    form.getItem(1).addValidationMessage("profile.reset-password.confirm.error-not-equal");
                } else {
                    Login login = (Login) req.getSession().getAttribute("login");
                    req.removeAttribute("login");
                    login.setSalt(UUID.randomUUID().toString());
                    login.setPassword(HashGenerator.hashPassword(login.getSalt(), form.getItem(0).getValue()));
                    login.setAttemptLeft(Integer.valueOf(GlobalProperties.getProperty("login.attempt.max")));
                    loginService.updateLoginAfterRestore(login);
                    resp.sendRedirect(UrlManager.getContextUri(req, "/home"));
                    return;
                }
            }
            req.setAttribute("restoreForm", form);
            req.getRequestDispatcher("/WEB-INF/jsp/profile/restore-password.jsp").forward(req, resp);
        } catch (FormException e) {
            throw new ActionException("exception.action.profile.restore.form", e.getCause());
        } catch (ServletException | IOException e) {
            throw new ActionException("exception.action.profile.restore-password.jsp", e.getCause());
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.restore-password.service", e.getCause());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.restore-password.hash", e.getCause());
        }
    }
}