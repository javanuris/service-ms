package com.epam.java.rt.lab.web.action.application;

import com.epam.java.rt.lab.entity.access.User;
import com.epam.java.rt.lab.entity.business.Application;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.service.ApplicationService;
import com.epam.java.rt.lab.service.CommentService;
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

public class PostCommentAction extends BaseAction implements Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
        try (CommentService commentService = new CommentService();
             ApplicationService applicationService
                     = new ApplicationService()) {
            Map<String, String> parameterMap =
                    UrlManager.getRequestParameterMap(req.getQueryString());
            String id = parameterMap.get(ID);
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
            Page page = new Page(parameterMap.get(PAGE),
                    parameterMap.get(ITEMS));
            req.setAttribute(PAGE, page);
            req.setAttribute(ID, idValue);
            FormControlValue photoDownloadValue =
                    new FormControlValue(req.getParameter(COMMENT_PHOTO));
            FormControlValue messageValue =
                    new FormControlValue(req.getParameter(COMMENT_MESSAGE));
            if (commentService.addComment(user, idValue,
                    photoDownloadValue, messageValue)) {
                resp.sendRedirect(UrlManager.getUriWithContext(req,
                        APPLICATION_VIEW_PATH, parameterMap));
                return;
            }
            req.setAttribute(COMMENT_PHOTO, photoDownloadValue);
            req.setAttribute(COMMENT_MESSAGE, messageValue);
            req.getRequestDispatcher(super.getJspName()).forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new AppException(ACTION_FORWARD_TO_JSP_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

}