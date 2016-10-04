package com.epam.java.rt.lab.web.action.application;

import com.epam.java.rt.lab.entity.business.Application;
import com.epam.java.rt.lab.entity.business.Category;
import com.epam.java.rt.lab.service.ApplicationService;
import com.epam.java.rt.lab.service.CategoryService;
import com.epam.java.rt.lab.service.ServiceException;
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
        try (ApplicationService applicationService = new ApplicationService()) {
            logger.debug("/WEB-INF/jsp/application/view.jsp");
            Map<String, String> parameterMap = UrlManager.getRequestParameterMap(req.getQueryString());
            String id = parameterMap.remove("id");
            if (ValidatorFactory.create("digits").validate(id) != null) {
                resp.sendRedirect(UrlManager.getContextUri(req, "/application/list", parameterMap));
            } else {
                Application application = applicationService.getApplication(Long.valueOf(id));
                View view = ViewFactory.getInstance().create("view-application");
                view.getControl(0).setValue(String.valueOf(application.getCreated()));
                view.getControl(1).setValue(application.getCategory().getName());
                view.getControl(2).setValue(application.getMessage());
                view.getControl(3).setValue(application.getUser().getName());
                view.getControl(4).setAction(UrlManager.getContextUri(req, "/application/comment",
                        UrlManager.getRequestParameterString(parameterMap), "id=".concat(id)));
                view.getControl(5).setAction(UrlManager.getContextUri(req, "/application/list", parameterMap));
                req.setAttribute("viewApplication", view);

                //comments here

                req.getRequestDispatcher("/WEB-INF/jsp/application/view.jsp").forward(req, resp);
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.category.view.user-category.get-user", e.getCause());
        } catch (ValidatorException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.category.view.validator.id", e.getCause());
        } catch (ViewException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.category.view.view-factory.get-instance", e.getCause());
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.category.view.request", e.getCause());
        }
    }

}
