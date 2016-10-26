package com.epam.java.rt.lab.web.action.category;

import com.epam.java.rt.lab.entity.business.Category;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.service.CategoryService;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.BaseAction;
import com.epam.java.rt.lab.web.component.FormControlValue;
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

import static com.epam.java.rt.lab.entity.business.Category.NULL_CATEGORY;
import static com.epam.java.rt.lab.util.PropertyManager.*;
import static com.epam.java.rt.lab.web.action.ActionExceptionCode.ACTION_FORWARD_TO_JSP_ERROR;
import static com.epam.java.rt.lab.web.validator.ValidatorFactory.DIGITS;

/**
 * category-ms
 */
public class PostEditAction extends BaseAction implements Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
        try (CategoryService categoryService = new CategoryService()) {
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
            Category category = categoryService.getCategory(idValue);
            if (category == null || category == NULL_CATEGORY) {
                resp.sendRedirect(UrlManager.getUriWithContext(req,
                        CATEGORY_LIST_PATH, parameterMap));
                return;
            }
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
            if (categoryService.updateCategory(idValue,
                    parentCategoryValue, nameValue)) {
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