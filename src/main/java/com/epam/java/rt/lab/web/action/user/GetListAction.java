package com.epam.java.rt.lab.web.action.user;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.BaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Service Management System
 */
public class GetListAction extends BaseAction implements Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
//        try (UserService userService = new UserService()) {
//            String pageIndex = req.getParameter("page");
//            String itemCount = req.getParameter("items");
//            Page page = new Page(
//                    ValidatorFactory.create("digits").validate(pageIndex) != null ?
//                            null : Long.valueOf(pageIndex),
//                    ValidatorFactory.create("digits").validate(itemCount) != null ?
//                            null : Long.valueOf(itemCount)
//            );
//            Table table = new Table();
//            table.setEntityList(userService.getUserList(page));
//            table.setHrefPrefix(UrlManager.getContextUri(req, "/user/view",
//                    "page=".concat(String.valueOf(page.getCurrentPage())),
//                    "items=".concat(String.valueOf(page.getItemsOnPage())),
//                    "id="));
//            req.setAttribute("userList", table);
//            req.setAttribute("userListPage", page);
//            List<Navigation> navigationList = (List<Navigation>) req.getSession().getAttribute("navigationList");
//            if (navigationList != null) {
//                for (Navigation navigation : navigationList)
//                    if ("/user/list".equals(navigation.getUri()))
//                        req.setAttribute("nav", navigation);
//            }
//            req.getRequestDispatcher("/WEB-INF/jsp/user/list.jsp").forward(req, resp);
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.access.user.list.category", e.getCause());
//        } catch (ValidatorException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.access.user.list.validator", e.getCause());
//        } catch (ServletException | IOException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.access.user.list.request", e.getCause());
//        }
    }

}
