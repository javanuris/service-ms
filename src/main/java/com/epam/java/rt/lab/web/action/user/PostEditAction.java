package com.epam.java.rt.lab.web.action.user;

import com.epam.java.rt.lab.entity.access.User;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.web.access.Role;
import com.epam.java.rt.lab.web.access.RoleFactory;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.ActionExceptionCode;
import com.epam.java.rt.lab.web.action.BaseAction;
import com.epam.java.rt.lab.web.component.FormControlValue;
import com.epam.java.rt.lab.web.component.SelectValue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;
import static com.epam.java.rt.lab.util.PropertyManager.*;
import static com.epam.java.rt.lab.web.action.ActionExceptionCode.ACTION_DENIED_ERROR;
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
        FormControlValue roleNameValue =
                new FormControlValue(req.getParameter(USER_ROLE_NAME));
        FormControlValue loginAttemptLeftValue =
                new FormControlValue(req.getParameter(USER_LOGIN_ATTEMPT_LEFT));
        FormControlValue loginStatusValue =
                new FormControlValue(req.getParameter(USER_LOGIN_STATUS));
        try (UserService userService = new UserService()) {
            Map<String, String> parameterMap =
                    UrlManager.getRequestParameterMap(req.getQueryString());
            String id = parameterMap.get(ID);
            if (id == null) throw new AppException(NULL_NOT_ALLOWED);
            User user = (User) req.getSession().getAttribute(USER_ATTR);
            if (user.getId().equals(Long.valueOf(id))) {
                throw new AppException(ACTION_DENIED_ERROR);
            }
            user = userService.getUser(Long.valueOf(id));
            if (req.getParameter(SUBMIT_SAVE_PROFILE) != null) {
                if (userService.updateUser(user,
                        firstNameValue, middleNameValue, lastNameValue,
                        avatarDownloadValue, roleNameValue,
                        loginAttemptLeftValue, loginStatusValue)) {
                    resp.sendRedirect(UrlManager.getUriWithContext(req,
                            USER_VIEW_PATH, parameterMap));
                    return;
                }
            } else if (req.getParameter(SUBMIT_REMOVE_AVATAR) != null) {
                avatarDownloadValue.setValue(FILE_DOWNLOAD_PATH
                        + SLASH + AVATAR_UPLOAD_TYPE + QUESTION);
            }
            List<SelectValue> valueList = new ArrayList<>();
            for (Map.Entry<String, Role> entry : RoleFactory.getInstance().
                    getRoleMap().entrySet()) {
                valueList.add(new SelectValue(entry.getValue().getName(),
                        entry.getValue().getName()));
            }
            roleNameValue.setAvailableValueList(valueList);
            valueList = new ArrayList<>();
            valueList.add(new SelectValue("0", "0"));
            valueList.add(new SelectValue("-1", "-1"));
            loginStatusValue.setAvailableValueList(valueList);
            req.setAttribute(USER_FIRST_NAME, firstNameValue);
            req.setAttribute(USER_MIDDLE_NAME, middleNameValue);
            req.setAttribute(USER_LAST_NAME, lastNameValue);
            req.setAttribute(USER_AVATAR_DOWNLOAD, avatarDownloadValue);
            req.setAttribute(USER_ROLE_NAME, roleNameValue);
            req.setAttribute(USER_LOGIN_ATTEMPT_LEFT, loginAttemptLeftValue);
            req.setAttribute(USER_LOGIN_STATUS, loginStatusValue);
            req.getRequestDispatcher(super.getJspName()).forward(req, resp);
        } catch (ServletException | IOException e) {
            String[] detailArray = {super.getJspName()};
            throw new AppException(ACTION_FORWARD_TO_JSP_ERROR,
                    e.getMessage(), e.getCause(), detailArray);
        }

    }
}