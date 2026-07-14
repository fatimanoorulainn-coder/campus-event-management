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
 * Servlet for handling user registration.
 * Validates registration data and creates new user accounts.
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(RegisterServlet.class);
    private AuthService authService;
    
    @Override
    public void init() throws ServletException {
        authService = new AuthService();
        logger.info("RegisterServlet initialized");
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
        
        // Show registration page
        request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get registration parameters
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String department = request.getParameter("department");
        String studentId = request.getParameter("studentId");
        
        // Validate required fields
        if (username == null || username.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.isEmpty() ||
            confirmPassword == null || confirmPassword.isEmpty() ||
            fullName == null || fullName.trim().isEmpty()) {
            
            request.setAttribute("error", "All required fields must be filled");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }
        
        // Validate password match
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }
        
        // Validate username length
        if (username.length() < 3 || username.length() > 50) {
            request.setAttribute("error", "Username must be between 3 and 50 characters");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }
        
        // Validate email format (basic validation)
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            request.setAttribute("error", "Please enter a valid email address");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }
        
        // Create user object
        User user = new User();
        user.setUsername(username.trim());
        user.setEmail(email.trim());
        user.setFullName(fullName.trim());
        user.setPhone(phone != null ? phone.trim() : "");
        user.setDepartment(department != null ? department.trim() : "");
        user.setStudentId(studentId != null ? studentId.trim() : "");
        user.setRole("student"); // Default role for new users
        
        try {
            // Register the user
            boolean registered = authService.registerUser(user, password);
            
            if (registered) {
                // Registration successful - login the user
                User authenticatedUser = authService.authenticate(username, password);
                if (authenticatedUser != null) {
                    HttpSession session = request.getSession(true);
                    authService.createSession(session, authenticatedUser);
                    
                    // Redirect to student dashboard
                    response.sendRedirect(request.getContextPath() + "/student/dashboard?registered=success");
                } else {
                    // Should not happen, but just in case
                    request.setAttribute("success", "Registration successful! Please login.");
                    request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
                }
            } else {
                // Registration failed - show error
                request.setAttribute("error", "Registration failed. Username or email may already exist.");
                request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            logger.error("Error during registration", e);
            request.setAttribute("error", "An error occurred during registration. Please try again.");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
        }
    }
}