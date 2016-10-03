package com.epam.java.rt.lab.web.action.rbac.role;

import com.epam.java.rt.lab.entity.rbac.Permission;
import com.epam.java.rt.lab.entity.rbac.Role;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.*;
import com.epam.java.rt.lab.util.StringArray;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.util.validator.FormValidator;
import com.epam.java.rt.lab.util.validator.ValidatorException;
import com.epam.java.rt.lab.util.validator.ValidatorFactory;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.ActionException;
import com.epam.java.rt.lab.web.action.profile.GetLoginAction;
import com.epam.java.rt.lab.web.component.form.Form;
import com.epam.java.rt.lab.web.component.form.FormControl;
import com.epam.java.rt.lab.web.component.form.FormException;
import com.epam.java.rt.lab.web.component.form.FormFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * service-ms
 */
public class PostEditAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(GetLoginAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try (RoleService roleService = new RoleService();
             PermissionService permissionService = new PermissionService()){
            logger.debug("/WEB-INF/jsp/rbac/role/view.jsp");
            Map<String, String> parameterMap = UrlManager.getRequestParameterMap(req.getQueryString());
            String id = parameterMap.get("id");
            if (ValidatorFactory.create("digits").validate(id) != null) {
                parameterMap.remove("id");
                resp.sendRedirect(UrlManager.getContextUri(req, "/rbac/role/list", parameterMap));
                return;
            } else {
                User user = (User) req.getSession().getAttribute("user");
                if (user.getRole().getId().equals(Long.valueOf(id))) {
                    resp.sendRedirect(UrlManager.getContextUri(req, "/rbac/role/list", parameterMap));
                    return;
                }
                Role role = roleService.getRole(Long.valueOf(id));
                Form form = FormFactory.getInstance().create("edit-role");
                List<FormControl.SelectValue> valueList = new ArrayList<>();
                for (Permission permission : permissionService.getPermissionList())
                    valueList.add(new FormControl.SelectValue(String.valueOf(permission.getId()), permission.getUri()));
                form.getItem(1).setAvailableValueList(valueList);
                if (FormValidator.validate(req, form)) {
                    role.setName(form.getItem(0).getValue());
                    role.getUriList().clear();
                    String[] uriArray = form.getItem(1).getGenericValue();
                    for (String uri : uriArray) {
                        role.getUriList().add(uri);
                        System.out.println(uri);
                    }
                    roleService.updateRole(role);
                    resp.sendRedirect(UrlManager.getContextUri(req, "/rbac/role/view", parameterMap));
                    return;
                }
                req.setAttribute("editRole", form);
                req.getRequestDispatcher("/WEB-INF/jsp/rbac/role/edit.jsp").forward(req, resp);
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.rbac.role.edit.user-service.get-user", e.getCause());
        } catch (ValidatorException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.rbac.role.edit.validator.id", e.getCause());
        } catch (FormException e) {
            throw new ActionException("exception.action.rbac.role.edit.form", e.getCause());
        } catch (ServletException | IOException e) {
            throw new ActionException("exception.action.rbac.role.edit.jsp", e.getCause());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}