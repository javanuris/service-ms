package com.epam.java.rt.lab.web.servlet;

import com.epam.java.rt.lab.util.file.UploadException;
import com.epam.java.rt.lab.util.file.UploadManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * category-ms
 */
@MultipartConfig(maxFileSize = 3145728)
@WebServlet(urlPatterns = "/file/upload/*")
public class UploadServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String contentLengthHeaderValue = req.getHeader("Content-Length");
        if (contentLengthHeaderValue != null) {
            Long contentLength = Long.valueOf(contentLengthHeaderValue);
            try {
                if (contentLength > 3145728) resp.sendError(500, "");
                if (req.getSession().getAttribute("user") != null) {
                    if (req.getMethod().equals("POST")) {
                        String absolutePath = UploadManager.
                                uploadFileAndGetAbsolutePath(req.getSession().getId(),
                                        req.getPathInfo().substring(1), req.getPart("file"));
                        if (absolutePath != null) resp.getWriter().print(absolutePath);
                    }
                }
            } catch (UploadException e) {
                throw new ServletException("exception.web.servlet.upload.upload-file", e.getCause());
            } catch (ServletException | IOException e) {
                throw new ServletException("exception.web.servlet.upload.get-part", e.getCause());
            }
        }
    }

}
