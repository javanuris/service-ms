package com.epam.java.rt.lab.web.action.category;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.BaseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Service Management System
 */
public class GetViewAction extends BaseAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(GetViewAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
//        try (CategoryService categoryService = new CategoryService()) {
//            logger.debug("/WEB-INF/jsp/category/view.jsp");
//            Map<String, String> parameterMap = UrlManager.getRequestParameterMap(req.getQueryString());
//            String id = parameterMap.remove("id");
//            if (ValidatorFactory.create("digits").validate(id) != null) {
//                resp.sendRedirect(UrlManager.getContextUri(req, "/category/list", parameterMap));
//            } else {
//                Category category = categoryService.getCategory(Long.valueOf(id));
//                if (category == null) {
//                    resp.sendRedirect(UrlManager.getContextUri(req, "/category/list", parameterMap));
//                } else {
//                    View view = ViewFactory.getInstance().create("view-category");
//                    if (category.getParentId() != null)
//                        view.getControl(0).setValue(categoryService.getCategory(category.getParentId()).getName());
//                    view.getControl(1).setValue(category.getName());
//                    view.getControl(2).setAction(UrlManager.getContextUri(req, "/category/edit",
//                            UrlManager.getRequestParameterString(parameterMap), "id=".concat(id)));
//                    view.getControl(3).setAction(UrlManager.getContextUri(req, "/category/list", parameterMap));
//                    req.setAttribute("viewCategory", view);
//                    req.getRequestDispatcher("/WEB-INF/jsp/category/view.jsp").forward(req, resp);
//                }
//            }
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.category.view.user-category.get-user", e.getCause());
//        } catch (ValidatorException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.category.view.validator.id", e.getCause());
//        } catch (ViewException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.category.view.view-factory.get-instance", e.getCause());
//        } catch (ServletException | IOException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.category.view.request", e.getCause());
//        }
    }

}
