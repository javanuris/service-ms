package com.epam.java.rt.lab.web.action.application;

import com.epam.java.rt.lab.entity.access.User;
import com.epam.java.rt.lab.entity.business.Application;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.service.ApplicationService;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.BaseAction;
import com.epam.java.rt.lab.web.component.Page;
import com.epam.java.rt.lab.web.validator.Validator;
import com.epam.java.rt.lab.web.validator.ValidatorFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.epam.java.rt.lab.util.PropertyManager.*;
import static com.epam.java.rt.lab.web.action.ActionExceptionCode.ACTION_FORWARD_TO_JSP_ERROR;
import static com.epam.java.rt.lab.web.validator.ValidatorFactory.DIGITS;

public class GetListAction extends BaseAction implements Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException {
        try (ApplicationService applicationService =
                     new ApplicationService()) {
            String pageIndex = req.getParameter(PAGE);
            String itemCount = req.getParameter(ITEMS);
            Long pageIndexValue = null;
            Long itemCountValue = null;
            if (pageIndex != null && itemCount != null) {
                Validator validator =
                        ValidatorFactory.getInstance().create(DIGITS);
                pageIndexValue = (validator.validate(pageIndex).length > 0)
                        ? null
                        : Long.valueOf(pageIndex);
                itemCountValue = (validator.validate(itemCount).length > 0)
                        ? null
                        : Long.valueOf(itemCount);
            }
            Page page = new Page(pageIndexValue, itemCountValue);
            User user = (User) req.getSession().getAttribute(USER_ATTR);
            List<Application> applicationList =
                    applicationService.getApplicationList(page, user);
            req.setAttribute(APPLICATION_LIST, applicationList);
            req.setAttribute(PAGE, page);
            req.getRequestDispatcher(super.getJspName()).forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new AppException(ACTION_FORWARD_TO_JSP_ERROR,
                    e.getMessage(), e.getCause());
        }
    }

}
