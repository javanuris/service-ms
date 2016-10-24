package com.epam.java.rt.lab.web.action.user;

import com.epam.java.rt.lab.entity.access.User;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.web.access.Role;
import com.epam.java.rt.lab.web.access.RoleFactory;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.BaseAction;
import com.epam.java.rt.lab.web.component.FormControlValue;
import com.epam.java.rt.lab.web.component.Page;
import com.epam.java.rt.lab.web.component.SelectValue;
import com.epam.java.rt.lab.web.validator.Validator;
import com.epam.java.rt.lab.web.validator.ValidatorFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.epam.java.rt.lab.entity.access.User.NULL_USER;
import static com.epam.java.rt.lab.util.PropertyManager.*;
import static com.epam.java.rt.lab.web.action.ActionExceptionCode.ACTION_FORWARD_TO_JSP_ERROR;
import static com.epam.java.rt.lab.web.validator.ValidatorFactory.DIGITS;

public class GetEditAction extends BaseAction implements Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
        try (UserService userService = new UserService()) {
            Map<String, String> parameterMap = UrlManager.
                    getRequestParameterMap(req.getQueryString());
            String id = parameterMap.remove(ID);
            Validator validator = ValidatorFactory.getInstance().create(DIGITS);
            if (validator.validate(id).length > 0) {
                resp.sendRedirect(UrlManager.getUriWithContext(req,
                        USER_VIEW_PATH, parameterMap));
                return;
            }
            Long idValue = Long.valueOf(id);
            User user = (User) req.getSession().getAttribute(USER_ATTR);
            if (user.getId().equals(idValue)) {
                resp.sendRedirect(UrlManager.getUriWithContext(req,
                        PROFILE_EDIT_PATH, parameterMap));
                return;
            }
            user = userService.getUser(idValue);
            if (user == null || user == NULL_USER) {
                resp.sendRedirect(UrlManager.getUriWithContext(req,
                        USER_LIST_PATH, parameterMap));
            }
            Page page = new Page(parameterMap.get(PAGE),
                    parameterMap.get(ITEMS));
            req.setAttribute(PAGE, page);
            req.setAttribute(ID, user.getId());
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
            FormControlValue roleNameValue =
                    new FormControlValue(user.getRole().getName());
            List<SelectValue> valueList = new ArrayList<>();
            for (Map.Entry<String, Role> entry : RoleFactory.getInstance().
                    getRoleMap().entrySet()) {
                valueList.add(new SelectValue(entry.getValue().getName(),
                        entry.getValue().getName()));
            }
            roleNameValue.setAvailableValueList(valueList);
            FormControlValue loginAttemptLeftValue =
                    new FormControlValue(String.
                            valueOf(user.getLogin().getAttemptLeft()));
            FormControlValue loginStatusValue =
                    new FormControlValue(String.
                            valueOf(user.getLogin().getStatus()));
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
            throw new AppException(ACTION_FORWARD_TO_JSP_ERROR,
                    e.getMessage(), e.getCause());
        }
    }
}