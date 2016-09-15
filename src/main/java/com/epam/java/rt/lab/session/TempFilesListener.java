package com.epam.java.rt.lab.session;

import com.epam.java.rt.lab.util.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.File;
import java.io.IOException;

/**
 * service-ms
 */
public class TempFilesListener implements HttpSessionListener {
    private static final Logger logger = LoggerFactory.getLogger(TempFilesListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        logger.debug("SessionDestroyed: {}", httpSessionEvent.getSession().getId());
        try {
            String extension = ".".concat(httpSessionEvent.getSession().getId());
            File temporaryFile = File.createTempFile("", extension);
            String folderPath = temporaryFile.getParentFile().getParent();
            FileManager.deleteFilesWithExtension(folderPath, extension);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
