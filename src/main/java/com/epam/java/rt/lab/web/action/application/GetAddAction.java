package com.epam.java.rt.lab.web.action.application;

import com.epam.java.rt.lab.entity.business.Category;
import com.epam.java.rt.lab.service.CategoryService;
import com.epam.java.rt.lab.service.ServiceException;
import com.epam.java.rt.lab.util.UrlManager;
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
public class GetAddAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(GetAddAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try (CategoryService categoryService = new CategoryService()) {
            logger.debug("/WEB-INF/jsp/application/add.jsp");
            Map<String, String> parameterMap = UrlManager.getRequestParameterMap(req.getQueryString());
            Form form = FormFactory.getInstance().create("add-application");
            form.setActionParameterString(UrlManager.getRequestParameterString(parameterMap));
            List<FormControl.SelectValue> valueList = new ArrayList<>();
            valueList.add(new FormControl.SelectValue("", "-"));
            for (Category parent : categoryService.getCategoryList())
                valueList.add(new FormControl.SelectValue(parent.getId().toString(), parent.getName()));
            form.getItem(0).setAvailableValueList(valueList);
            form.getItem(3).setActionParameters("?".concat(UrlManager.getRequestParameterString(parameterMap)));
            req.setAttribute("addApplication", form);
            req.getRequestDispatcher("/WEB-INF/jsp/application/add.jsp").forward(req, resp);
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.application.add.service", e.getCause());
        } catch (FormException e) {
            throw new ActionException("exception.action.application.add.form", e.getCause());
        } catch (ServletException | IOException e) {
            throw new ActionException("exception.action.application.add.jsp", e.getCause());
        }
    }
}