package com.epam.java.rt.lab.action;

import com.epam.java.rt.lab.dao.h2.QueryBuilder;
import com.epam.java.rt.lab.entity.rbac.Role;
import com.epam.java.rt.lab.service.RoleService;
import com.epam.java.rt.lab.service.ServiceException;
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
@WebAction
public class DevelopAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(DevelopAction.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException {
        try {
            RoleService roleService = new RoleService();
            List<Role> roleList = roleService.getRoleList(QueryBuilder.OrderType.ASC, RoleService.OrderBy.NAME);
            req.setAttribute("roleList", roleList);

            req.getRequestDispatcher("/WEB-INF/jsp/develop.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            throw new ActionException("exception.action.develop", e.getCause());
        } catch (ServiceException e) {
            throw new ActionException("exception", e.getCause());
        }
    }

}
