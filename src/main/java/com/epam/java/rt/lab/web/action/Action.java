package com.epam.java.rt.lab.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Service Management System
 */
public interface Action {
    void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException;
}
