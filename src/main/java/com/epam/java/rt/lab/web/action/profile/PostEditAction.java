package com.epam.java.rt.lab.web.action.profile;

import com.epam.java.rt.lab.exception.AppException;
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
            if (req.getParameter(SUBMIT_SAVE_PROFILE) != null) {
                if (userService.updateUser(req.getSession(),
                        firstNameValue, middleNameValue, lastNameValue,
                        avatarDownloadValue)) {
                    resp.sendRedirect(UrlManager.
                            getUriWithContext(req, PROFILE_VIEW_PATH));
                    return;
                }
            } else if (req.getParameter(SUBMIT_REMOVE_AVATAR) != null) {
                avatarDownloadValue.setValue(FILE_DOWNLOAD_PATH
                        + SLASH + AVATAR_UPLOAD_TYPE + QUESTION);
            }
            req.setAttribute(USER_FIRST_NAME, firstNameValue);
            req.setAttribute(USER_MIDDLE_NAME, middleNameValue);
            req.setAttribute(USER_LAST_NAME, lastNameValue);
            req.setAttribute(USER_AVATAR_DOWNLOAD, avatarDownloadValue);
            req.getRequestDispatcher(super.getJspName()).forward(req, resp);
        } catch (ServletException | IOException e) {
            String[] detailArray = {super.getJspName()};
            throw new AppException(ACTION_FORWARD_TO_JSP_ERROR,
                    e.getMessage(), e.getCause(), detailArray);
        }
    }
}