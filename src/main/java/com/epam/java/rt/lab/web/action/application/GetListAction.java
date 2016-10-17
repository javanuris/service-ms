package com.epam.java.rt.lab.web.action.application;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.web.action.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetListAction implements Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
//        try (ApplicationService applicationService = new ApplicationService()) {
//            Page page = new Page(req.getParameter("page"), req.getParameter("items"));
//            Table table = new Table();
//            User user = (User) req.getSession().getAttribute("user");
//            if (!"authorized".equals(user.getRole().getName())) user = null;
//            table.setEntityList(applicationService.getApplicationList(page, user));
//            table.setHrefPrefix(UrlManager.getContextUri(req, "/application/view",
//                    "page=".concat(String.valueOf(page.getCurrentPage())),
//                    "items=".concat(String.valueOf(page.getItemsOnPage())),
//                    "id="));
//            req.setAttribute("applicationList", table);
//            req.setAttribute("applicationListPage", page);
//            req.setAttribute("addApplicationRef", UrlManager.getContextUri(req, "/application/add",
//                    UrlManager.getRequestParameterString(req)));
//            List<Navigation> navigationList = (List<Navigation>) req.getSession().getAttribute("navigationList");
//            if (navigationList != null) {
//                for (Navigation navigation : navigationList)
//                    if ("/application/list".equals(navigation.getUri()))
//                        req.setAttribute("nav", navigation);
//            }
//            req.getRequestDispatcher("/WEB-INF/jsp/application/list.jsp").forward(req, resp);
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.application.list.category", e.getCause());
//        } catch (ServletException | IOException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.application.list.request", e.getCause());
//        }
    }

}
