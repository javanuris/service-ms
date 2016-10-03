package com.epam.java.rt.lab.web.action.rbac.user;

import com.epam.java.rt.lab.entity.rbac.Role;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.RoleService;
import com.epam.java.rt.lab.service.ServiceException;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.UrlManager;
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
public class GetEditAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(GetLoginAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try (UserService userService = new UserService();
             RoleService roleService = new RoleService()) {
            logger.debug("/WEB-INF/jsp/rbac/user/view.jsp");
            Map<String, String> parameterMap = UrlManager.getRequestParameterMap(req.getQueryString());
            String id = parameterMap.get("id");
            if (ValidatorFactory.create("digits").validate(id) != null) {
                parameterMap.remove("id");
                resp.sendRedirect(UrlManager.getContextUri(req, "/rbac/user/list", parameterMap));
            } else {
                User user = userService.getUser(Long.valueOf(id));
                Form form = FormFactory.getInstance().create("edit-user-profile");
                form.setActionParameterString(UrlManager.getRequestParameterString(parameterMap));
                form.getItem(0).setValue(user.getFirstName());
                form.getItem(1).setValue(user.getMiddleName());
                form.getItem(2).setValue(user.getLastName());
                if (user.getAvatarId() == null) {
                    form.getItem(3).setValue(UrlManager.getContextUri(req, "/file/download/avatar?"));
                } else {
                    form.getItem(3).setValue(UrlManager.getContextRef(req, "/file/download/avatar", "id", user.getAvatarId()));
                }
                List<FormControl.SelectValue> valueList = new ArrayList<>();
                for (Role role : roleService.getRoleList())
                    valueList.add(new FormControl.SelectValue(role.getId().toString(), role.getName()));
                form.getItem(4).setAvailableValueList(valueList);
                form.getItem(4).setValue(user.getRole().getId().toString());
                form.getItem(6).setActionParameters("?".concat(UrlManager.getRequestParameterString(parameterMap)));
                req.setAttribute("editForm", form);
                req.getRequestDispatcher("/WEB-INF/jsp/rbac/user/edit.jsp").forward(req, resp);
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.rbac.user.edit.user-service.get-user", e.getCause());
        } catch (ValidatorException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.rbac.user.edit.validator.id", e.getCause());
        } catch (FormException e) {
            throw new ActionException("exception.action.rbac.user.edit.form", e.getCause());
        } catch (ServletException | IOException e) {
            throw new ActionException("exception.action.rbac.user.edit.jsp", e.getCause());
        }
    }
}