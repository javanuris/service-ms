package com.epam.java.rt.lab.web.action.profile;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.BaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Service Management System
 */
public class GetActivateAction extends BaseAction implements Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
//        try (LoginService loginService = new LoginService();
//             UserService userService = new UserService()) {
//            if ("GET".equals(req.getMethod())) {
//                logger.debug("GET");
//                String activationEmail = req.getParameter("email");
//                String activationCode = req.getParameter("code");
//                req.getSession().removeAttribute("activationEmail");
//                req.getSession().removeAttribute("activationRef");
//                if (activationEmail != null && activationCode != null) {
//                    Login login = loginService.getActivateLogin(activationEmail, activationCode);
//                    if (login != null) {
//                        userService.addUser(login);
//                        loginService.removeActivate(login.getEmail());
//                        resp.sendRedirect(UrlManager.getContextUri(req, "/profile/login"));
//                        return;
//                    } else {
//                        req.getSession().setAttribute("message", "message.activation-error");
//                    }
//                }
//            }
//            resp.sendRedirect(UrlManager.getContextUri(req, "/home"));
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.activate.category", e.getCause());
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.activate.request", e.getCause());
//        }
    }

}