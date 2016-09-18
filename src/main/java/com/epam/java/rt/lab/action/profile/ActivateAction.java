package com.epam.java.rt.lab.action.profile;

import com.epam.java.rt.lab.action.Action;
import com.epam.java.rt.lab.action.ActionException;
import com.epam.java.rt.lab.action.WebAction;
import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.service.LoginService;
import com.epam.java.rt.lab.util.UrlManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Service Management System
 */
@WebAction
public class ActivateAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ActivateAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try (LoginService loginService = new LoginService()) {
            if ("GET".equals(req.getMethod())) {
                logger.debug("GET");
                String activationEmail = (String) req.getSession().getAttribute("activationEmail");
                String activationCode = (String) req.getSession().getAttribute("activationCode");
                logger.debug("email: {}, code: {}", activationEmail, activationCode);
                req.getSession().removeAttribute("activationEmail");
                req.getSession().removeAttribute("activationCode");
                if (activationEmail != null && activationCode != null) {
                    Login login = loginService.confirmActivationCode(activationEmail, activationCode);
                    if (login != null) {
                        loginService.addLogin(login);
                        resp.sendRedirect(UrlManager.getContextUri(req, "/profile/login"));
                    } else {
                        req.getSession().setAttribute("message", "message.activation.error-confirm");
                        resp.sendRedirect(UrlManager.getContextUri(req, "/home"));
                    }
                }
            }
        } catch (ConnectionException | DaoException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.activate.login-service", e.getCause());
        } catch (IOException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.activate.redirect", e.getCause());
        }
    }

}