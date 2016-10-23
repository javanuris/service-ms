package com.epam.java.rt.lab.web.listener;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.PropertyManager;
import com.epam.java.rt.lab.util.file.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.File;
import java.io.IOException;

@WebListener
public class TempFilesListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        try {
            String extension = PropertyManager.COMMA
                               + httpSessionEvent.getSession().getId();
            File temporaryFile = File.createTempFile("", extension);
            String folderPath = temporaryFile.getParentFile().getParent();
            FileManager.deleteFilesWithExtension(folderPath, extension);
        } catch (IOException | AppException e) {
            e.printStackTrace();
        }
    }
}
