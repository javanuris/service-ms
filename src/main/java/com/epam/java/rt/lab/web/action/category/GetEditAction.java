package com.epam.java.rt.lab.web.action.category;

import com.epam.java.rt.lab.entity.business.Category;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.service.CategoryService;
import com.epam.java.rt.lab.util.UrlManager;
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

import static com.epam.java.rt.lab.entity.business.Category.NULL_CATEGORY;
import static com.epam.java.rt.lab.util.PropertyManager.*;
import static com.epam.java.rt.lab.web.action.ActionExceptionCode.ACTION_FORWARD_TO_JSP_ERROR;
import static com.epam.java.rt.lab.web.validator.ValidatorFactory.DIGITS;

public class GetEditAction extends BaseAction implements Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
        try (CategoryService categoryService = new CategoryService()) {
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
            Category category = categoryService.getCategory(idValue);
            if (category == null || category == NULL_CATEGORY) {
                resp.sendRedirect(UrlManager.getUriWithContext(req,
                        CATEGORY_LIST_PATH, parameterMap));
                return;
            }
            Category parentCategory = null;
            if (category.getParentId() != null) {
                parentCategory = categoryService.
                        getCategory(category.getParentId());
            }
            Page page = new Page(parameterMap.get(PAGE),
                    parameterMap.get(ITEMS));
            FormControlValue nameValue =
                    new FormControlValue(category.getName());
            FormControlValue parentCategoryValue = new FormControlValue("");
            if (parentCategory != null) {
                parentCategoryValue.
                        setValue(String.valueOf(parentCategory.getId()));
            }
            List<SelectValue> valueList = new ArrayList<>();
            valueList.add(new SelectValue("", "-"));
            for (Category parent : categoryService.getCategoryList()) {
                valueList.add(new SelectValue(parent.getId().toString(),
                        parent.getName()));
            }
            parentCategoryValue.setAvailableValueList(valueList);
            req.setAttribute(PAGE, page);
            req.setAttribute(ID, category.getId());
            req.setAttribute(CATEGORY_PARENT, parentCategoryValue);
            req.setAttribute(CATEGORY_NAME, nameValue);
            req.getRequestDispatcher(super.getJspName()).forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new AppException(ACTION_FORWARD_TO_JSP_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

}