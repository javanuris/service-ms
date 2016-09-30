package com.epam.java.rt.lab.web.action.rbac.user;

import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.ActionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Service Management System
 */
public class ListAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ListAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
//        try (UserService userService = new UserService()) {
//            req.setAttribute("navItemArray", NavigationComponent.getNavItemArray("nav.rbac"));
//            String page = req.getParameter("page");
//            String items = req.getParameter("items");
//            PageComponent pageComponent = new PageComponent(
//                    !ValidatorFactory.isOnlyDigits(page) ? 1L : Long.valueOf(page),
//                    !ValidatorFactory.isOnlyDigits(items) ? null : Long.valueOf(items));
//            ListComponent listComponent = new ListComponent();
//            listComponent.setEntityList(userService.getUserList(pageComponent));
//            listComponent.setHrefPrefix(UrlManager.getContextUri(req, "/rbac/user/view",
//                    "page=".concat(String.valueOf(pageComponent.getCurrentPage())),
//                    "items=".concat(String.valueOf(pageComponent.getItemsOnPage())),
//                    "id="));
//            req.setAttribute("userList", listComponent);
//            req.setAttribute("userListPage", pageComponent);
//            req.getRequestDispatcher("/WEB-INF/jsp/rbac/user/list.jsp").forward(req, resp);
//        } catch (ConnectionException | DaoException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.rbac.user.list.user-service", e.getCause());
//        } catch (ServletException | IOException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.rbac.user.list.forward", e.getCause());
//        }
    }

}
