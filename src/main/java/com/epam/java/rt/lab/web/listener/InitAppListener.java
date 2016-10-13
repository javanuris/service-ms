package com.epam.java.rt.lab.web.listener;

import com.epam.java.rt.lab.util.file.UploadException;
import com.epam.java.rt.lab.util.file.UploadManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebListener;

/**
 * service-ms
 */
@WebListener
public class InitAppListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            UploadManager.initContentTypeListMap();
        } catch (UploadException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
