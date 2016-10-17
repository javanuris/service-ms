package com.epam.java.rt.lab.web.servlet;

import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.file.UploadManager;
import com.epam.java.rt.lab.web.validator.ValidatorFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;
import static com.epam.java.rt.lab.util.PropertyManager.*;
import static com.epam.java.rt.lab.web.validator.ValidatorFactory.DIGITS;
import static com.epam.java.rt.lab.web.servlet.ServletExceptionCode.CONTENT_LENGTH_ERROR;
import static com.epam.java.rt.lab.web.servlet.ServletExceptionCode.CONTENT_LENGTH_HEADER_ERROR;

@MultipartConfig(maxFileSize = UPLOAD_FILE_MAX_SIZE)
@WebServlet(urlPatterns = FILE_UPLOAD_PATH + SLASH + ASTERISK)
public class UploadServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            if (req.getSession().getAttribute(USER_ATTR) == null) {
                throw new AppException(NULL_NOT_ALLOWED);
            }
            String contentLengthHeaderValue = req.getHeader("Content-Length");
            if (contentLengthHeaderValue == null
                    || ValidatorFactory.getInstance().create(DIGITS).
                    validate(contentLengthHeaderValue) == null) {
                throw new AppException(CONTENT_LENGTH_HEADER_ERROR);
            }
            Long contentLength = Long.valueOf(contentLengthHeaderValue);
            if (contentLength > UPLOAD_FILE_MAX_SIZE) {
                throw new AppException(CONTENT_LENGTH_ERROR);
            }
            if (POST.equals(req.getMethod())) {
                String absolutePath = UploadManager.
                        receiveFileAndGetAbsolutePath(req.getSession().getId(),
                                req.getPathInfo().substring(1),
                                req.getPart("file"));
                resp.getWriter().print(absolutePath);
            }
        } catch (AppException e) {
            throw new ServletException(e.getMessage(), e.getCause());
        }
    }

}
