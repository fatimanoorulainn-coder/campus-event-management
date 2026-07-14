package com.campus.controller.student;

import com.campus.dao.UserDAO;
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
 * Servlet for Student Profile management.
 * Allows students to view and update their profile information.
 */
@WebServlet("/student/profile")
public class StudentProfileServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(StudentProfileServlet.class);
    private AuthService authService;
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        authService = new AuthService();
        userDAO = new UserDAO();
        logger.info("StudentProfileServlet initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (!authService.isLoggedIn(session) || !authService.isStudent(session)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User currentUser = authService.getCurrentUser(session);
        
        // Refresh user data from database
        User updatedUser = userDAO.findById(currentUser.getUserId());
        if (updatedUser != null) {
            // Update session with fresh user data
            session.setAttribute(AuthService.SESSION_USER, updatedUser);
            request.setAttribute("user", updatedUser);
        } else {
            request.setAttribute("user", currentUser);
        }
        
        request.getRequestDispatcher("/WEB-INF/views/student/profile.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (!authService.isLoggedIn(session) || !authService.isStudent(session)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User currentUser = authService.getCurrentUser(session);
        String action = request.getParameter("action");
        
        if ("updateProfile".equals(action)) {
            updateProfile(request, response, currentUser);
        } else if ("updatePassword".equals(action)) {
            updatePassword(request, response, currentUser);
        } else {
            response.sendRedirect(request.getContextPath() + "/student/profile");
        }
    }
    
    private void updateProfile(HttpServletRequest request, HttpServletResponse response, User currentUser) 
            throws ServletException, IOException {
        
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String department = request.getParameter("department");
        String studentId = request.getParameter("studentId");
        
        // Validate input
        if (fullName == null || fullName.trim().isEmpty()) {
            request.setAttribute("error", "Full name is required");
            doGet(request, response);
            return;
        }
        
        // Update user object
        currentUser.setFullName(fullName.trim());
        currentUser.setPhone(phone != null ? phone.trim() : "");
        currentUser.setDepartment(department != null ? department.trim() : "");
        currentUser.setStudentId(studentId != null ? studentId.trim() : "");
        
        try {
            boolean updated = userDAO.updateUser(currentUser);
            
            if (updated) {
                // Update session
                request.getSession().setAttribute(AuthService.SESSION_USER, currentUser);
                request.setAttribute("success", "Profile updated successfully!");
            } else {
                request.setAttribute("error", "Failed to update profile");
            }
        } catch (Exception e) {
            logger.error("Error updating profile", e);
            request.setAttribute("error", "An error occurred while updating profile");
        }
        
        request.setAttribute("user", currentUser);
        request.getRequestDispatcher("/WEB-INF/views/student/profile.jsp").forward(request, response);
    }
    
    private void updatePassword(HttpServletRequest request, HttpServletResponse response, User currentUser) 
            throws ServletException, IOException {
        
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Validate input
        if (currentPassword == null || currentPassword.isEmpty() ||
            newPassword == null || newPassword.isEmpty() ||
            confirmPassword == null || confirmPassword.isEmpty()) {
            
            request.setAttribute("error", "All password fields are required");
            doGet(request, response);
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "New passwords do not match");
            doGet(request, response);
            return;
        }
        
        if (newPassword.length() < 6) {
            request.setAttribute("error", "New password must be at least 6 characters");
            doGet(request, response);
            return;
        }
        
        // Verify current password
        try {
            // Use the stored password hash to verify
            User dbUser = userDAO.findById(currentUser.getUserId());
            if (dbUser == null) {
                request.setAttribute("error", "User not found");
                doGet(request, response);
                return;
            }
            
            // Import PasswordUtil
            if (!com.campus.util.PasswordUtil.verifyPassword(currentPassword, dbUser.getPasswordHash())) {
                request.setAttribute("error", "Current password is incorrect");
                doGet(request, response);
                return;
            }
            
            // Hash new password and update
            String newPasswordHash = com.campus.util.PasswordUtil.hashPassword(newPassword);
            boolean updated = userDAO.updatePassword(currentUser.getUserId(), newPasswordHash);
            
            if (updated) {
                request.setAttribute("success", "Password updated successfully!");
            } else {
                request.setAttribute("error", "Failed to update password");
            }
        } catch (Exception e) {
            logger.error("Error updating password", e);
            request.setAttribute("error", "An error occurred while updating password");
        }
        
        request.setAttribute("user", currentUser);
        request.getRequestDispatcher("/WEB-INF/views/student/profile.jsp").forward(request, response);
    }
}