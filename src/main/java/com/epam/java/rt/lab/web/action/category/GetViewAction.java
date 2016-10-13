package com.epam.java.rt.lab.web.action.category;

import com.epam.java.rt.lab.entity.business.Category;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.CategoryService;
import com.epam.java.rt.lab.service.ServiceException;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.util.validator.ValidatorException;
import com.epam.java.rt.lab.util.validator.ValidatorFactory;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.ActionException;
import com.epam.java.rt.lab.web.component.view.View;
import com.epam.java.rt.lab.web.component.view.ViewException;
import com.epam.java.rt.lab.web.component.view.ViewFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Service Management System
 */
public class GetViewAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(GetViewAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try (CategoryService categoryService = new CategoryService()) {
            logger.debug("/WEB-INF/jsp/category/view.jsp");
            Map<String, String> parameterMap = UrlManager.getRequestParameterMap(req.getQueryString());
            String id = parameterMap.remove("id");
            if (ValidatorFactory.create("digits").validate(id) != null) {
                resp.sendRedirect(UrlManager.getContextUri(req, "/category/list", parameterMap));
            } else {
                Category category = categoryService.getCategory(Long.valueOf(id));
                if (category == null) {
                    resp.sendRedirect(UrlManager.getContextUri(req, "/category/list", parameterMap));
                } else {
                    View view = ViewFactory.getInstance().create("view-category");
                    if (category.getParentId() != null)
                        view.getControl(0).setValue(categoryService.getCategory(category.getParentId()).getName());
                    view.getControl(1).setValue(category.getName());
                    view.getControl(2).setAction(UrlManager.getContextUri(req, "/category/edit",
                            UrlManager.getRequestParameterString(parameterMap), "id=".concat(id)));
                    view.getControl(3).setAction(UrlManager.getContextUri(req, "/category/list", parameterMap));
                    req.setAttribute("viewCategory", view);
                    req.getRequestDispatcher("/WEB-INF/jsp/category/view.jsp").forward(req, resp);
                }
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.category.view.user-category.valueOf-user", e.getCause());
        } catch (ValidatorException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.category.view.validator.id", e.getCause());
        } catch (ViewException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.category.view.view-factory.valueOf-instance", e.getCause());
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.category.view.request", e.getCause());
        }
    }

}
