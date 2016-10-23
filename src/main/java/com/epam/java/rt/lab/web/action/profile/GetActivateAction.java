package com.epam.java.rt.lab.web.action.profile;

import com.epam.java.rt.lab.entity.access.Login;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.service.LoginService;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.BaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.epam.java.rt.lab.entity.access.Login.NULL_LOGIN;
import static com.epam.java.rt.lab.util.PropertyManager.*;
import static com.epam.java.rt.lab.web.action.ActionExceptionCode.ACTION_FORWARD_TO_JSP_ERROR;

public class GetActivateAction extends BaseAction implements Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
        try (LoginService loginService = new LoginService();
             UserService userService = new UserService()) {
            String activationEmail = req.getParameter(FORM_EMAIL);
            String activationCode = req.getParameter(FORM_CODE);
            req.getSession().removeAttribute(ACTIVATION_EMAIL_ATTR);
            req.getSession().removeAttribute(ACTIVATION_REF_ATTR);
            if (activationEmail != null && activationCode != null) {
                Login login = loginService.
                        getActivateLogin(activationEmail, activationCode);
                if (login != null && login != NULL_LOGIN) {
                    userService.addUser(login);
                    loginService.removeActivate(login.getEmail());
                    req.getSession().setAttribute(MESSAGE_ATTR,
                            "message.info.activate.success");
                    resp.sendRedirect(UrlManager.
                            getContextUri(req, HOME_PATH));
                    return;
                } else {
                    req.getSession().setAttribute(MESSAGE_ATTR,
                            "message.activation-error");
                }
            }
            resp.sendRedirect(UrlManager.getContextUri(req, HOME_PATH));
        } catch (IOException e) {
            throw new AppException(ACTION_FORWARD_TO_JSP_ERROR);
        }
    }

}