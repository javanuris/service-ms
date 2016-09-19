package com.epam.java.rt.lab.action.profile;

import com.epam.java.rt.lab.action.Action;
import com.epam.java.rt.lab.action.ActionException;
import com.epam.java.rt.lab.action.WebAction;
import com.epam.java.rt.lab.component.FormComponent;
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
 * service-ms
 */
@WebAction
public class EditAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(LoginAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try (UserService userService = new UserService()) {
            User user = userService.getUser((Long) req.getSession().getAttribute("userId"));
            FormComponent formComponent = null;
            switch (FormComponent.getStatus("profile.edit", UrlManager.getContextPathInfo(req), 100)) {
                case 1:
                    formComponent = FormComponent.get("profile.edit");
                    break;
                case 0:
                    formComponent = FormComponent.set("profile.edit",
                            new FormComponent.Item("profile.edit.first-name.label", "input", "profile.edit.first-name.label"),
                            new FormComponent.Item("profile.edit.middle-name.label", "input", "profile.edit.middle-name.label"),
                            new FormComponent.Item("profile.edit.last-name.label", "input", "profile.edit.last-name.label"),
                            new FormComponent.Item("profile.edit.avatar.label", "image", UrlManager.getContextUri(req, "/file/upload/avatar")),
                            new FormComponent.Item("profile.edit.remove-avatar.label", "button",
                                    UrlManager.getContextUri(req, "/profile/edit", "sub-action=remove-avatar")),
                            new FormComponent.Item("profile.edit.submit.label", "submit",
                                    ""),
                            new FormComponent.Item("profile.edit.view-profile.label", "button",
                                    UrlManager.getContextUri(req, "/profile/view"))
                    );
                    break;
                case -1:
                    throw new ActionException("exception.action.login.form-status");
            }
            req.setAttribute("editForm", formComponent);
            if ("GET".equals(req.getMethod())) {
                logger.debug("GET");
                formComponent.getItem(0).setValue(user.getFirstName());
                formComponent.getItem(1).setValue(user.getMiddleName());
                formComponent.getItem(2).setValue(user.getLastName());
                if (user.getAvatarId() == null) {
                    formComponent.getItem(3).setValue(UrlManager.getContextUri(req, "/file/download/avatar"));
                } else {
                    formComponent.getItem(3).setValue(UrlManager.getContextRef(req, "/file/download/avatar", "id", user.getAvatarId()));
                }
                String subAction = req.getParameter("sub-action");
                if (subAction != null) {
                    switch (subAction) {
                        case "remove-avatar":
                            logger.debug("REMOVE-AVATAR");
                            userService.removeAvatar(user);
                            if (userService.updateUser(user) != 1) {
                                logger.debug("REMOVE ERROR");
                                String[] validationMessageArray = {"profile.edit.avatar.error-remove"};
                                formComponent.getItem(3).setValidationMessageArray(validationMessageArray);
                            } else {
                                logger.debug("REMOVE SUCCESS");
                                resp.sendRedirect(UrlManager.getContextUri(req, "/profile/edit"));
                                return;
                            }
                    }
                }
            } else if ("POST".equals(req.getMethod())) {
                logger.debug("POST");
                if (FormManager.validate(req, formComponent)) {
                    logger.debug("FORM VALID");
                    user.setFirstName(formComponent.getItem(0).getValue());
                    user.setMiddleName(formComponent.getItem(1).getValue());
                    user.setLastName(formComponent.getItem(2).getValue());
                    if (formComponent.getItem(3).getValue().length() > 0)
                        userService.setAvatar(user, formComponent.getItem(3).getValue());
                    if (userService.updateUser(user) != 1) {
                        logger.debug("UPDATE ERROR");
                        String[] validationMessageArray = {"profile.edit.submit.error-edit"};
                        formComponent.getItem(4).setValidationMessageArray(validationMessageArray);
                    } else {
                        logger.debug("UPDATE SUCCESS");
                        req.getSession().setAttribute("userName", user.getName());
                        resp.sendRedirect(UrlManager.getContextUri(req, "/profile/view"));
                        return;
                    }
                }
            }
            req.getRequestDispatcher("/WEB-INF/jsp/profile/edit.jsp").forward(req, resp);
        } catch (ConnectionException | DaoException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.edit.user-service", e.getCause());
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.edit.forward", e.getCause());
        }
    }
}