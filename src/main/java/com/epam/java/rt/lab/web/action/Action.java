package com.epam.java.rt.lab.web.action;

import com.epam.java.rt.lab.exception.AppException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An object that executes request and forwards to fill response
 */
public interface Action {

    /**
     * Executes request and forwards to fill request
     *
     * @param req  request object which should be handled
     * @param resp response object that should receive response
     * @throws AppException
     * @see HttpServletRequest
     * @see HttpServletResponse
     */
    void execute(HttpServletRequest req, HttpServletResponse resp)
            throws AppException;

}
