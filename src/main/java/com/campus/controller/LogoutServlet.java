package com.campus.controller;

import com.campus.service.AuthService;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet for handling user logout.
 * Invalidates the user session.
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(LogoutServlet.class);
    private AuthService authService;
    
    @Override
    public void init() throws ServletException {
        authService = new AuthService();
        logger.info("LogoutServlet initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            authService.logout(session);
        }
        
        // Redirect to login page with logout message
        String contextPath = request.getContextPath();
        response.sendRedirect(contextPath + "/login?logout=success");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Handle POST requests the same way as GET
        doGet(request, response);
    }
}