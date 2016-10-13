package com.epam.java.rt.lab.web.action.user;

import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.ServiceException;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.util.validator.ValidatorException;
import com.epam.java.rt.lab.util.validator.ValidatorFactory;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.ActionException;
import com.epam.java.rt.lab.web.component.view.View;
import com.epam.java.rt.lab.web.component.view.ViewException;
import com.epam.java.rt.lab.web.component.view.ViewFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Service Management System
 */
public class GetViewAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(GetViewAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try (UserService userService = new UserService()) {
            logger.debug("/WEB-INF/jsp/user/view.jsp");
            Map<String, String> parameterMap = UrlManager.getRequestParameterMap(req.getQueryString());
            String id = parameterMap.remove("id");
            if (ValidatorFactory.create("digits").validate(id) != null) {
                resp.sendRedirect(UrlManager.getContextUri(req, "/user/list", parameterMap));
            } else {
                User user = (User) req.getSession().getAttribute("user");
                if (user.getId() == Long.valueOf(id)) {
                    resp.sendRedirect(UrlManager.getContextUri(req, "/profile/view", parameterMap));
                    return;
                }
                user = userService.getUser(Long.valueOf(id));
                if (user == null) {
                    resp.sendRedirect(UrlManager.getContextUri(req, "/user/list", parameterMap));
                } else {
                    View view = ViewFactory.getInstance().create("view-user-profile");
                    view.getControl(0).setValue(user.getFirstName());
                    view.getControl(1).setValue(user.getMiddleName());
                    view.getControl(2).setValue(user.getLastName());
                    view.getControl(3).setValue(
                            UrlManager.getContextRef(req, "/file/download/avatar", "id", user.getAvatarId())
                    );
                    view.getControl(4).setValue(user.getLogin().getEmail());
                    view.getControl(5).setValue(user.getRole().getName());
                    view.getControl(6).setValue(String.valueOf(user.getLogin().getAttemptLeft()));
                    view.getControl(7).setValue(String.valueOf(user.getLogin().getStatus()));
                    view.getControl(8).setAction(UrlManager.getContextUri(req, "/user/edit",
                            UrlManager.getRequestParameterString(parameterMap), "id=".concat(id)));
                    view.getControl(9).setAction(UrlManager.getContextUri(req, "/user/list", parameterMap));
                    req.setAttribute("viewProfile", view);
                    req.getRequestDispatcher("/WEB-INF/jsp/user/view.jsp").forward(req, resp);
                }
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.user.view.user-category.get-user", e.getCause());
        } catch (ValidatorException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.user.view.validator.id", e.getCause());
        } catch (ViewException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.user.view.view-factory.get-instance", e.getCause());
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.user.view.request", e.getCause());
        }
    }

}
