package com.epam.java.rt.lab.web.action.profile;

import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.ServiceException;
import com.epam.java.rt.lab.service.UserService;
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

/**
 * service-ms
 */
public class PostEditAction implements Action {

    private static final Logger logger = LoggerFactory.getLogger(GetLoginAction.class);

    private enum Submit {
        SAVE_PROFILE,
        REMOVE_AVATAR
    }

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try {
            Form form = FormFactory.getInstance().create("edit-profile");
            Submit submit = req.getParameter(form.getItem(4).getName()) != null ? Submit.SAVE_PROFILE :
                    req.getParameter(form.getItem(5).getName()) != null ? Submit.REMOVE_AVATAR : null;
            switch (submit) {
                case REMOVE_AVATAR:
                    logger.debug("SUBMIT-REMOVE-AVATAR");
                    form.getItem(0).setValue(req.getParameter(form.getItem(0).getName()));
                    form.getItem(1).setValue(req.getParameter(form.getItem(1).getName()));
                    form.getItem(2).setValue(req.getParameter(form.getItem(2).getName()));
                    form.getItem(3).setValue(UrlManager.getContextUri(req, "/file/download/avatar?"));
                    break;
                case SAVE_PROFILE:
                    logger.debug("SUBMIT-SAVE-PROFILE");
                    if (FormValidator.validate(req, form)) {
                        logger.debug("FORM VALID");
                        try (UserService userService = new UserService()) {
                            User user = (User) req.getSession().getAttribute("user");
                            user.setFirstName(form.getItem(0).getValue());
                            user.setMiddleName(form.getItem(1).getValue());
                            user.setLastName(form.getItem(2).getValue());
                            if (form.getItem(3).getValue().length() == 0) {
                                Long avatarId = user.getAvatarId();
                                user.setAvatarId(null);
                                userService.updateUser(user);
                                userService.removeAvatar(user.getAvatarId());
                            } else {
                                String[] pair = form.getItem(3).getValue().split("\\?");
                                if (pair.length == 2) {
                                    pair = pair[1].split("=");
                                    if (pair.length == 2)
                                        userService.setAvatar(user, pair[1]);
                                }
                                userService.updateUser(user);
                            }
                            resp.sendRedirect(UrlManager.getContextUri(req, "/profile/view"));
                            return;
                        } catch (ServiceException e) {
                            e.printStackTrace();
                            throw new ActionException("exception.action.profile.edit.service", e.getCause());
                        }
                    }
                    break;
            }
            req.setAttribute("editForm", form);
            req.getRequestDispatcher("/WEB-INF/jsp/profile/edit.jsp").forward(req, resp);
        } catch (FormException e) {
            throw new ActionException("exception.action.profile.edit.form", e.getCause());
        } catch (ServletException | IOException e) {
            throw new ActionException("exception.action.profile.edit.jsp", e.getCause());
        }


//            } else if ("POST".equals(req.getMethod())) {
//                logger.debug("POST");
//                if (ValidatorFactory.validate(req, form)) {
//                    logger.debug("FORM VALID");
//                    user.setFirstName(form.getItem(0).getValue());
//                    user.setMiddleName(form.getItem(1).getValue());
//                    user.setLastName(form.getItem(2).getValue());
//                    if (form.getItem(3).getValue().length() == 0) {
//                        userService.removeAvatar(user);
//                    } else {
//                        logger.debug("{} = {}", form.getItem(3).getLabel(), form.getItem(3).getValue());
//                        String[] pair = form.getItem(3).getValue().split("\\?");
//                        logger.debug("pair = {}", pair);
//                        if (pair.length == 2) {
//                            pair = pair[1].split("=");
//                            logger.debug("pair = {}", pair);
//                            if (pair.length == 2) userService.setAvatar(user, pair[1]);
//                        }
//                    }
//                    if (userService.updateUser(user) != 1) {
//                        logger.debug("UPDATE ERROR");
//                        String[] validationMessageArray = {"profile.edit.submit.error-edit"};
//                        form.getItem(4).setValidationMessageArray(validationMessageArray);
//                    } else {
//                        logger.debug("UPDATE SUCCESS");
//                        req.getSession().setAttribute("userName", user.getName());
//                        resp.sendRedirect(UrlManager.getContextUri(req, "/profile/view"));
//                        return;
//                    }
//                }
//            }
//            req.getRequestDispatcher("/WEB-INF/jsp/profile/edit.jsp").forward(req, resp);
//        } catch (ConnectionException | DaoException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.edit.user-service", e.getCause());
//        } catch (ServletException | IOException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.edit.forward", e.getCause());
//        } catch (ComponentException e) {
//            e.printStackTrace();
//        }
    }
}