package com.epam.java.rt.lab.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * service-ms
 */
@MultipartConfig
@WebServlet(urlPatterns = "/file/upload")
public class UploadServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(UploadServlet.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equals("POST")) {
            logger.debug("UPLOAD FILE");
            Part filePart = req.getPart("file");
            InputStream inputStream = filePart.getInputStream();
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            File outputFile = File.createTempFile(fileName.concat("_upload"), ".tmp");
            Files.copy(inputStream, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            logger.debug("UPLOAD COMPLETE");
            resp.getWriter().print("filePath=".concat(outputFile.getAbsolutePath()));
        }
    }

}
