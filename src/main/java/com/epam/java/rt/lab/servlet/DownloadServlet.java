package com.epam.java.rt.lab.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * service-ms
 */
@WebServlet(urlPatterns = "/file/download/*")
public class DownloadServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(FrontServlet.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equals("GET")) {
            String downloadFileName = req.getPathInfo();

        }
    }

}
