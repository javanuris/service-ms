package com.epam.java.rt.lab.action;

import com.epam.java.rt.lab.dao.h2.QueryBuilder_;
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

    }

}
