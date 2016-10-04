package com.epam.java.rt.lab.web.action.profile;

import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.service.LoginService;
import com.epam.java.rt.lab.service.ServiceException;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.ActionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Service Management System
 */
public class GetActivateAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(GetActivateAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try (LoginService loginService = new LoginService();
             UserService userService = new UserService()) {
            if ("GET".equals(req.getMethod())) {
                logger.debug("GET");
                String activationEmail = req.getParameter("email");
                String activationCode = req.getParameter("code");
                req.getSession().removeAttribute("activationEmail");
                req.getSession().removeAttribute("activationRef");
                if (activationEmail != null && activationCode != null) {
                    Login login = loginService.getActivateLogin(activationEmail, activationCode);
                    if (login != null) {
                        userService.addUser(login);
                        loginService.removeActivate(login.getEmail());
                        resp.sendRedirect(UrlManager.getContextUri(req, "/profile/login"));
                        return;
                    } else {
                        req.getSession().setAttribute("message", "message.activation-error");
                    }
                }
            }
            resp.sendRedirect(UrlManager.getContextUri(req, "/home"));
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.activate.category", e.getCause());
        } catch (IOException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.activate.request", e.getCause());
        }
    }

}