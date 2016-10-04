package com.epam.java.rt.lab.web.action.rbac.role;

import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.ActionException;
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
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
//        try (RoleService roleService = new RoleService()) {
//            String pageIndex = req.getParameter("page");
//            String itemCount = req.getParameter("items");
//            Page page = new Page(
//                    ValidatorFactory.create("digits").validate(pageIndex) != null ?
//                            null : Long.valueOf(pageIndex),
//                    ValidatorFactory.create("digits").validate(itemCount) != null ?
//                            null : Long.valueOf(itemCount)
//            );
//            Table table = new Table();
//            table.setEntityList(roleService.getRoleList(page));
//            table.setHrefPrefix(UrlManager.getContextUri(req, "/access/role/view",
//                    "page=".concat(String.valueOf(page.getCurrentPage())),
//                    "items=".concat(String.valueOf(page.getItemsOnPage())),
//                    "id="));
//            req.setAttribute("roleList", table);
//            req.setAttribute("roleListPage", page);
//            List<Navigation> navigationList = (List<Navigation>) req.getSession().getAttribute("navigationList");
//            if (navigationList != null) {
//                for (Navigation navigation : navigationList)
//                    if ("/access/user/list".equals(navigation.getUri()))
//                        req.setAttribute("nav", navigation);
//            }
//            req.getRequestDispatcher("/WEB-INF/jsp/access/role/list.jsp").forward(req, resp);
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.access.role.list.category", e.getCause());
//        } catch (ValidatorException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.access.role.list.validator", e.getCause());
//        } catch (ServletException | IOException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.access.role.list.request", e.getCause());
//        }
    }

}
