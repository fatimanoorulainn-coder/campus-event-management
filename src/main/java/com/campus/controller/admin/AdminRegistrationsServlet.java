package com.campus.controller.admin;

import com.campus.dao.RegistrationDAO;
import com.campus.model.Registration;
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
 * Servlet for Admin Registration Management.
 * Allows admins to view all registrations in the system.
 */
@WebServlet("/admin/registrations")
public class AdminRegistrationsServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(AdminRegistrationsServlet.class);
    private AuthService authService;
    private RegistrationDAO registrationDAO;
    
    @Override
    public void init() throws ServletException {
        authService = new AuthService();
        registrationDAO = new RegistrationDAO();
        logger.info("AdminRegistrationsServlet initialized");
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
            // Get all registrations
            List<Registration> registrations = registrationDAO.getAllRegistrations();
            
            request.setAttribute("registrations", registrations);
            request.getRequestDispatcher("/WEB-INF/views/admin/registrations.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.error("Error loading admin registrations", e);
            request.setAttribute("error", "An error occurred while loading registrations");
            request.getRequestDispatcher("/WEB-INF/views/admin/registrations.jsp").forward(request, response);
        }
    }
}