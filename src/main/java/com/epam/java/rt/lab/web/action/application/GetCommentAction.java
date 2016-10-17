package com.epam.java.rt.lab.web.action.application;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.web.action.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetCommentAction implements Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
//        try (CategoryService categoryService = new CategoryService()) {
//            logger.debug("/WEB-INF/jsp/application/comment.jsp");
//            Map<String, String> parameterMap = UrlManager.getRequestParameterMap(req.getQueryString());
//            Form form = FormFactory.getInstance().create("comment-application");
//            form.setActionParameterString(UrlManager.getRequestParameterString(parameterMap));
//            form.getItem(0).setValue(UrlManager.getContextUri(req, "/file/download/photo?"));
//            form.getItem(3).setActionParameters("?".concat(UrlManager.getRequestParameterString(parameterMap)));
//            req.setAttribute("commentApplication", form);
//            req.getRequestDispatcher("/WEB-INF/jsp/application/comment.jsp").forward(req, resp);
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.application.comment.service", e.getCause());
//        } catch (FormException e) {
//            throw new ActionException("exception.action.application.comment.form", e.getCause());
//        } catch (ServletException | IOException e) {
//            throw new ActionException("exception.action.application.comment.jsp", e.getCause());
//        }
    }
}