package com.epam.java.rt.lab.web.action.application;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.web.action.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetViewAction implements Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
//        try (ApplicationService applicationService = new ApplicationService();
//             CommentService commentService = new CommentService()) {
//            logger.debug("/WEB-INF/jsp/application/view.jsp");
//            Map<String, String> parameterMap = UrlManager.getRequestParameterMap(req.getQueryString());
//            String id = parameterMap.remove("id");
//            if (ValidatorFactory.create("digits").validate(id) != null) {
//                resp.sendRedirect(UrlManager.getContextUri(req, "/application/list", parameterMap));
//            } else {
//                User user = (User) req.getSession().getAttribute("user");
//                Application application = applicationService.getApplication(Long.valueOf(id));
//                if (application == null || (!application.getUser().getId().equals(user.getId()) &&
//                        ("authorized".equals(user.getRole().getName())))) {
//                    resp.sendRedirect(UrlManager.getContextUri(req, "/application/list", parameterMap));
//                } else {
//                    View view = ViewFactory.getInstance().create("view-application");
//                    view.getControl(0).setValue(String.valueOf(application.getCreated()));
//                    view.getControl(1).setValue(application.getCategory().getName());
//                    view.getControl(2).setValue(application.getMessage());
//                    view.getControl(3).setValue(application.getUser().getName());
//                    view.getControl(4).setAction(UrlManager.getContextUri(req, "/application/list", parameterMap));
//                    req.setAttribute("viewApplication", view);
//                    req.setAttribute("commentRef", UrlManager.getContextUri(req, "/application/comment",
//                            UrlManager.getRequestParameterString(parameterMap), "id=".concat(id)));
//                    req.setAttribute("commentPhotoRef", UrlManager.getContextUri(req, "/file/download/photo?id="));
//                    req.setAttribute("userAvatarRef", UrlManager.getContextUri(req, "/file/download/avatar?id="));
//                    Page page = new Page("", "");
//                    req.setAttribute("commentList", commentService.getCommentList(page, Long.valueOf(id)));
//                    if (page.getCountItems() > 10) {
//                        parameterMap.put("rel", "application");
//                        req.setAttribute("allCommentRef", UrlManager.getContextUri(req, "/comment", parameterMap));
//                    }
//                    req.getRequestDispatcher("/WEB-INF/jsp/application/view.jsp").forward(req, resp);
//                }
//            }
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.category.view.user-category.get-user", e.getCause());
//        } catch (ValidatorException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.category.view.validator.id", e.getCause());
//        } catch (ViewException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.category.view.view-factory.get-instance", e.getCause());
//        } catch (ServletException | IOException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.category.view.request", e.getCause());
//        }
    }

}
