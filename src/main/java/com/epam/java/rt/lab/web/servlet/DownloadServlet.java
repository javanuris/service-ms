package com.epam.java.rt.lab.web.servlet;

import com.epam.java.rt.lab.entity.File;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.PropertyManager;
import com.epam.java.rt.lab.util.file.DownloadManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Timestamp;

/**
 * category-ms
 */
@WebServlet(urlPatterns = "/file/download/*")
public class DownloadServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {
        if (req.getSession().getAttribute("user") != null
                && "GET".equals(req.getMethod())) {
            String id = req.getParameter("id");
            String path = req.getParameter("path");
            try {
                if (id != null) {
                    File file = DownloadManager.
                            getFileFromDatabase(req.getPathInfo(), id);
                    DownloadManager.sendFile(req.getHeader("If-Modified-Since"),
                            file.getModified(), file.getType(), file.getFile(),
                            resp);
                } else if (path != null) {
                    java.io.File file = new java.io.File(path);
                    Integer lastPointIndex = path.
                            lastIndexOf(PropertyManager.POINT) + 1;
                    String contentType = path.substring(lastPointIndex);
                    contentType = contentType.
                            replaceAll(PropertyManager.UNDERSCORE,
                                    PropertyManager.SLASH);
                    DownloadManager.sendFile(req.getHeader("If-Modified-Since"),
                            new Timestamp(file.lastModified()), contentType,
                            new FileInputStream(file), resp);
                }
            } catch (AppException | FileNotFoundException e) {
                throw new ServletException(e.getCause());
            }
        }
    }

}
