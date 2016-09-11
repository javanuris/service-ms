package com.epam.java.rt.lab.action.profile;

import com.epam.java.rt.lab.action.Action;
import com.epam.java.rt.lab.action.ActionException;
import com.epam.java.rt.lab.action.WebAction;
import com.epam.java.rt.lab.component.ViewComponent;
import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Service Management System
 */
@WebAction
public class ViewAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ViewAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try {
            logger.debug("/WEB-INF/jsp/profile/view.jsp");
            User user = (new UserService()).getUser((Long) req.getSession().getAttribute("userId"));
            req.getSession().setAttribute("profileView", new ViewComponent(
                    new ViewComponent.ViewItem
                            ("profile.view.first-name.label", "input", user.getFirstName()),
                    new ViewComponent.ViewItem
                            ("profile.view.middle-name.label", "input", user.getMiddleName()),
                    new ViewComponent.ViewItem
                            ("profile.view.last-name.label", "input", user.getLastName()),
                    new ViewComponent.ViewItem
                            ("profile.view.role-name.label", "input", user.getRole().getName()),
                    new ViewComponent.ViewItem
                            ("profile.view.login-email.label", "input", user.getLogin().getEmail()),
                    new ViewComponent.ViewItem
                            ("profile.view.reset-login-password.label", "button", req.getContextPath().concat("/profile/reset-password")),
                    new ViewComponent.ViewItem
                            ("profile.view.edit-profile.label", "button", req.getContextPath().concat("/profile/edit"))));
            req.getRequestDispatcher("/WEB-INF/jsp/profile/view.jsp").forward(req, resp);
        } catch (ServletException | IOException | ConnectionException | DaoException e) {
            throw new ActionException("exception.action.view", e.getCause());
        }
    }

}
