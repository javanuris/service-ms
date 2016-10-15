package com.epam.java.rt.lab.util.file;

import com.epam.java.rt.lab.entity.File;
import com.epam.java.rt.lab.service.CommentService;
import com.epam.java.rt.lab.service.ServiceException;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.TimestampManager;
import com.epam.java.rt.lab.util.UtilException;
import org.h2.util.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;

public final class DownloadManager {

    private DownloadManager() {
    }

    public static void sendFile(String ifModifiedSinceHeader,
                                Timestamp lastModified, String contentType,
                                InputStream fileStream,
                                HttpServletResponse resp) throws UtilException {
        Timestamp ifModifiedSince = ifModifiedSinceHeader != null
                ? TimestampManager.valueOf(ifModifiedSinceHeader)
                : null;
        if (ifModifiedSince == null || lastModified == null
                || TimestampManager.secondsBetweenTimestamps(ifModifiedSince,
                lastModified) > 1) {
            if (lastModified != null) {
                resp.setHeader("Last-Modified", lastModified.toString());
            }
            if (contentType != null) resp.setContentType(contentType);
            try {
                IOUtils.copy(fileStream, resp.getOutputStream());
                fileStream.close();
            } catch (IOException e) {
                throw new UtilException(e.getCause());
            }
        } else {
            resp.setStatus(304);
        }
    }

    public static File getFileFromDatabase(String pathInfo, String id)
            throws UtilException {
        if ("/avatar".equals(pathInfo)) {
            try (UserService userService = new UserService()) {
                return userService.getAvatar(id);
            } catch (ServiceException e) {
                throw new UtilException(e.getCause());
            }
        } else if ("/photo".equals(pathInfo)) {
            try (CommentService commentService = new CommentService()) {
                return commentService.getPhoto(id);
            } catch (ServiceException e) {
                throw new UtilException(e.getCause());
            }
        } else {
            throw new UtilException();
        }
    }

}
