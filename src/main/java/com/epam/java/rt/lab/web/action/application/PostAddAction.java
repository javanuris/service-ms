package com.epam.java.rt.lab.web.action.application;

import com.epam.java.rt.lab.entity.business.Application;
import com.epam.java.rt.lab.entity.business.Category;
import com.epam.java.rt.lab.entity.business.Comment;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.ApplicationService;
import com.epam.java.rt.lab.service.CategoryService;
import com.epam.java.rt.lab.service.CommentService;
import com.epam.java.rt.lab.service.ServiceException;
import com.epam.java.rt.lab.util.TimestampManager;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.util.validator.FormValidator;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.ActionException;
import com.epam.java.rt.lab.web.component.form.Form;
import com.epam.java.rt.lab.web.component.form.FormControl;
import com.epam.java.rt.lab.web.component.form.FormException;
import com.epam.java.rt.lab.web.component.form.FormFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * category-ms
 */
public class PostAddAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(PostAddAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try (ApplicationService applicationService = new ApplicationService();
             CategoryService categoryService = new CategoryService();
             CommentService commentService = new CommentService()) {
            logger.debug("/WEB-INF/jsp/application/add.jsp");
            Map<String, String> parameterMap = UrlManager.getRequestParameterMap(req.getQueryString());
            Form form = FormFactory.getInstance().create("add-application");
            form.setActionParameterString(UrlManager.getRequestParameterString(parameterMap));
            List<FormControl.SelectValue> valueList = new ArrayList<>();
            valueList.add(new FormControl.SelectValue("", "-"));
            for (Category category : categoryService.getCategoryList())
                valueList.add(new FormControl.SelectValue(category.getId().toString(), category.getName()));
            form.getItem(0).setAvailableValueList(valueList);
            form.getItem(5).setActionParameters("?".concat(UrlManager.getRequestParameterString(parameterMap)));
            if (FormValidator.validate(req, form)) {
                Application application = new Application();
                if (form.getItem(0).getValue().length() == 0) {
                    form.getItem(0).addValidationMessage("message.validation.category-error");
                } else {
                    User user = (User) req.getSession().getAttribute("user");
                    Category category = new Category();
                    category.setId(Long.valueOf(form.getItem(0).getValue()));
                    application.setCategory(category);
                    application.setCreated(TimestampManager.getCurrentTimestamp());
                    application.setUser((User) req.getSession().getAttribute("user"));
                    application.setMessage(form.getItem(1).getValue());
                    Long applicationId = applicationService.addApplication(application);
                    Comment comment = new Comment();
                    comment.setCreated(TimestampManager.getCurrentTimestamp());
                    comment.setUser(user);
                    comment.setApplicationId(Long.valueOf(applicationId));
                    commentService.setCommentPhoto(comment, form.getItem(2).getValue());
                    comment.setMessage(form.getItem(3).getValue());
                    commentService.addComment(comment);
                    parameterMap.put("id", String.valueOf(applicationId));
                    resp.sendRedirect(UrlManager.getContextUri(req, "/application/view", parameterMap));
                    return;
                }
            }
            req.setAttribute("addApplication", form);
            req.getRequestDispatcher("/WEB-INF/jsp/application/add.jsp").forward(req, resp);
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.application.service", e.getCause());
        } catch (FormException e) {
            throw new ActionException("exception.action.application.user.edit.form", e.getCause());
        } catch (ServletException | IOException e) {
            throw new ActionException("exception.action.application.user.edit.jsp", e.getCause());
        }
    }
}