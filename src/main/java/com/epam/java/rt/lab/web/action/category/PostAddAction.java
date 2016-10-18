package com.epam.java.rt.lab.web.action.category;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.BaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * category-ms
 */
public class PostAddAction extends BaseAction implements Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
//        try (CategoryService categoryService = new CategoryService()) {
//            logger.debug("/WEB-INF/jsp/category/edit.jsp");
//            Map<String, String> parameterMap = UrlManager.getRequestParameterMap(req.getQueryString());
//            Form form = FormFactory.getInstance().create("add-category");
//            form.setActionParameterString(UrlManager.getRequestParameterString(parameterMap));
//            List<FormControl.SelectValue> valueList = new ArrayList<>();
//            valueList.add(new FormControl.SelectValue("", "-"));
//            for (Category parent : categoryService.getCategoryList())
//                valueList.add(new FormControl.SelectValue(parent.getId().toString(), parent.getName()));
//            form.getItem(0).setAvailableValueList(valueList);
//            form.getItem(3).setActionParameters("?".concat(UrlManager.getRequestParameterString(parameterMap)));
//            if (FormValidator.validate(req, form)) {
//                Category category = new Category();
//                if (form.getItem(0).getValue().length() > 0)
//                    category.setParentId(Long.valueOf(form.getItem(0).getValue()));
//                category.setName(form.getItem(1).getValue());
//                category.setCreated(TimestampManager.getCurrentTimestamp());
//                parameterMap.put("id", String.valueOf(categoryService.addCategory(category)));
//                resp.sendRedirect(UrlManager.getContextUri(req, "/category/view", parameterMap));
//                return;
//            }
//            req.setAttribute("editCategory", form);
//            req.getRequestDispatcher("/WEB-INF/jsp/category/edit.jsp").forward(req, resp);
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.category.edit.user-category.get-user", e.getCause());
//        } catch (FormException e) {
//            throw new ActionException("exception.action.category.user.edit.form", e.getCause());
//        } catch (ServletException | IOException e) {
//            throw new ActionException("exception.action.category.user.edit.jsp", e.getCause());
//        }
    }
}