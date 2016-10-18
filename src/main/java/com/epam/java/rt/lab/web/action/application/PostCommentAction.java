package com.epam.java.rt.lab.web.action.application;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.BaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PostCommentAction extends BaseAction implements Action {

    private enum Submit {
        SAVE_PROFILE,
        REMOVE_AVATAR
    }

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
//        try (CommentService commentService = new CommentService();
//             ApplicationService applicationService = new ApplicationService()) {
//            logger.debug("/WEB-INF/jsp/category/edit.jsp");
//            Map<String, String> parameterMap = UrlManager.getRequestParameterMap(req.getQueryString());
//            String id = parameterMap.get("id");
//            if (ValidatorFactory.create("digits").validate(id) != null) {
//                parameterMap.remove("id");
//                resp.sendRedirect(UrlManager.getContextUri(req, "/application/view", parameterMap));
//            } else {
//                User user = (User) req.getSession().getAttribute("user");
//                Application application = applicationService.getApplication(Long.valueOf(id));
//                if (application == null || (!application.getUser().getId().equals(user.getId()) &&
//                        ("authorized".equals(user.getRole().getName())))) {
//                    resp.sendRedirect(UrlManager.getContextUri(req, "/application/list", parameterMap));
//                } else {
//                    Form form = FormFactory.getInstance().create("comment-application");
//                    form.setActionParameterString(UrlManager.getRequestParameterString(parameterMap));
//                    if (FormValidator.validate(req, form)) {
//                        logger.debug("FORM VALID");
//                        Comment comment = new Comment();
//                        comment.setCreated(TimestampManager.getCurrentTimestamp());
//                        comment.setUser(user);
//                        comment.setApplicationId(Long.valueOf(id));
//                        commentService.setCommentPhoto(comment, form.getItem(0).getValue());
//                        comment.setMessage(form.getItem(1).getValue());
//                        commentService.addComment(comment);
//                        resp.sendRedirect(UrlManager.getContextUri(req, "/application/view", parameterMap));
//                        return;
//                    }
//                    req.setAttribute("commentApplication", form);
//                    req.getRequestDispatcher("/WEB-INF/jsp/application/comment.jsp").forward(req, resp);
//                }
//            }
//        } catch (FormException e) {
//            throw new ActionException("exception.action.application.comment.form", e.getCause());
//        } catch (ServletException | IOException e) {
//            throw new ActionException("exception.action.application.comment.jsp", e.getCause());
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.application.comment.service", e.getCause());
//        } catch (ValidatorException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.application.comment.validator", e.getCause());
//        }
    }

}