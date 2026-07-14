package com.campus.controller.student;

import com.campus.model.Event;
import com.campus.model.Registration;
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
import java.util.List;

/**
 * Servlet for Student Dashboard.
 * Displays overview, upcoming events, and registered events for students.
 */
@WebServlet("/student/dashboard")
public class StudentDashboardServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(StudentDashboardServlet.class);
    private AuthService authService;
    private EventService eventService;
    
    @Override
    public void init() throws ServletException {
        authService = new AuthService();
        eventService = new EventService();
        logger.info("StudentDashboardServlet initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Check if user is logged in and is a student
        if (!authService.isLoggedIn(session) || !authService.isStudent(session)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User currentUser = authService.getCurrentUser(session);
        int userId = currentUser.getUserId();
        
        try {
            // Get upcoming events
            List<Event> upcomingEvents = eventService.getUpcomingEvents();
            
            // Get events the student is registered for
            List<Event> registeredEvents = eventService.getAllEvents(userId);
            // Filter to only show registered events
            registeredEvents.removeIf(event -> !event.isUserRegistered());
            
            // Get all events with registration status
            List<Event> allEvents = eventService.getAllEvents(userId);
            
            // Get total registered count for the user
            int totalRegistered = registeredEvents.size();
            
            // Set attributes for JSP
            request.setAttribute("user", currentUser);
            request.setAttribute("upcomingEvents", upcomingEvents);
            request.setAttribute("registeredEvents", registeredEvents);
            request.setAttribute("allEvents", allEvents);
            request.setAttribute("totalRegistered", totalRegistered);
            
            // Check for registration success message
            String registered = request.getParameter("registered");
            if ("success".equals(registered)) {
                request.setAttribute("successMessage", "Welcome! Your account has been created successfully.");
            }
            
            request.getRequestDispatcher("/WEB-INF/views/student/dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.error("Error loading student dashboard", e);
            request.setAttribute("error", "An error occurred while loading the dashboard");
            request.getRequestDispatcher("/WEB-INF/views/student/dashboard.jsp").forward(request, response);
        }
    }
}