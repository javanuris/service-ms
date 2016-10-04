package com.epam.java.rt.lab.web.action.application;

import com.epam.java.rt.lab.service.ApplicationService;
import com.epam.java.rt.lab.service.ServiceException;
import com.epam.java.rt.lab.util.UrlManager;
import com.epam.java.rt.lab.web.action.Action;
import com.epam.java.rt.lab.web.action.ActionException;
import com.epam.java.rt.lab.web.component.Page;
import com.epam.java.rt.lab.web.component.Table;
import com.epam.java.rt.lab.web.component.navigation.Navigation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Service Management System
 */
public class GetListAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(GetListAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try (ApplicationService applicationService = new ApplicationService()) {
            Page page = new Page(req);
            Table table = new Table();
            table.setEntityList(applicationService.getApplicationList(page));
            table.setHrefPrefix(UrlManager.getContextUri(req, "/application/view",
                    "page=".concat(String.valueOf(page.getCurrentPage())),
                    "items=".concat(String.valueOf(page.getItemsOnPage())),
                    "id="));
            req.setAttribute("applicationList", table);
            req.setAttribute("applicationListPage", page);
            req.setAttribute("addApplicationRef", UrlManager.getContextUri(req, "/application/add",
                    UrlManager.getRequestParameterString(req)));
            List<Navigation> navigationList = (List<Navigation>) req.getSession().getAttribute("navigationList");
            if (navigationList != null) {
                for (Navigation navigation : navigationList)
                    if ("/application/list".equals(navigation.getUri()))
                        req.setAttribute("nav", navigation);
            }
            req.getRequestDispatcher("/WEB-INF/jsp/application/list.jsp").forward(req, resp);
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.application.list.category", e.getCause());
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.application.list.request", e.getCause());
        }
    }

}
