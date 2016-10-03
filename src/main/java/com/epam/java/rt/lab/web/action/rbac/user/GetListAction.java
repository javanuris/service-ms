package com.epam.java.rt.lab.web.action.rbac.user;

import com.epam.java.rt.lab.service.ServiceException;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.util.validator.ValidatorException;
import com.epam.java.rt.lab.util.validator.ValidatorFactory;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.ActionException;
import com.epam.java.rt.lab.web.component.Table;
import com.epam.java.rt.lab.web.component.Page;
import com.epam.java.rt.lab.web.component.navigation.Navigation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Service Management System
 */
public class GetListAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(GetListAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try (UserService userService = new UserService()) {
            String pageIndex = req.getParameter("page");
            String itemCount = req.getParameter("items");
            Page page = new Page(
                    ValidatorFactory.create("digits").validate(pageIndex) != null ?
                            null : Long.valueOf(pageIndex),
                    ValidatorFactory.create("digits").validate(itemCount) != null ?
                            null : Long.valueOf(itemCount)
            );
            Table table = new Table();
            table.setEntityList(userService.getUserList(page));
            table.setHrefPrefix(UrlManager.getContextUri(req, "/rbac/user/view",
                    "page=".concat(String.valueOf(page.getCurrentPage())),
                    "items=".concat(String.valueOf(page.getItemsOnPage())),
                    "id="));
            req.setAttribute("userList", table);
            req.setAttribute("userListPage", page);
            List<Navigation> navigationList = (List<Navigation>) req.getSession().getAttribute("navigationList");
            if (navigationList != null) {
                for (Navigation navigation : navigationList)
                    if ("/rbac/user/list".equals(navigation.getUri()))
                        req.setAttribute("nav", navigation);
            }
            req.getRequestDispatcher("/WEB-INF/jsp/rbac/user/list.jsp").forward(req, resp);
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.rbac.user.list.service", e.getCause());
        } catch (ValidatorException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.rbac.user.list.validator", e.getCause());
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.rbac.user.list.request", e.getCause());
        }
    }

}
