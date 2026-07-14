package com.campus.controller.admin;

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
import java.util.List;

/**
 * Servlet for Admin User Management.
 * Allows admins to view and manage all users in the system.
 */
@WebServlet("/admin/users")
public class AdminUsersServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(AdminUsersServlet.class);
    private AuthService authService;
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        authService = new AuthService();
        userDAO = new UserDAO();
        logger.info("AdminUsersServlet initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (!authService.isLoggedIn(session) || !authService.isAdmin(session)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
            // Get all students
            List<User> students = userDAO.getAllStudents();
            
            // Search functionality
            String searchParam = request.getParameter("search");
            if (searchParam != null && !searchParam.trim().isEmpty()) {
                students = userDAO.searchUsers(searchParam.trim());
            }
            
            request.setAttribute("students", students);
            request.setAttribute("searchTerm", searchParam);
            
            request.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.error("Error loading admin users", e);
            request.setAttribute("error", "An error occurred while loading users");
            request.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(request, response);
        }
    }
}