package com.epam.java.rt.lab.action.profile;

import com.epam.java.rt.lab.action.Action;
import com.epam.java.rt.lab.action.ActionException;
import com.epam.java.rt.lab.action.WebAction;
import com.epam.java.rt.lab.component.form.Form;
import com.epam.java.rt.lab.component.form.FormException;
import com.epam.java.rt.lab.component.form.FormFactory;
import com.epam.java.rt.lab.util.UrlManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Service Management System
 */
@WebAction
public class GetLoginAction implements Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try {
            Form form = FormFactory.getInstance().create("login-profile");
            form.setActionParameterString(UrlManager.getRequestParameterString(req));
            req.setAttribute("loginForm", form);
        } catch (FormException e) {
            throw new ActionException("exception.action.profile.login.form", e.getCause());
        }
    }

}