package com.epam.java.rt.lab.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.io.PrintWriter;

public class ServiceException extends Exception {

    private static final String UNNAMED_SERVICE_EXCEPTION = "exception.service.unnamed";
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceException.class);

    public ServiceException() {
        super();
        LOGGER.error(UNNAMED_SERVICE_EXCEPTION);
    }

    public ServiceException(String message) {
        super(message);
        LOGGER.error(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        LOGGER.error(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
        LOGGER.error(UNNAMED_SERVICE_EXCEPTION, cause);
    }

    protected ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        LOGGER.error(UNNAMED_SERVICE_EXCEPTION, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }

    @Override
    public synchronized Throwable getCause() {
        return super.getCause();
    }

    @Override
    public synchronized Throwable initCause(Throwable cause) {
        return super.initCause(cause);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }

    @Override
    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return super.fillInStackTrace();
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

    @Override
    public void setStackTrace(StackTraceElement[] stackTrace) {
        super.setStackTrace(stackTrace);
    }
}
