package com.epam.java.rt.lab.web.action.profile;

import com.epam.java.rt.lab.entity.access.User;
import com.epam.java.rt.lab.util.UrlManager;
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

/**
 * Service Management System
 */
public class GetViewAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(GetViewAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try {
            logger.debug("/WEB-INF/jsp/profile/view.jsp");
            User user = (User) req.getSession().getAttribute("user");
            View view = ViewFactory.getInstance().create("view-profile");
            view.getControl(0).setValue(user.getFirstName());
            view.getControl(1).setValue(user.getMiddleName());
            view.getControl(2).setValue(user.getLastName());
            view.getControl(3).setValue(
                    UrlManager.getContextRef(req, "/file/download/avatar", "id", user.getAvatarId())
            );
            view.getControl(4).setValue(user.getLogin().getEmail());
            view.getControl(5).setAction(UrlManager.getContextUri(req, "/profile/reset-password"));
            view.getControl(6).setAction(UrlManager.getContextUri(req, "/profile/edit"));
            req.setAttribute("viewProfile", view);
            req.getRequestDispatcher("/WEB-INF/jsp/profile/view.jsp").forward(req, resp);
        } catch (ViewException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.view.view-factory.get-instance", e.getCause());
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.view.request", e.getCause());
        }
    }

}
