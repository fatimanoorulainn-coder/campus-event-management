package com.campus.controller;

import com.campus.model.User;
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
 * Servlet for handling user login.
 * Supports both username and email authentication.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(LoginServlet.class);
    private AuthService authService;
    
    @Override
    public void init() throws ServletException {
        authService = new AuthService();
        logger.info("LoginServlet initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (authService.isLoggedIn(session)) {
            // Redirect based on role
            User user = authService.getCurrentUser(session);
            if (user != null && user.isAdmin()) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/student/dashboard");
            }
            return;
        }
        
        // Show login page
        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String usernameOrEmail = request.getParameter("username");
        String password = request.getParameter("password");
        
        // Validate input
        if (usernameOrEmail == null || usernameOrEmail.trim().isEmpty() || 
            password == null || password.isEmpty()) {
            
            request.setAttribute("error", "Please enter username/email and password");
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
            return;
        }
        
        try {
            // Authenticate user
            User user = authService.authenticate(usernameOrEmail.trim(), password);
            
            if (user == null) {
                request.setAttribute("error", "Invalid username/email or password");
                request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
                return;
            }
            
            // Create session
            HttpSession session = request.getSession(true);
            authService.createSession(session, user);
            
            // Check if there was a redirect after login
            String redirectUrl = (String) session.getAttribute("redirectAfterLogin");
            if (redirectUrl != null) {
                session.removeAttribute("redirectAfterLogin");
                response.sendRedirect(redirectUrl);
                return;
            }
            
            // Redirect based on role
            if (user.isAdmin()) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/student/dashboard");
            }
            
        } catch (Exception e) {
            logger.error("Error during login process", e);
            request.setAttribute("error", "An error occurred during login. Please try again.");
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
        }
    }
}