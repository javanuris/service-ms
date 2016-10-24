package com.epam.java.rt.lab.web.action.profile;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.service.LoginService;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.BaseAction;
import com.epam.java.rt.lab.web.component.FormControlValue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.epam.java.rt.lab.util.PropertyManager.*;
import static com.epam.java.rt.lab.web.action.ActionExceptionCode.ACTION_FORWARD_TO_JSP_ERROR;

public class PostEditAction extends BaseAction implements Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
        FormControlValue firstNameValue =
                new FormControlValue(req.getParameter(USER_FIRST_NAME));
        FormControlValue middleNameValue =
                new FormControlValue(req.getParameter(USER_MIDDLE_NAME));
        FormControlValue lastNameValue =
                new FormControlValue(req.getParameter(USER_LAST_NAME));
        FormControlValue avatarDownloadValue =
                new FormControlValue(req.getParameter(USER_AVATAR_DOWNLOAD));
        try (UserService userService = new UserService()) {
            if (req.getParameter(FORM_SUBMIT_SAVE_PROFILE) != null) {
                if (userService.updateUser(req.getSession(),
                        firstNameValue, middleNameValue, lastNameValue,
                        avatarDownloadValue)) {
                    resp.sendRedirect(UrlManager.
                            getUriWithContext(req, PROFILE_VIEW_PATH));
                    return;
                }
            } else if (req.getParameter(FORM_SUBMIT_REMOVE_AVATAR) != null) {

            }
            req.setAttribute(USER_FIRST_NAME, firstNameValue);
            req.setAttribute(USER_MIDDLE_NAME, middleNameValue);
            req.setAttribute(USER_LAST_NAME, lastNameValue);
            req.setAttribute(USER_AVATAR_DOWNLOAD, avatarDownloadValue);
            req.getRequestDispatcher(super.getJspName()).forward(req, resp);

//        try {
//            Form form = FormFactory.getInstance().create("edit-profile");
//            Submit submit = req.getParameter(form.getItem(4).getName()) != null ? Submit.SAVE_PROFILE :
//                    req.getParameter(form.getItem(5).getName()) != null ? Submit.REMOVE_AVATAR : null;
//            switch (submit) {
//                case REMOVE_AVATAR:
//                    logger.debug("SUBMIT-REMOVE-AVATAR");
//                    form.getItem(0).setValue(req.getParameter(form.getItem(0).getName()));
//                    form.getItem(1).setValue(req.getParameter(form.getItem(1).getName()));
//                    form.getItem(2).setValue(req.getParameter(form.getItem(2).getName()));
//                    form.getItem(3).setValue(UrlManager.getContextUri(req, "/file/download/avatar?"));
//                    break;
//                case SAVE_PROFILE:
//                    logger.debug("SUBMIT-SAVE-PROFILE");
//                    if (FormValidator.validate(req, form)) {
//                        logger.debug("FORM VALID");
//                        try (UserService userService = new UserService()) {
//                            User user = (User) req.getSession().getAttribute("user");
//                            user.setFirstName(form.getItem(0).getValue());
//                            user.setMiddleName(form.getItem(1).getValue());
//                            user.setLastName(form.getItem(2).getValue());
//                            userService.updateUser(user, form.getItem(3).getValue());
//                            resp.sendRedirect(UrlManager.getContextUri(req, "/profile/view"));
//                            return;
//                        } catch (ServiceException e) {
//                            e.printStackTrace();
//                            throw new ActionException("exception.action.profile.edit.category", e.getCause());
//                        }
//                    }
//                    break;
//            }
//            req.setAttribute("editForm", form);
//            req.getRequestDispatcher("/WEB-INF/jsp/profile/edit.jsp").forward(req, resp);
//        } catch (FormException e) {
//            throw new ActionException("exception.action.profile.edit.form", e.getCause());
//        } catch (ServletException | IOException e) {
//            throw new ActionException("exception.action.profile.edit.jsp", e.getCause());
//        }
        } catch (ServletException | IOException e) {
            String[] detailArray = {super.getJspName()};
            throw new AppException(ACTION_FORWARD_TO_JSP_ERROR,
                    e.getMessage(), e.getCause(), detailArray);
        }
    }
}