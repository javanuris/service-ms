package com.epam.java.rt.lab.web.action.rbac.role;

import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.ActionException;
import com.epam.java.rt.lab.web.action.profile.GetLoginAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * service-ms
 */
public class GetEditAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(GetLoginAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
//        try (RoleService roleService = new RoleService();
//             PermissionService permissionService = new PermissionService()) {
//            logger.debug("/WEB-INF/jsp/access/role/view.jsp");
//            Map<String, String> parameterMap = UrlManager.getRequestParameterMap(req.getQueryString());
//            String id = parameterMap.get("id");
//            if (ValidatorFactory.create("digits").validate(id) != null) {
//                parameterMap.remove("id");
//                resp.sendRedirect(UrlManager.getContextUri(req, "/access/role/list", parameterMap));
//            } else {
//                User user = (User) req.getSession().getAttribute("user");
//                if (user.getRole().getId().equals(Long.valueOf(id))) {
//                    req.getSession().setAttribute("message", "message.role-owner");
//                    resp.sendRedirect(UrlManager.getContextUri(req, "/home", parameterMap));
//                    return;
//                }
//                Role role = roleService.getRole(Long.valueOf(id));
//                Form form = FormFactory.getInstance().create("edit-role");
//                form.setActionParameterString(UrlManager.getRequestParameterString(parameterMap));
//                form.getItem(0).setValue(role.getName());
//                List<FormControl.SelectValue> valueList = new ArrayList<>();
//                for (Permission permission : permissionService.getPermissionList())
//                    valueList.add(new FormControl.SelectValue(String.valueOf(permission.getId()), permission.getUri()));
//                form.getItem(1).setAvailableValueList(valueList);
//                form.getItem(1).setGenericValue(role.getUriList());
//                form.getItem(3).setActionParameters("?".concat(UrlManager.getRequestParameterString(parameterMap)));
//                req.setAttribute("editRole", form);
//                req.getRequestDispatcher("/WEB-INF/jsp/access/role/edit.jsp").forward(req, resp);
//            }
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.access.role.edit.user-service.get-user", e.getCause());
//        } catch (ValidatorException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.access.role.edit.validator.id", e.getCause());
//        } catch (FormException e) {
//            throw new ActionException("exception.action.access.role.edit.form", e.getCause());
//        } catch (ServletException | IOException e) {
//            throw new ActionException("exception.action.access.role.edit.jsp", e.getCause());
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.access.role.edit.uris", e.getCause());
//        }
    }
}