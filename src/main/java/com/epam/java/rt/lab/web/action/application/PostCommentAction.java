package com.epam.java.rt.lab.web.action.application;

import com.epam.java.rt.lab.entity.business.Application;
import com.epam.java.rt.lab.entity.business.Comment;
import com.epam.java.rt.lab.entity.rbac.User;
import com.epam.java.rt.lab.service.ApplicationService;
import com.epam.java.rt.lab.service.CommentService;
import com.epam.java.rt.lab.service.ServiceException;
import com.epam.java.rt.lab.util.TimestampCompare;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.util.validator.FormValidator;
import com.epam.java.rt.lab.util.validator.ValidatorException;
import com.epam.java.rt.lab.util.validator.ValidatorFactory;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.ActionException;
import com.epam.java.rt.lab.web.action.profile.GetLoginAction;
import com.epam.java.rt.lab.web.component.form.Form;
import com.epam.java.rt.lab.web.component.form.FormException;
import com.epam.java.rt.lab.web.component.form.FormFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * category-ms
 */
public class PostCommentAction implements Action {

    private static final Logger logger = LoggerFactory.getLogger(GetLoginAction.class);

    private enum Submit {
        SAVE_PROFILE,
        REMOVE_AVATAR
    }

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try (CommentService commentService = new CommentService();
             ApplicationService applicationService = new ApplicationService()) {
            logger.debug("/WEB-INF/jsp/category/edit.jsp");
            Map<String, String> parameterMap = UrlManager.getRequestParameterMap(req.getQueryString());
            String id = parameterMap.get("id");
            if (ValidatorFactory.create("digits").validate(id) != null) {
                parameterMap.remove("id");
                resp.sendRedirect(UrlManager.getContextUri(req, "/application/view", parameterMap));
            } else {
                User user = (User) req.getSession().getAttribute("user");
                Application application = applicationService.getApplication(Long.valueOf(id));
                if (application == null || (!application.getUser().getId().equals(user.getId()) &&
                        ("authorized".equals(user.getRole().getName())))) {
                    resp.sendRedirect(UrlManager.getContextUri(req, "/application/list", parameterMap));
                } else {
                    Form form = FormFactory.getInstance().create("comment-application");
                    form.setActionParameterString(UrlManager.getRequestParameterString(parameterMap));
                    if (FormValidator.validate(req, form)) {
                        logger.debug("FORM VALID");
                        Comment comment = new Comment();
                        comment.setCreated(TimestampCompare.getCurrentTimestamp());
                        comment.setUser(user);
                        comment.setApplicationId(Long.valueOf(id));
                        commentService.setCommentPhoto(comment, form.getItem(0).getValue());
                        comment.setMessage(form.getItem(1).getValue());
                        commentService.addComment(comment);
                        resp.sendRedirect(UrlManager.getContextUri(req, "/application/view", parameterMap));
                        return;
                    }
                    req.setAttribute("commentApplication", form);
                    req.getRequestDispatcher("/WEB-INF/jsp/application/comment.jsp").forward(req, resp);
                }
            }
        } catch (FormException e) {
            throw new ActionException("exception.action.application.comment.form", e.getCause());
        } catch (ServletException | IOException e) {
            throw new ActionException("exception.action.application.comment.jsp", e.getCause());
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.application.comment.service", e.getCause());
        } catch (ValidatorException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.application.comment.validator", e.getCause());
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
//            throw new ActionException("exception.action.edit.user-category", e.getCause());
//        } catch (ServletException | IOException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.edit.forward", e.getCause());
//        } catch (ComponentException e) {
//            e.printStackTrace();
//        }
    }
}