package com.epam.java.rt.lab.action.rbac.user;

import com.epam.java.rt.lab.action.Action;
import com.epam.java.rt.lab.action.ActionException;
import com.epam.java.rt.lab.action.WebAction;
import com.epam.java.rt.lab.component.ViewComponent;
import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.FormManager;
import com.epam.java.rt.lab.util.UrlManager;
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
public class ViewAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ViewAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try (UserService userService = new UserService()) {
            logger.debug("/WEB-INF/jsp/rbac/user/view.jsp");
            String id = req.getParameter("id");
            if (id == null || !FormManager.isOnlyDigits(id)) {
                resp.sendRedirect("");
                return;
            }
            User user = userService.getUser(Long.valueOf(id));
            req.setAttribute("userProfileView", new ViewComponent(
                    new ViewComponent.ViewItem
                            ("profile.view.first-name.label", "input", user.getFirstName()),
                    new ViewComponent.ViewItem
                            ("profile.view.middle-name.label", "input", user.getMiddleName()),
                    new ViewComponent.ViewItem
                            ("profile.view.last-name.label", "input", user.getLastName()),
                    new ViewComponent.ViewItem
                            ("profile.view.avatar.label", "image",
                                    UrlManager.getContextRef(req, "/file/download/avatar", "id", user.getAvatarId())),
                    new ViewComponent.ViewItem
                            ("profile.view.role-name.label", "input", user.getRole().getName()),
                    new ViewComponent.ViewItem
                            ("profile.view.login-email.label", "input", user.getLogin().getEmail()),
                    new ViewComponent.ViewItem
                            ("profile.view.reset-password.label", "button", UrlManager.getContextUri(req, "/profile/reset-password")),
                    new ViewComponent.ViewItem
                            ("profile.view.edit-profile.label", "button", UrlManager.getContextUri(req, "/profile/edit"))));
            req.getRequestDispatcher("/WEB-INF/jsp/rbac/user/view.jsp").forward(req, resp);
        } catch (ConnectionException | DaoException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.rbac.user.view.user-service", e.getCause());
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.rbac.user.view.forward", e.getCause());
        }
    }

}
