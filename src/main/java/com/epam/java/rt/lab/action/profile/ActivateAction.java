package com.epam.java.rt.lab.action.profile;

import com.epam.java.rt.lab.action.Action;
import com.epam.java.rt.lab.action.ActionException;
import com.epam.java.rt.lab.action.WebAction;
import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.entity.rbac.Role;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.LoginService;
import com.epam.java.rt.lab.service.RoleService;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.UrlManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
//        try (LoginService loginService = new LoginService();
//             RoleService roleService = new RoleService();
//             UserService userService = new UserService();) {
//            if ("GET".equals(req.getMethod())) {
//                logger.debug("GET");
//                String activationEmail = req.getParameter("email.regex");
//                String activationCode = req.getParameter("code");
//                logger.debug("email: {}, code: {}", activationEmail, activationCode);
//                req.getSession().removeAttribute("activationEmail");
//                req.getSession().removeAttribute("activationCode");
//                req.getSession().removeAttribute("activationRef");
//                if (activationEmail != null && activationCode != null) {
//                    Login login = loginService.confirmActivationCode(activationEmail, activationCode);
//                    if (login != null) {
//                        loginService.addLogin(login);
//                        User user = new User();
//                        Role role = roleService.getRoleAuthorized();
//                        user.setRole(role);
//                        login = loginService.getLogin(login.getEmail());
//                        user.setLogin(login);
//                        userService.addUser(user);
//                        resp.sendRedirect(UrlManager.getContextUri(req, "/profile/login"));
//                        return;
//                    } else {
//                        req.getSession().setAttribute("message", "message.activation.error-confirm");
//                    }
//                }
//            }
//            resp.sendRedirect(UrlManager.getContextUri(req, "/home"));
//        } catch (ConnectionException | DaoException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.activate.login-service", e.getCause());
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.activate.redirect", e.getCause());
//        }
    }

}