package com.epam.java.rt.lab.action.rbac.user;

import com.epam.java.rt.lab.action.Action;
import com.epam.java.rt.lab.action.ActionException;
import com.epam.java.rt.lab.action.WebAction;
import com.epam.java.rt.lab.component.ListComponent;
import com.epam.java.rt.lab.component.NavigationComponent;
import com.epam.java.rt.lab.component.PageComponent;
import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.FormManager;
import com.epam.java.rt.lab.util.UrlManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Service Management System
 */
@WebAction
public class ListAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ListAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        UserService userService = null;
        try {
            logger.debug("/WEB-INF/jsp/rbac/user/list.jsp");
            req.setAttribute("navItemArray", NavigationComponent.getNavItemArray("nav.rbac"));
            ListComponent listComponent = new ListComponent();
            userService = new UserService();
            String page = req.getParameter("page");
            Long pageIndex = 1L;
            if (FormManager.isOnlyDigits(page)) pageIndex = Long.valueOf(page);
            PageComponent pageComponent = new PageComponent(pageIndex, ListComponent.MAX_LIST_ITEM_ON_PAGE);
            for (User user : userService.getUserList(pageComponent)) {
                Map<String, String> valueMap = new HashMap<>();
                valueMap.put("id", user.getId().toString());
                valueMap.put("firstName", user.getFirstName());
                valueMap.put("middleName", user.getMiddleName());
                valueMap.put("lastName", user.getLastName());
                valueMap.put("name", user.getName());
                valueMap.put("role", user.getRole().getName());
                valueMap.put("href", UrlManager.getContextUri(req,
                        "/rbac/user/view?id=".concat(String.valueOf(user.getId()))));
                if (user.getLogin() != null) valueMap.put("login", user.getLogin().getEmail());
                listComponent.addValueMap(valueMap);
            }
            req.setAttribute("userList", listComponent);
            req.setAttribute("userListPage", pageComponent);
            req.getRequestDispatcher("/WEB-INF/jsp/rbac/user/list.jsp").forward(req, resp);
        } catch (ServletException | IOException | ConnectionException | DaoException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.rbac.user.list", e.getCause());
        } finally {
            try {
                if (userService != null) userService.close();
            } catch (DaoException e) {
                e.printStackTrace();
            }
        }
    }

}
