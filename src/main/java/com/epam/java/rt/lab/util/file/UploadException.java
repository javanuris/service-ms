package com.epam.java.rt.lab.util.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * service-ms
 */
public class UploadException extends Exception {
    
    private static final String UNNAMED_UPLOAD_EXCEPTION = "exception.util.file.upload.unnamed";
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadException.class);

    public UploadException() {
        super();
        LOGGER.error(UNNAMED_UPLOAD_EXCEPTION);
    }

    public UploadException(String message) {
        super(message);
        LOGGER.error(message);
    }

    public UploadException(String message, Throwable cause) {
        super(message, cause);
        LOGGER.error(message, cause);
    }

    public UploadException(Throwable cause) {
        super(cause);
        LOGGER.error(UNNAMED_UPLOAD_EXCEPTION, cause);
    }

    protected UploadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        LOGGER.error(UNNAMED_UPLOAD_EXCEPTION, cause);
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
