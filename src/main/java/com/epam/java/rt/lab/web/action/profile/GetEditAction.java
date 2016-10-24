package com.epam.java.rt.lab.web.action.profile;

import com.epam.java.rt.lab.entity.access.User;
import com.epam.java.rt.lab.exception.AppException;
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

public class GetEditAction extends BaseAction implements Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
        User user = (User) req.getSession().getAttribute(USER_ATTR);
        FormControlValue firstNameValue =
                new FormControlValue(user.getFirstName());
        FormControlValue middleNameValue =
                new FormControlValue(user.getMiddleName());
        FormControlValue lastNameValue =
                new FormControlValue(user.getLastName());
        FormControlValue avatarDownloadValue =
                new FormControlValue(UrlManager.
                        getUriWithContext(req, FILE_DOWNLOAD_PATH
                                + FILE_AVATAR_PREFIX + QUESTION
                                + ((user.getAvatarId() == null) ? ""
                                : ID + EQUAL + user.getAvatarId())));
        req.setAttribute(USER_FIRST_NAME, firstNameValue);
        req.setAttribute(USER_MIDDLE_NAME, middleNameValue);
        req.setAttribute(USER_LAST_NAME, lastNameValue);
        req.setAttribute(USER_AVATAR_DOWNLOAD, avatarDownloadValue);
        try {
            req.getRequestDispatcher(super.getJspName()).forward(req, resp);
        } catch (ServletException | IOException e) {
            String[] detailArray = {super.getJspName()};
            throw new AppException(ACTION_FORWARD_TO_JSP_ERROR,
                    e.getMessage(), e.getCause(), detailArray);
        }
    }
}