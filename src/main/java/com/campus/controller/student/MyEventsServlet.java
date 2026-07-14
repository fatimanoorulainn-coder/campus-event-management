package com.campus.controller.student;

import com.campus.model.Event;
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
 * Servlet for displaying events a student is registered for.
 * Allows students to view and cancel their event registrations.
 */
@WebServlet("/student/my-events")
public class MyEventsServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(MyEventsServlet.class);
    private AuthService authService;
    private EventService eventService;
    
    @Override
    public void init() throws ServletException {
        authService = new AuthService();
        eventService = new EventService();
        logger.info("MyEventsServlet initialized");
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
        int userId = currentUser.getUserId();
        
        try {
            // Get all events with registration status
            List<Event> allEvents = eventService.getAllEvents(userId);
            
            // Filter to only show registered events
            List<Event> registeredEvents = allEvents.stream()
                    .filter(Event::isUserRegistered)
                    .collect(java.util.stream.Collectors.toList());
            
            // Check for cancellation message
            String cancelled = request.getParameter("cancelled");
            if ("success".equals(cancelled)) {
                request.setAttribute("successMessage", "Registration cancelled successfully!");
            }
            
            String registered = request.getParameter("registered");
            if ("success".equals(registered)) {
                request.setAttribute("successMessage", "Successfully registered for the event!");
            }
            
            request.setAttribute("registeredEvents", registeredEvents);
            request.setAttribute("user", currentUser);
            
            request.getRequestDispatcher("/WEB-INF/views/student/my-events.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.error("Error loading my events", e);
            request.setAttribute("error", "An error occurred while loading your events");
            request.getRequestDispatcher("/WEB-INF/views/student/my-events.jsp").forward(request, response);
        }
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
        String eventIdParam = request.getParameter("eventId");
        
        if ("cancel".equals(action) && eventIdParam != null) {
            try {
                int eventId = Integer.parseInt(eventIdParam);
                boolean cancelled = eventService.cancelRegistration(currentUser.getUserId(), eventId);
                
                if (cancelled) {
                    response.sendRedirect(request.getContextPath() + "/student/my-events?cancelled=success");
                } else {
                    response.sendRedirect(request.getContextPath() + "/student/my-events?error=failed");
                }
            } catch (NumberFormatException e) {
                logger.error("Invalid event ID", e);
                response.sendRedirect(request.getContextPath() + "/student/my-events?error=invalid");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/student/my-events");
        }
    }
}