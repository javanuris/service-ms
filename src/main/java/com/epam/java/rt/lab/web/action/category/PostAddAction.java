package com.epam.java.rt.lab.web.action.category;

import com.epam.java.rt.lab.entity.business.Category;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.service.CategoryService;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.web.action.Action;
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

import static com.epam.java.rt.lab.util.PropertyManager.*;
import static com.epam.java.rt.lab.web.action.ActionExceptionCode.ACTION_FORWARD_TO_JSP_ERROR;

public class PostAddAction extends BaseAction implements Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
        try (CategoryService categoryService = new CategoryService()) {
            Map<String, String> parameterMap =
                    UrlManager.getRequestParameterMap(req.getQueryString());
            FormControlValue parentCategoryValue =
                    new FormControlValue(req.getParameter(CATEGORY_PARENT));
            List<SelectValue> valueList = new ArrayList<>();
            valueList.add(new SelectValue("", "-"));
            for (Category parent : categoryService.getCategoryList()) {
                valueList.add(new SelectValue(parent.getId().toString(),
                        parent.getName()));
            }
            parentCategoryValue.setAvailableValueList(valueList);
            FormControlValue nameValue =
                    new FormControlValue(req.getParameter(CATEGORY_NAME));
            Long id = categoryService.
                    addCategory(parentCategoryValue, nameValue);
            if (id != null) {
                parameterMap.put("id", String.valueOf(id));
                resp.sendRedirect(UrlManager.getUriWithContext(req,
                        CATEGORY_VIEW_PATH, parameterMap));
                return;
            }
            req.setAttribute(CATEGORY_PARENT, parentCategoryValue);
            req.setAttribute(CATEGORY_NAME, nameValue);
            req.getRequestDispatcher(super.getJspName()).forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new AppException(ACTION_FORWARD_TO_JSP_ERROR,
                    e.getMessage(), e.getCause());
       }
    }
}