package com.epam.java.rt.lab.web.action.profile;

import com.epam.java.rt.lab.entity.access.User;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.ActionException;
import com.epam.java.rt.lab.web.component.form.Form;
import com.epam.java.rt.lab.web.component.form.FormException;
import com.epam.java.rt.lab.web.component.form.FormFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * category-ms
 */
public class GetEditAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(GetLoginAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try {
            User user = (User) req.getSession().getAttribute("user");
            Form form = FormFactory.getInstance().create("edit-profile");
            form.getItem(0).setValue(user.getFirstName());
            form.getItem(1).setValue(user.getMiddleName());
            form.getItem(2).setValue(user.getLastName());
            if (user.getAvatarId() == null) {
                form.getItem(3).setValue(UrlManager.getContextUri(req, "/file/download/avatar?"));
            } else {
                form.getItem(3).setValue(UrlManager.getContextRef(req, "/file/download/avatar", "id", user.getAvatarId()));
            }
            req.setAttribute("editForm", form);
            req.getRequestDispatcher("/WEB-INF/jsp/profile/edit.jsp").forward(req, resp);
        } catch (FormException e) {
            throw new ActionException("exception.action.profile.edit.form", e.getCause());
        } catch (ServletException | IOException e) {
            throw new ActionException("exception.action.profile.edit.jsp", e.getCause());
        }
    }
}