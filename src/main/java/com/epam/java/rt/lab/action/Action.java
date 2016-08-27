package com.epam.java.rt.lab.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * semasy
 */
public interface Action {
    void execute(HttpServletRequest req, HttpServletResponse resp) throws ActionException;
}
