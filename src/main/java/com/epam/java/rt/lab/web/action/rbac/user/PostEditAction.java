package com.epam.java.rt.lab.web.action.rbac.user;

import com.epam.java.rt.lab.entity.rbac.Role;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.RoleService;
import com.epam.java.rt.lab.service.ServiceException;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.util.validator.FormValidator;
import com.epam.java.rt.lab.util.validator.ValidatorException;
import com.epam.java.rt.lab.util.validator.ValidatorFactory;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.ActionException;
import com.epam.java.rt.lab.web.action.profile.GetLoginAction;
import com.epam.java.rt.lab.web.component.form.Form;
import com.epam.java.rt.lab.web.component.form.FormControl;
import com.epam.java.rt.lab.web.component.form.FormException;
import com.epam.java.rt.lab.web.component.form.FormFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * service-ms
 */
public class PostEditAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(GetLoginAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try (UserService userService = new UserService();
             RoleService roleService = new RoleService()) {
            logger.debug("/WEB-INF/jsp/rbac/user/view.jsp");
            Map<String, String> parameterMap = UrlManager.getRequestParameterMap(req.getQueryString());
            String id = parameterMap.get("id");
            if (ValidatorFactory.create("digits").validate(id) != null) {
                parameterMap.remove("id");
                resp.sendRedirect(UrlManager.getContextUri(req, "/rbac/user/list", parameterMap));
                return;
            } else {
                User user = userService.getUser(Long.valueOf(id));
                Form form = FormFactory.getInstance().create("edit-user-profile");
                List<FormControl.SelectValue> valueList = new ArrayList<>();
                for (Role role : roleService.getRoleList())
                    valueList.add(new FormControl.SelectValue(role.getId().toString(), role.getName()));
                form.getItem(4).setAvailableValueList(valueList);
                if (FormValidator.validate(req, form)) {
                    user.setFirstName(form.getItem(0).getValue());
                    user.setMiddleName(form.getItem(1).getValue());
                    user.setLastName(form.getItem(2).getValue());
                    user.setRole(roleService.getRole(Long.valueOf(form.getItem(4).getValue())));
                    if (form.getItem(3).getValue().length() == 0) {
                        Long avatarId = user.getAvatarId();
                        user.setAvatarId(null);
                        userService.updateUser(user);
                        userService.removeAvatar(avatarId);
                    } else {
                        String[] pair = form.getItem(3).getValue().split("\\?");
                        if (pair.length == 2) {
                            pair = pair[1].split("=");
                            if (pair.length == 2)
                                if ("path".equals(pair[0]))
                                    userService.setAvatar(user, pair[1]);
                        }
                        userService.updateUser(user);
                    }
                    resp.sendRedirect(UrlManager.getContextUri(req, "/rbac/user/view", parameterMap));
                    return;
                }
                req.setAttribute("editForm", form);
                req.getRequestDispatcher("/WEB-INF/jsp/rbac/user/edit.jsp").forward(req, resp);
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.rbac.user.edit.user-service.get-user", e.getCause());
        } catch (ValidatorException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.rbac.user.edit.validator.id", e.getCause());
        } catch (FormException e) {
            throw new ActionException("exception.action.rbac.user.edit.form", e.getCause());
        } catch (ServletException | IOException e) {
            throw new ActionException("exception.action.rbac.user.edit.jsp", e.getCause());
        }
//        try (UserService userService = new UserService()) {
//            Map<String, String> parameterMap = UrlManager.getRequestParameterMap(req.getQueryString());
//            String id = parameterMap.remove("id");
//            if (id == null || !ValidatorFactory.isOnlyDigits(id)) {
//                resp.sendRedirect(UrlManager.getContextUri(req, "/rbac/user/list", parameterMap));
//                return;
//            }
//            User user = userService.getUser(Long.valueOf(id));
//            Form form = FormFactory.getInstance().create("user-profile-edit");
////            switch (Form.getStatus("userProfile.edit", UrlManager.getContextPathInfo(req), 100)) {
////                case 1:
////                    form = Form.create("profile.edit");
////                    break;
////                case 0:
////                    form = Form.set("profile.edit",
////                            new Form.Item("profile.edit.first-name.label", "input", "profile.edit.first-name.label",
////                                    ValidatorFactory.Validator.getRegex(ValidatorFactory.getRegex("name.regex"), "validation.name")),
////                            new Form.Item("profile.edit.middle-name.label", "input", "profile.edit.middle-name.label",
////                                    ValidatorFactory.Validator.getRegex(ValidatorFactory.getRegex("name.regex"), "validation.name")),
////                            new Form.Item("profile.edit.last-name.label", "input", "profile.edit.last-name.label",
////                                    ValidatorFactory.Validator.getRegex(ValidatorFactory.getRegex("name.regex"), "validation.name")),
////                            new Form.Item("profile.edit.avatar.label", "image", UrlManager.getContextUri(req, "/file/upload/avatar"), null),
////                            new Form.Item("profile.edit.remove-avatar.label", "button",
////                                    UrlManager.getContextUri(req, "/profile/edit", "sub-action=remove-avatar"), null),
////                            new Form.Item("profile.edit.submit.label", "submit",
////                                    "", null),
////                            new Form.Item("profile.edit.view-profile.label", "button",
////                                    UrlManager.getContextUri(req, "/profile/view"), null)
////                    );
////                    break;
////                case -1:
////                    throw new ActionException("exception.action.login.form-status");
////            }
//            req.setAttribute("editForm", form);
//            if ("GET".equals(req.getMethod())) {
//                logger.debug("GET");
//                form.getItem(0).setValue(user.getFirstName());
//                form.getItem(1).setValue(user.getMiddleName());
//                form.getItem(2).setValue(user.getLastName());
//                if (user.getAvatarId() == null) {
//                    form.getItem(3).setValue(UrlManager.getContextUri(req, "/file/download/avatar?"));
//                } else {
//                    form.getItem(3).setValue(UrlManager.getContextRef(req, "/file/download/avatar", "id", user.getAvatarId()));
//                }
//                String subAction = req.getParameter("sub-action");
//                if (subAction != null) {
//                    switch (subAction) {
//                        case "remove-avatar":
//                            logger.debug("REMOVE-AVATAR");
//                            userService.removeAvatar(user);
//                            if (userService.updateUser(user) != 1) {
//                                logger.debug("REMOVE ERROR");
//                                String[] validationMessageArray = {"profile.edit.avatar.error-remove"};
//                                form.getItem(3).setValidationMessageArray(validationMessageArray);
//                            } else {
//                                logger.debug("REMOVE SUCCESS");
//                                resp.sendRedirect(UrlManager.getContextUri(req, "/profile/edit"));
//                                return;
//                            }
//                    }
//                }
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