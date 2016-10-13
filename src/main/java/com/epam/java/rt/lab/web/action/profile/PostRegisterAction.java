package com.epam.java.rt.lab.web.action.profile;

import com.epam.java.rt.lab.entity.rbac.Activate;
import com.epam.java.rt.lab.entity.rbac.Login;
import com.epam.java.rt.lab.service.LoginService;
import com.epam.java.rt.lab.service.ServiceException;
import com.epam.java.rt.lab.util.PropertyManager;
import com.epam.java.rt.lab.util.HashGenerator;
import com.epam.java.rt.lab.util.TimestampCompare;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.util.validator.FormValidator;
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
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Service Management System
 */
public class PostRegisterAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(PostRegisterAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try (LoginService loginService = new LoginService()) {
            Form form = FormFactory.getInstance().create("register-profile");
            if (FormValidator.validate(req, form)) {
                logger.debug("FORM VALID");
                if (!form.getItem(1).getValue().equals(form.getItem(2).getValue())) {
                    logger.debug("PASSWORD AND REPEAT NOT EQUAL");
                    form.getItem(2).addValidationMessage("message.profile.repeat-not-equal");
                } else {
                    Login login = loginService.getLogin(form.getItem(0).getValue());
                    if (login != null) {
                        logger.debug("EMAIL ALREADY EXISTS");
                        form.getItem(0).addValidationMessage("profile.login.email.error-exists-register");
                    } else {
                        logger.debug("REGISTER ACCEPTED");
                        Activate activate = new Activate();
                        activate.setEmail(form.getItem(0).getValue());
                        activate.setSalt(UUID.randomUUID().toString());
                        activate.setPassword(HashGenerator.hashPassword(activate.getSalt(), form.getItem(1).getValue()));
                        activate.setCode(UUID.randomUUID().toString());
                        activate.setValid(TimestampCompare.daysToTimestamp(
                                TimestampCompare.getCurrentTimestamp(),
                                Integer.valueOf(PropertyManager.getProperty("activation.days.valid"))));
                        if (loginService.addActivate(activate) == 0) {
                            logger.debug("STORING ACTIVATE CODE FAILED");
                            // TODO: need some reaction
                        } else {
                            logger.debug("ACTIVATE CODE STORED");
                            req.getSession().setAttribute("activationEmail", form.getItem(0).getValue());
                            req.getSession().setAttribute("activationRef",
                                    UrlManager.getContextRef(req, "/profile/activate", "email, code",
                                            form.getItem(0).getValue(), activate.getCode()));
                            logger.debug("REDIRECT TO {}", "/home");
                            resp.sendRedirect(UrlManager.getContextUri(req, "/home"));
                            return;
                        }
                    }
                }
            }
            req.setAttribute("registerForm", form);
            req.getRequestDispatcher("/WEB-INF/jsp/profile/register.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.profile.register.forward", e.getCause());
        } catch (FormException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.profile.register.form", e.getCause());
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ActionException();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.profile.register.hash", e.getCause());
        }
    }

}