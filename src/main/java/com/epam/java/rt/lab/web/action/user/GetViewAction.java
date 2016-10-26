package com.epam.java.rt.lab.web.action.user;

import com.epam.java.rt.lab.entity.access.User;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.BaseAction;
import com.epam.java.rt.lab.web.component.Page;
import com.epam.java.rt.lab.web.validator.Validator;
import com.epam.java.rt.lab.web.validator.ValidatorFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.epam.java.rt.lab.entity.access.User.NULL_USER;
import static com.epam.java.rt.lab.util.PropertyManager.*;
import static com.epam.java.rt.lab.web.action.ActionExceptionCode.ACTION_FORWARD_TO_JSP_ERROR;
import static com.epam.java.rt.lab.web.validator.ValidatorFactory.DIGITS;

public class GetViewAction extends BaseAction implements Action {

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
                        USER_LIST_PATH, parameterMap));
                return;
            }
            Long idValue = Long.valueOf(id);
            User user = (User) req.getSession().getAttribute(USER_ATTR);
            if (user.getId().equals(idValue)) {
                resp.sendRedirect(UrlManager.getUriWithContext(req,
                        PROFILE_VIEW_PATH, parameterMap));
                return;
            }
            user = userService.getUser(idValue);
            if (user == null || user == NULL_USER) {
                resp.sendRedirect(UrlManager.getUriWithContext(req,
                        USER_LIST_PATH, parameterMap));
                return;
            }
            Page page = new Page(parameterMap.get(PAGE),
                    parameterMap.get(ITEMS));
            req.setAttribute(PAGE, page);
            req.setAttribute(ID, user.getId());
            req.setAttribute(USER_FIRST_NAME, user.getFirstName());
            req.setAttribute(USER_MIDDLE_NAME, user.getMiddleName());
            req.setAttribute(USER_LAST_NAME, user.getLastName());
            parameterMap = new HashMap<>();
            parameterMap.put(ID, String.valueOf(user.getAvatarId()));
            req.setAttribute(USER_AVATAR_DOWNLOAD,
                    UrlManager.getUriWithContext(req,
                            FILE_DOWNLOAD_PATH + FILE_AVATAR_PREFIX,
                            parameterMap));
            req.setAttribute(USER_LOGIN_EMAIL, user.getLogin().getEmail());
            req.setAttribute(USER_ROLE_NAME, user.getRole().getName());
            req.setAttribute(USER_LOGIN_ATTEMPT_LEFT,
                    user.getLogin().getAttemptLeft());
            req.setAttribute(USER_LOGIN_STATUS, user.getLogin().getStatus());
            req.getRequestDispatcher(super.getJspName()).forward(req, resp);
        } catch (ServletException| IOException e) {
            throw new AppException(ACTION_FORWARD_TO_JSP_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

}