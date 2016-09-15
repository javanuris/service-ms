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
import java.util.*;

/**
 * service-ms
 */
@MultipartConfig
@WebServlet(urlPatterns = "/file/upload/*")
public class UploadServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(UploadServlet.class);
    private static Map<String, List<String>> contentTypeListMap = new HashMap<>();

    private static void initContentTypeListMap() throws IOException {
        Properties properties = new Properties();
        properties.load(UploadServlet.class.getClassLoader().getResourceAsStream("content-types.properties"));
        Enumeration names = properties.propertyNames();
        while (names.hasMoreElements()) {
            String key = (String) names.nextElement();
            List<String> types = new ArrayList<>();
            for (String type : properties.getProperty(key).split(",")) types.add(type);
            contentTypeListMap.put(key, types);
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("userId") != null) {
            if (req.getMethod().equals("POST")) {
                logger.debug("UPLOAD FILE");
                if (UploadServlet.contentTypeListMap.size() == 0) initContentTypeListMap();
                Part filePart = req.getPart("file");
                String prefix = null;
                String postfix = null;
                switch (req.getPathInfo()) {
                    case "/avatar":
                        logger.debug("contentType = {} (valid: {})", filePart.getContentType(), contentTypeListMap.get("avatar").toArray());
                        if (UploadServlet.contentTypeListMap.get("avatar").contains(filePart.getContentType())) {
                            prefix = ".avatar.".concat(filePart.getContentType().replaceAll("/", "_")).concat(".");
                            postfix = ".".concat(req.getSession().getId());
                        }
                        break;
                }
                if (prefix != null) {
                    InputStream inputStream = filePart.getInputStream();
                    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                    File outputFile = File.createTempFile(fileName.concat(prefix), postfix);
                    Files.copy(inputStream, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    logger.debug("UPLOAD COMPLETE");
                    resp.getWriter().print("filePath=".concat(outputFile.getAbsolutePath()));
                    return;
                }
            }
        }
        resp.getWriter().print("");
    }

}
