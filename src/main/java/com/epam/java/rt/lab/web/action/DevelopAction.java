package com.epam.java.rt.lab.web.action;

import com.epam.java.rt.lab.dao.h2.QueryBuilder_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
