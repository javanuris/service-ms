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
public class GetViewAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(GetViewAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
//        try (RoleService roleService = new RoleService()) {
//            logger.debug("/WEB-INF/jsp/rbac/role/view.jsp");
//            Map<String, String> parameterMap = UrlManager.getRequestParameterMap(req.getQueryString());
//            String id = parameterMap.remove("id");
//            if (ValidatorFactory.create("digits").validate(id) != null) {
//                resp.sendRedirect(UrlManager.getContextUri(req, "/rbac/role/list", parameterMap));
//            } else {
//                User user = (User) req.getSession().getAttribute("user");
//                if (user.getRole().getId().equals(Long.valueOf(id))) {
//                    req.getSession().setAttribute("message", "message.role-owner");
//                    resp.sendRedirect(UrlManager.getContextUri(req, "/home", parameterMap));
//                    return;
//                }
//                Role role = roleService.getRole(Long.valueOf(id));
//                View view = ViewFactory.getInstance().create("view-role");
//                view.getControl(0).setValue(role.getName());
//                view.getControl(1).setValue(role.getUris());
//                view.getControl(2).setAction(UrlManager.getContextUri(req, "/rbac/role/edit",
//                        UrlManager.getRequestParameterString(parameterMap), "id=".concat(id)));
//                view.getControl(3).setAction(UrlManager.getContextUri(req, "/rbac/role/list", parameterMap));
//                req.setAttribute("viewRole", view);
//                req.getRequestDispatcher("/WEB-INF/jsp/rbac/role/view.jsp").forward(req, resp);
//            }
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.rbac.user.view.user-service.get-user", e.getCause());
//        } catch (ValidatorException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.rbac.user.view.validator.id", e.getCause());
//        } catch (ViewException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.view.view-factory.get-instance", e.getCause());
//        } catch (ServletException | IOException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.view.request", e.getCause());
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.view.uris", e.getCause());
//        }
    }

}
