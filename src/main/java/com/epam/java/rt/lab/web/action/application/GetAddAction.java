package com.epam.java.rt.lab.web.action.application;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.web.action.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetAddAction implements Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
//        try (CategoryService categoryService = new CategoryService()) {
//            logger.debug("/WEB-INF/jsp/application/add.jsp");
//            Map<String, String> parameterMap = UrlManager.getRequestParameterMap(req.getQueryString());
//            Form form = FormFactory.getInstance().create("add-application");
//            form.setActionParameterString(UrlManager.getRequestParameterString(parameterMap));
//            List<FormControl.SelectValue> valueList = new ArrayList<>();
//            valueList.add(new FormControl.SelectValue("", "-"));
//            for (Category parent : categoryService.getCategoryList())
//                valueList.add(new FormControl.SelectValue(parent.getId().toString(), parent.getName()));
//            form.getItem(0).setAvailableValueList(valueList);
//            form.getItem(2).setValue(UrlManager.getContextUri(req, "/file/download/photo?"));
//            form.getItem(5).setActionParameters("?".concat(UrlManager.getRequestParameterString(parameterMap)));
//            req.setAttribute("addApplication", form);
//            req.getRequestDispatcher("/WEB-INF/jsp/application/add.jsp").forward(req, resp);
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            throw new ActionException("exception.action.application.add.service", e.getCause());
//        } catch (FormException e) {
//            throw new ActionException("exception.action.application.add.form", e.getCause());
//        } catch (ServletException | IOException e) {
//            throw new ActionException("exception.action.application.add.jsp", e.getCause());
//        }
    }
}