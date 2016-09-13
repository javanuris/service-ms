package com.epam.java.rt.lab.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * service-ms
 */
@WebServlet(urlPatterns = "/file/upload/*")
public class UploadServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(FrontServlet.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equals("POST")) {
            String uploadFileName = req.getPathInfo();
            if (uploadFileName.equals("")) {
                resp.getWriter().print("");
            } else {
                logger.debug("UPLOAD FILE {}", uploadFileName);
                String outputFileName = null;
                InputStream inputStream = req.getInputStream();
                try {
                    if (inputStream != null) {
                        outputFileName = File.createTempFile(uploadFileName.concat("_upload"), ".tmp").getAbsolutePath();
                        logger.debug("OUTPUT FILE = {}", outputFileName);
                        OutputStream outputStream = new FileOutputStream(outputFileName);
                        try {
                            byte[] buffer = new byte[4096];
                            for (int n; (n = inputStream.read(buffer)) != -1; )
                                outputStream.write(buffer, 0, n);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            outputStream.close();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    inputStream.close();
                }
                logger.debug("UPLOAD COMPLETE");
                if (outputFileName == null) {
                    resp.getWriter().print("");
                } else {
                    resp.getWriter().print(outputFileName);
                }
            }
        }
    }

}
