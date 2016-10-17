package com.epam.java.rt.lab.util.file;

import com.epam.java.rt.lab.entity.File;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.service.CommentService;
import com.epam.java.rt.lab.service.ServiceException;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.TimestampManager;
import org.h2.util.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;

import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;
import static com.epam.java.rt.lab.util.file.FileExceptionCode.FILE_ACCESS_ERROR;
import static com.epam.java.rt.lab.util.file.FileExceptionCode.FILE_NOT_FOUND;

public final class DownloadManager {

    private DownloadManager() {
    }

    public static void sendFile(String ifModifiedSinceHeader,
                                Timestamp lastModified, String contentType,
                                InputStream fileStream,
                                HttpServletResponse resp) throws AppException {
        if (fileStream == null || resp == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        Timestamp ifModifiedSince = ((!TimestampManager.
                isTimestamp(ifModifiedSinceHeader)) ? null
                : Timestamp.valueOf(ifModifiedSinceHeader));
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
                throw new AppException(FILE_ACCESS_ERROR,
                        e.getMessage(), e.getCause());
            }
        } else {
            resp.setStatus(304);
        }
    }

    public static File getFileFromDatabase(String pathInfo, String id)
            throws AppException {
        if (pathInfo == null || id == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        if ("/avatar".equals(pathInfo)) {
            try (UserService userService = new UserService()) {
                return userService.getAvatar(id);
            } catch (ServiceException e) {
                String[] detailArray = {pathInfo, id};
                throw new AppException(FILE_NOT_FOUND,
                        e.getMessage(), e.getCause(), detailArray);
            }
        } else if ("/photo".equals(pathInfo)) {
            try (CommentService commentService = new CommentService()) {
                return commentService.getPhoto(id);
            } catch (ServiceException e) {
                String[] detailArray = {pathInfo, id};
                throw new AppException(FILE_NOT_FOUND,
                        e.getMessage(), e.getCause(), detailArray);
            }
        } else {
            String[] detailArray = {pathInfo, id};
            throw new AppException(FILE_NOT_FOUND, detailArray);
        }
    }

}
