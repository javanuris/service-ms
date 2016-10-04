package com.epam.java.rt.lab.web.action.user;

import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.ServiceException;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.util.validator.ValidatorException;
import com.epam.java.rt.lab.util.validator.ValidatorFactory;
import com.epam.java.rt.lab.web.access.Role;
import com.epam.java.rt.lab.web.access.RoleException;
import com.epam.java.rt.lab.web.access.RoleFactory;
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
        try (UserService userService = new UserService()) {
            logger.debug("/WEB-INF/jsp/user/view.jsp");
            Map<String, String> parameterMap = UrlManager.getRequestParameterMap(req.getQueryString());
            String id = parameterMap.get("id");
            if (ValidatorFactory.create("digits").validate(id) != null) {
                parameterMap.remove("id");
                resp.sendRedirect(UrlManager.getContextUri(req, "/user/list", parameterMap));
            } else {
                User user = (User) req.getSession().getAttribute("user");
                if (user.getId() == Long.valueOf(id)) {
                    resp.sendRedirect(UrlManager.getContextUri(req, "/profile/edit", parameterMap));
                    return;
                }
                user = userService.getUser(Long.valueOf(id));
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
                for (Map.Entry<String, Role> entry : RoleFactory.getInstance().getRoleMap().entrySet())
                    valueList.add(new FormControl.SelectValue(entry.getValue().getName(), entry.getValue().getName()));
                form.getItem(4).setAvailableValueList(valueList);
                form.getItem(4).setValue(user.getRole().getName());
                form.getItem(5).setValue(String.valueOf(user.getLogin().getAttemptLeft()));
                valueList = new ArrayList<>();
                valueList.add(new FormControl.SelectValue("0", "0"));
                valueList.add(new FormControl.SelectValue("-1", "-1"));
                form.getItem(6).setAvailableValueList(valueList);
                form.getItem(6).setValue(String.valueOf(user.getLogin().getStatus()));
                form.getItem(8).setActionParameters("?".concat(UrlManager.getRequestParameterString(parameterMap)));
                req.setAttribute("editForm", form);
                req.getRequestDispatcher("/WEB-INF/jsp/user/edit.jsp").forward(req, resp);
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.user.edit.user-service.get-user", e.getCause());
        } catch (ValidatorException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.user.edit.validator.id", e.getCause());
        } catch (FormException e) {
            throw new ActionException("exception.action.access.user.edit.form", e.getCause());
        } catch (ServletException | IOException e) {
            throw new ActionException("exception.action.access.user.edit.jsp", e.getCause());
        } catch (RoleException e) {
            throw new ActionException("exception.action.access.user.edit.role-factory", e.getCause());
        }
    }
}