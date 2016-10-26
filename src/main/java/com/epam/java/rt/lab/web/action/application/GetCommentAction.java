package com.epam.java.rt.lab.web.action.application;

import com.epam.java.rt.lab.entity.access.User;
import com.epam.java.rt.lab.entity.business.Application;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.service.ApplicationService;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.BaseAction;
import com.epam.java.rt.lab.web.component.FormControlValue;
import com.epam.java.rt.lab.web.component.Page;
import com.epam.java.rt.lab.web.validator.Validator;
import com.epam.java.rt.lab.web.validator.ValidatorFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static com.epam.java.rt.lab.entity.business.Application.NULL_APPLICATION;
import static com.epam.java.rt.lab.util.PropertyManager.*;
import static com.epam.java.rt.lab.web.action.ActionExceptionCode.ACTION_FORWARD_TO_JSP_ERROR;
import static com.epam.java.rt.lab.web.validator.ValidatorFactory.DIGITS;

public class GetCommentAction extends BaseAction implements Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
        try (ApplicationService applicationService =
                     new ApplicationService()) {
            Map<String, String> parameterMap =
                    UrlManager.getRequestParameterMap(req.getQueryString());
            String id = parameterMap.remove(ID);
            Validator validator = ValidatorFactory.getInstance().create(DIGITS);
            if (validator.validate(id).length > 0) {
                resp.sendRedirect(UrlManager.getUriWithContext(req,
                        CATEGORY_LIST_PATH, parameterMap));
                return;
            }
            Long idValue = Long.valueOf(id);
            User user = (User) req.getSession().getAttribute(USER_ATTR);
            Application application =
                    applicationService.getApplication(idValue, user);
            if (application == null || application == NULL_APPLICATION) {
                resp.sendRedirect(UrlManager.getUriWithContext(req,
                        APPLICATION_LIST_PATH, parameterMap));
                return;
            }
            FormControlValue photoDownloadValue =
                    new FormControlValue(UrlManager.
                            getUriWithContext(req, FILE_DOWNLOAD_PATH
                                    + FILE_PHOTO_PREFIX + QUESTION));
            Page page = new Page(parameterMap.get(PAGE),
                    parameterMap.get(ITEMS));
            req.setAttribute(PAGE, page);
            req.setAttribute(ID, idValue);
            req.setAttribute(COMMENT_PHOTO, photoDownloadValue);
            req.getRequestDispatcher(super.getJspName()).forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new AppException(ACTION_FORWARD_TO_JSP_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

}