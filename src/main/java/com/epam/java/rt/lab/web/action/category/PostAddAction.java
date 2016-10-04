package com.epam.java.rt.lab.web.action.category;

import com.epam.java.rt.lab.entity.business.Category;
import com.epam.java.rt.lab.service.CategoryService;
import com.epam.java.rt.lab.service.ServiceException;
import com.epam.java.rt.lab.util.TimestampCompare;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.util.validator.FormValidator;
import com.epam.java.rt.lab.util.validator.ValidatorException;
import com.epam.java.rt.lab.util.validator.ValidatorFactory;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.ActionException;
import com.epam.java.rt.lab.web.action.profile.GetLoginAction;
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
        try (CategoryService categoryService = new CategoryService()) {
            logger.debug("/WEB-INF/jsp/category/edit.jsp");
            Map<String, String> parameterMap = UrlManager.getRequestParameterMap(req.getQueryString());
            Form form = FormFactory.getInstance().create("add-category");
            form.setActionParameterString(UrlManager.getRequestParameterString(parameterMap));
            List<FormControl.SelectValue> valueList = new ArrayList<>();
            valueList.add(new FormControl.SelectValue("", "-"));
            for (Category parent : categoryService.getCategoryList())
                valueList.add(new FormControl.SelectValue(parent.getId().toString(), parent.getName()));
            form.getItem(0).setAvailableValueList(valueList);
            if (FormValidator.validate(req, form)) {
                Category category = new Category();
                if (form.getItem(0).getValue().length() > 0) {
                    Category parent = new Category();
                    parent.setId(Long.valueOf(form.getItem(0).getValue()));
                    category.setParent(parent);
                }
                category.setName(form.getItem(1).getValue());
                category.setCreated(TimestampCompare.getCurrentTimestamp());
                parameterMap.put("id", String.valueOf(categoryService.addCategory(category)));
                resp.sendRedirect(UrlManager.getContextUri(req, "/category/view", parameterMap));
                return;
            }
            req.setAttribute("editCategory", form);
            req.getRequestDispatcher("/WEB-INF/jsp/category/edit.jsp").forward(req, resp);
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.category.edit.user-category.get-user", e.getCause());
        } catch (FormException e) {
            throw new ActionException("exception.action.category.user.edit.form", e.getCause());
        } catch (ServletException | IOException e) {
            throw new ActionException("exception.action.category.user.edit.jsp", e.getCause());
        }
    }
}