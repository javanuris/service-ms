package com.epam.java.rt.lab.web.action.rbac.user;

import com.epam.java.rt.lab.entity.rbac.Role;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.LoginService;
import com.epam.java.rt.lab.service.RoleService;
import com.epam.java.rt.lab.service.ServiceException;
import com.epam.java.rt.lab.service.UserService;
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
        try (UserService userService = new UserService();
             RoleService roleService = new RoleService();
             LoginService loginService = new LoginService()){
            logger.debug("/WEB-INF/jsp/rbac/user/view.jsp");
            Map<String, String> parameterMap = UrlManager.getRequestParameterMap(req.getQueryString());
            String id = parameterMap.get("id");
            if (ValidatorFactory.create("digits").validate(id) != null) {
                parameterMap.remove("id");
                resp.sendRedirect(UrlManager.getContextUri(req, "/rbac/user/list", parameterMap));
                return;
            } else {
                User user = (User) req.getSession().getAttribute("user");
                if (user.getId().equals(Long.valueOf(id))) {
                    resp.sendRedirect(UrlManager.getContextUri(req, "/profile/edit", parameterMap));
                    return;
                }
                user = userService.getUser(Long.valueOf(id));
                Form form = FormFactory.getInstance().create("edit-user-profile");
                List<FormControl.SelectValue> valueList = new ArrayList<>();
                for (Role role : roleService.getRoleList())
                    valueList.add(new FormControl.SelectValue(role.getId().toString(), role.getName()));
                form.getItem(4).setAvailableValueList(valueList);
                valueList = new ArrayList<>();
                valueList.add(new FormControl.SelectValue("0", "0"));
                valueList.add(new FormControl.SelectValue("-1", "-1"));
                form.getItem(6).setAvailableValueList(valueList);
                if (FormValidator.validate(req, form)) {
                    user.setFirstName(form.getItem(0).getValue());
                    user.setMiddleName(form.getItem(1).getValue());
                    user.setLastName(form.getItem(2).getValue());
                    user.setRole(roleService.getRole(Long.valueOf(form.getItem(4).getValue())));
                    user.getLogin().setAttemptLeft(Integer.valueOf(form.getItem(5).getValue()));
                    user.getLogin().setStatus(Integer.valueOf(form.getItem(6).getValue()));
                    loginService.updateLogin(user.getLogin());
                    if (form.getItem(3).getValue().length() == 0) {
                        Long avatarId = user.getAvatarId();
                        user.setAvatarId(null);
                        userService.updateUser(user);
                        userService.removeAvatar(avatarId);
                    } else {
                        String[] pair = form.getItem(3).getValue().split("\\?");
                        if (pair.length == 2) {
                            pair = pair[1].split("=");
                            if (pair.length == 2)
                                if ("path".equals(pair[0]))
                                    userService.setAvatar(user, pair[1]);
                        }
                        userService.updateUser(user);
                    }
                    resp.sendRedirect(UrlManager.getContextUri(req, "/rbac/user/view", parameterMap));
                    return;
                }
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