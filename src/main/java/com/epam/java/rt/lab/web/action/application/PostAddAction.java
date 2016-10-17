package com.epam.java.rt.lab.web.action.application;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.web.action.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PostAddAction implements Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
//        try (ApplicationService applicationService = new ApplicationService();
//             CategoryService categoryService = new CategoryService();
//             CommentService commentService = new CommentService()) {
//            logger.debug("/WEB-INF/jsp/application/add.jsp");
//            Map<String, String> parameterMap = UrlManager.getRequestParameterMap(req.getQueryString());
//            Form form = FormFactory.getInstance().create("add-application");
//            form.setActionParameterString(UrlManager.getRequestParameterString(parameterMap));
//            List<FormControl.SelectValue> valueList = new ArrayList<>();
//            valueList.add(new FormControl.SelectValue("", "-"));
//            for (Category category : categoryService.getCategoryList())
//                valueList.add(new FormControl.SelectValue(category.getId().toString(), category.getName()));
//            form.getItem(0).setAvailableValueList(valueList);
//            form.getItem(5).setActionParameters("?".concat(UrlManager.getRequestParameterString(parameterMap)));
//            if (FormValidator.validate(req, form)) {
//                Application application = new Application();
//                if (form.getItem(0).getValue().length() == 0) {
//                    form.getItem(0).addValidationMessage("message.validation.category-error");
//                } else {
//                    User user = (User) req.getSession().getAttribute("user");
//                    Category category = new Category();
//                    category.setId(Long.valueOf(form.getItem(0).getValue()));
//                    application.setCategory(category);
//                    application.setCreated(TimestampManager.getCurrentTimestamp());
//                    application.setUser((User) req.getSession().getAttribute("user"));
//                    application.setMessage(form.getItem(1).getValue());
//                    Long applicationId = applicationService.addApplication(application);
//                    Comment comment = new Comment();
//                    comment.setCreated(TimestampManager.getCurrentTimestamp());
//                    comment.setUser(user);
//                    comment.setApplicationId(Long.valueOf(applicationId));
//                    commentService.setCommentPhoto(comment, form.getItem(2).getValue());
//                    comment.setMessage(form.getItem(3).getValue());
//                    commentService.addComment(comment);
//                    parameterMap.put("id", String.valueOf(applicationId));
//                    resp.sendRedirect(UrlManager.getContextUri(req, "/application/view", parameterMap));
//                    return;
//                }
//            }
//            req.setAttribute("addApplication", form);
//            req.getRequestDispatcher("/WEB-INF/jsp/application/add.jsp").forward(req, resp);
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.application.service", e.getCause());
//        } catch (FormException e) {
//            throw new ActionException("exception.action.application.user.edit.form", e.getCause());
//        } catch (ServletException | IOException e) {
//            throw new ActionException("exception.action.application.user.edit.jsp", e.getCause());
//        }
    }
}