/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AttachmentFileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtain the base path to the attachment store
        ServletContext sc = getServletContext();
        String jobAttachmentBasePath = sc.getInitParameter("jobAttachmentBasePath");
        
        // Load the file
        String filename = request.getPathInfo().substring(1);
        File file = new File(jobAttachmentBasePath, filename);
        
        // Respond with the file contents
        response.setHeader("Content-Type", getServletContext().getMimeType(filename));
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", String.format("inline; filename=\"%s\"", filename));
        Files.copy(file.toPath(), response.getOutputStream());
    }
}
