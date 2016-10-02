package com.epam.java.rt.lab.web.servlet;

import com.epam.java.rt.lab.entity.rbac.Avatar;
import com.epam.java.rt.lab.service.ServiceException;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.TimestampCompare;
import com.epam.java.rt.lab.util.validator.Validator;
import com.epam.java.rt.lab.util.validator.ValidatorException;
import com.epam.java.rt.lab.util.validator.ValidatorFactory;
import org.h2.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * service-ms
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
                switch (req.getPathInfo()) {
                    case "/avatar":
                        String avatarId = req.getParameter("id");
                        String avatarPath = req.getParameter("path");
                        String ifModifiedSinceHeader = req.getHeader("If-Modified-Since");
                        if (ifModifiedSinceHeader != null)
                            ifModifiedSince = TimestampCompare.of(ifModifiedSinceHeader);
                        if (avatarId != null) {
                            logger.debug("AVATAR BY ID: {}", avatarId);
                            try (UserService userService = new UserService()) {
                                Avatar avatar = userService.getAvatar(avatarId);
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
                        } else if (avatarPath != null) {
                            logger.debug("AVATAR BY PATH: {}", avatarPath);
                            File file = new File(avatarPath);
                            lastModified = new Timestamp(file.lastModified());
                            contentType = avatarPath.substring(avatarPath.lastIndexOf(".") + 1).replaceAll("_", "/");
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

    private void avatar(String avatarId, String avatarPath) {
        //
    }

}
