package com.epam.java.rt.lab.web.action.profile;

import com.epam.java.rt.lab.util.UrlManager;
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

/**
 * Service Management System
 */
public class GetRegisterAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(GetRegisterAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try {
            Form form = FormFactory.getInstance().create("register-profile");
            form.setActionParameterString(UrlManager.getRequestParameterString(req));
            req.setAttribute("registerForm", form);
            req.getRequestDispatcher("/WEB-INF/jsp/profile/register.jsp").forward(req, resp);
        } catch (FormException e) {
            throw new ActionException("exception.action.profile.register.form", e.getCause());
        } catch (ServletException | IOException e) {
            throw new ActionException("exception.action.profile.register.jsp", e.getCause());
        }
    }

}