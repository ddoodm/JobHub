package com.deinyon.aip.jobhub.servlets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A Servlet which deals with storage of users' attachments.
 * 
 * @remarks This Servlet is partially implemented, and does not function. This
 * feature has not been entirely implemented.
 * @author Deinyon Davies <deinyond@gmail.com>
 */
public class AttachmentFileServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
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
