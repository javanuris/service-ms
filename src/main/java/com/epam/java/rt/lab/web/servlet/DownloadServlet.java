package com.epam.java.rt.lab.web.servlet;

import com.epam.java.rt.lab.entity.business.Photo;
import com.epam.java.rt.lab.entity.rbac.Avatar;
import com.epam.java.rt.lab.service.CommentService;
import com.epam.java.rt.lab.service.ServiceException;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.TimestampCompare;
import org.h2.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;

/**
 * category-ms
 */
@WebServlet(urlPatterns = "/file/download/*")
public class DownloadServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(DownloadServlet.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") != null) {
            if (req.getMethod().equals("GET")) {
                logger.debug("DOWNLOAD REQUESTED: {}", req.getPathInfo());
                InputStream inputStream = null;
                Timestamp lastModified = null;
                Timestamp ifModifiedSince = null;
                String contentType = null;
                String fileId = req.getParameter("id");
                String filePath = req.getParameter("path");
                String ifModifiedSinceHeader = req.getHeader("If-Modified-Since");
                switch (req.getPathInfo()) {
                    case "/avatar":
                        if (ifModifiedSinceHeader != null)
                            ifModifiedSince = TimestampCompare.of(ifModifiedSinceHeader);
                        if (fileId != null) {
                            logger.debug("AVATAR BY ID: {}", fileId);
                            try (UserService userService = new UserService()) {
                                Avatar avatar = userService.getAvatar(fileId);
                                if (avatar != null) {
                                    lastModified = avatar.getModified();
                                    contentType = avatar.getType();
                                    if (ifModifiedSince != null && TimestampCompare.secondsBetweenTimestamps
                                            (ifModifiedSince, lastModified) < 1) {
                                        resp.setStatus(304);
                                        return;
                                    }
                                    inputStream = avatar.getFile();
                                }
                            } catch (ServiceException e) {
                                e.printStackTrace();
                                throw new ServletException(e);
                            }
                        } else {
                            logger.debug("AVATAR BY PATH: {}", filePath);
                            File file = new File(filePath);
                            lastModified = new Timestamp(file.lastModified());
                            contentType = filePath.substring(filePath.lastIndexOf(".") + 1).replaceAll("_", "/");
                            if (ifModifiedSince != null && TimestampCompare.secondsBetweenTimestamps
                                    (ifModifiedSince, lastModified) < 1) {
                                resp.setStatus(304);
                                return;
                            }
                            inputStream = new FileInputStream(file);
                        }
                    case "/photo":
                        if (ifModifiedSinceHeader != null)
                            ifModifiedSince = TimestampCompare.of(ifModifiedSinceHeader);
                        if (fileId != null) {
                            logger.debug("PHOTO BY ID: {}", fileId);
                            try (CommentService commentService = new CommentService()) {
                                Photo photo = commentService.getPhoto(fileId);
                                if (photo != null) {
                                    lastModified = photo.getModified();
                                    contentType = photo.getType();
                                    if (ifModifiedSince != null && TimestampCompare.secondsBetweenTimestamps
                                            (ifModifiedSince, lastModified) < 1) {
                                        resp.setStatus(304);
                                        return;
                                    }
                                    inputStream = photo.getFile();
                                }
                            } catch (ServiceException e) {
                                e.printStackTrace();
                                throw new ServletException(e);
                            }
                        } else {
                            logger.debug("PHOTO BY PATH: {}", filePath);
                            File file = new File(filePath);
                            lastModified = new Timestamp(file.lastModified());
                            contentType = filePath.substring(filePath.lastIndexOf(".") + 1).replaceAll("_", "/");
                            if (ifModifiedSince != null && TimestampCompare.secondsBetweenTimestamps
                                    (ifModifiedSince, lastModified) < 1) {
                                resp.setStatus(304);
                                return;
                            }
                            inputStream = new FileInputStream(file);
                        }
                }
                if (inputStream != null) {
                    logger.debug("READY TO DOWNLOAD");
                    if (lastModified != null)
                        resp.setHeader("Last-Modified", lastModified.toString());
                    if (contentType != null)
                        resp.setContentType(contentType);
                    IOUtils.copy(inputStream, resp.getOutputStream());
                    inputStream.close();
                    logger.debug("DOWNLOAD COMPLETE");
                }
            }
        }
    }


}
