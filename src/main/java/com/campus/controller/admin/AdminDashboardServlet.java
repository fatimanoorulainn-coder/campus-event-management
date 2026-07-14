package com.campus.controller.admin;

import com.campus.model.User;
import com.campus.service.AuthService;
import com.campus.service.EventService;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Servlet for Admin Dashboard.
 * Displays statistics, overview, and quick actions for administrators.
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(AdminDashboardServlet.class);
    private AuthService authService;
    private EventService eventService;
    
    @Override
    public void init() throws ServletException {
        authService = new AuthService();
        eventService = new EventService();
        logger.info("AdminDashboardServlet initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Check if user is logged in and is an admin
        if (!authService.isLoggedIn(session) || !authService.isAdmin(session)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User currentUser = authService.getCurrentUser(session);
        
        try {
            // Get dashboard statistics
            Object[] stats = eventService.getDashboardStats();
            int totalEvents = (int) stats[0];
            int totalRegistrations = (int) stats[1];
            int totalUsers = (int) stats[2];
            int totalStudents = (int) stats[3];
            
            // Format numbers for display
            NumberFormat formatter = NumberFormat.getInstance(Locale.US);
            
            request.setAttribute("user", currentUser);
            request.setAttribute("totalEvents", totalEvents);
            request.setAttribute("totalRegistrations", totalRegistrations);
            request.setAttribute("totalUsers", totalUsers);
            request.setAttribute("totalStudents", totalStudents);
            request.setAttribute("totalEventsFormatted", formatter.format(totalEvents));
            request.setAttribute("totalRegistrationsFormatted", formatter.format(totalRegistrations));
            request.setAttribute("totalUsersFormatted", formatter.format(totalUsers));
            request.setAttribute("totalStudentsFormatted", formatter.format(totalStudents));
            
            request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.error("Error loading admin dashboard", e);
            request.setAttribute("error", "An error occurred while loading the dashboard");
            request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
        }
    }
}