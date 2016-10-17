package com.epam.java.rt.lab.web.action.category;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.web.action.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Service Management System
 */
public class GetListAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(GetListAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
//        try (CategoryService categoryService = new CategoryService()) {
//            Page page = new Page(req.getParameter("page"), req.getParameter("items"));
//            Table table = new Table();
//            table.setEntityList(categoryService.getCategoryList(page));
//            table.setHrefPrefix(UrlManager.getContextUri(req, "/category/view",
//                    "page=".concat(String.valueOf(page.getCurrentPage())),
//                    "items=".concat(String.valueOf(page.getItemsOnPage())),
//                    "id="));
//            req.setAttribute("categoryList", table);
//            req.setAttribute("categoryListPage", page);
//            req.setAttribute("addCategoryRef", UrlManager.getContextUri(req, "/category/add",
//                    UrlManager.getRequestParameterString(req)));
//            List<Navigation> navigationList = (List<Navigation>) req.getSession().getAttribute("navigationList");
//            if (navigationList != null) {
//                for (Navigation navigation : navigationList)
//                    if ("/category/list".equals(navigation.getUri()))
//                        req.setAttribute("nav", navigation);
//            }
//            req.getRequestDispatcher("/WEB-INF/jsp/category/list.jsp").forward(req, resp);
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.service.list.category", e.getCause());
//        } catch (ServletException | IOException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.service.list.request", e.getCause());
//        }
    }

}
