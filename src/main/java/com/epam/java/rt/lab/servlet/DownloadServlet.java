package com.epam.java.rt.lab.servlet;

import com.epam.java.rt.lab.connection.ConnectionException;
import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.service.UserService;
import com.epam.java.rt.lab.util.FormValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * service-ms
 */
@WebServlet(urlPatterns = "/file/download/*")
public class DownloadServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(DownloadServlet.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equals("GET")) {
            logger.debug("DOWNLOAD REQUESTED: {}", req.getPathInfo());
            InputStream inputStream = null;
            switch (req.getPathInfo()) {
                case "/avatar":
                    String avatarId = req.getParameter("id");
                    logger.debug("AVATAR: {}", avatarId);
                    if (FormValidator.isOnlyDigits(avatarId))
                        try {
                            inputStream = (new UserService()).getAvatar(Long.valueOf(avatarId));
                        } catch (DaoException | ConnectionException e) {
                            e.printStackTrace();
                            throw new ServletException(e.getMessage(), e.getCause());
                        }
                    break;
            }
            if (inputStream != null) {
                logger.debug("READY TO DOWNLOAD");
                resp.setContentType("image/jpeg");
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 4096);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(resp.getOutputStream(), 4096);
                byte[] buffer = new byte[4096];
                int length;
                while ((length = bufferedInputStream.read(buffer)) > 0) {
                    bufferedOutputStream.write(buffer, 0, length);
                }
                logger.debug("DOWNLOAD COMPLETE");
            }
        }
    }

}
