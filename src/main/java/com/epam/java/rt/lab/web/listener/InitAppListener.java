package com.epam.java.rt.lab.web.listener;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.PropertyManager;
import com.epam.java.rt.lab.util.UtilException;
import com.epam.java.rt.lab.util.file.UploadException;
import com.epam.java.rt.lab.util.file.UploadManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * service-ms
 */
@WebListener
public class InitAppListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            PropertyManager.initGlobalProperties();
            AppException.initExceptionBundle();
            UploadManager.initContentTypeListMap();
        } catch (UtilException | UploadException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
