package com.epam.java.rt.lab.action.profile;

import com.epam.java.rt.lab.action.Action;
import com.epam.java.rt.lab.action.ActionException;
import com.epam.java.rt.lab.action.WebAction;
import com.epam.java.rt.lab.component.FormComponent;
import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.FormValidator;
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
        FormComponent formComponent = (FormComponent) req.getSession().getAttribute("editProfileForm");
        try {
            UserService userService = new UserService();
            if (req.getMethod().equals("GET")) {
                logger.debug("GET");
                if (UrlManager.getUrlParameter(req, "cancel", null) != null) {
                    req.getSession().removeAttribute("editProfileForm");
                    resp.sendRedirect(UrlManager.getContextUri(req, "/profile/view"));
                    return;
                }
                if (formComponent != null) {
                    formComponent.clearValidationMessageArray();
                } else {
                    User user = userService.getUser((Long) req.getSession().getAttribute("userId"));
                    logger.debug("user = {}, {}, {}", user.getFirstName(), user.getMiddleName(), user.getLastName());
                    formComponent = new FormComponent("edit-profile", "/profile/edit",
                            new FormComponent.FormItem
                                    ("profile.edit.first-name.label", "text", "profile.edit.first-name.label")
                                    .setValueAndReturn(user.getFirstName()),
                            new FormComponent.FormItem
                                    ("profile.edit.middle-name.label", "text", "profile.edit.middle-name.label")
                                    .setValueAndReturn(user.getMiddleName()),
                            new FormComponent.FormItem
                                    ("profile.edit.last-name.label", "text", "profile.edit.last-name.label")
                                    .setValueAndReturn(user.getLastName()),
                            new FormComponent.FormItem
                                    ("profile.edit.avatar.label", "file", "profile.edit.avatar.placeholder")
                                    .setValueAndReturn(user.getAvatarId()),
                            new FormComponent.FormItem
                                    ("profile.edit.submit.label", "submit", ""),
                            new FormComponent.FormItem
                                    ("profile.edit.cancel.label", "button",
                                            UrlManager.getUriForButton(req, "/profile/edit", "cancel"), ""));
                    logger.debug("item.value = {}", formComponent.getFormItemArray()[0].getValue());
                    req.getSession().setAttribute("editProfileForm", formComponent);
                }
                req.getRequestDispatcher("/WEB-INF/jsp/profile/edit.jsp").forward(req, resp);
            } else if (req.getMethod().equals("POST")) {
                logger.debug("POST");
                if (FormValidator.setValueAndValidate(req, formComponent.getFormItemArray())) {
                    logger.debug("VALID");
                    User user = userService.getUser((Long) req.getSession().getAttribute("userId"));
                    user.setFirstName(formComponent.getFormItemArray()[0].getValue());
                    user.setMiddleName(formComponent.getFormItemArray()[1].getValue());
                    user.setLastName(formComponent.getFormItemArray()[2].getValue());
                    if (formComponent.getFormItemArray()[3].getValue().length() > 0)
                        userService.putAvatar(user, formComponent.getFormItemArray()[3].getValue());
                    logger.debug("user.getName() = {}", user.getName());
                    if (userService.updateUser(user) != 1) {
                        logger.debug("UPDATE ERROR");
                        String[] validationMessageArray = {"profile.edit.submit.error-edit"};
                        formComponent.getFormItemArray()[3].setValidationMessageArray(validationMessageArray);
                    } else {
                        logger.debug("UPDATE SUCCESS");
                        req.getSession().removeAttribute("editProfileForm");
                        req.getSession().setAttribute("userName", user.getName());
                        resp.sendRedirect(UrlManager.getContextUri(req, "/profile/view"));
                        return;
                    }
                }
                logger.debug("NOT VALID");
                req.getRequestDispatcher("/WEB-INF/jsp/profile/edit.jsp").forward(req, resp);
            }
        } catch (ServletException | IOException | ConnectionException | DaoException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.edit", e.getCause());
        }
    }
}