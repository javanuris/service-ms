package com.epam.java.rt.lab.web.action.profile;

import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.Restore;
import com.epam.java.rt.lab.service.LoginService;
import com.epam.java.rt.lab.service.ServiceException;
import com.epam.java.rt.lab.util.CookieManager;
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
import java.util.List;

/**
 * service-ms
 */
public class GetRestorePasswordAction implements Action {

    private static final Logger logger = LoggerFactory.getLogger(GetRestorePasswordAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try (LoginService loginService = new LoginService()) {
            req.getSession().removeAttribute("restoreEmail");
            req.getSession().removeAttribute("restoreRef");
            String email = req.getParameter("email");
            String code = req.getParameter("code");
            if (email != null && code != null) {
                String cookieName = CookieManager.getUserAgentCookieName(req);
                if (cookieName != null) {
                    String cookieValue = CookieManager.getCookieValue(req, cookieName);
                    CookieManager.removeCookie(req, resp, cookieName, UrlManager.getContextUri(req, ""));
                    Login login = loginService.getRestoreLogin(email, code, cookieName, cookieValue);
                    if (login != null) {
                        req.getSession().setAttribute("login", login);
                        Form form = FormFactory.create("restore-profile");
                        form.setActionParameterString(UrlManager.getRequestParameterString(req));
                        req.setAttribute("restoreForm", form);
                        req.getRequestDispatcher("/WEB-INF/jsp/profile/restore-password.jsp").forward(req, resp);
                        return;
                    }
                }
            }
            resp.sendRedirect(UrlManager.getContextUri(req, "/home"));
        } catch (FormException e) {
            throw new ActionException("exception.action.profile.restore.form", e.getCause());
        } catch (ServletException | IOException e) {
            throw new ActionException("exception.action.profile.restore-password.jsp", e.getCause());
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.restore-password.service", e.getCause());
        }
    }
}